package com.elevate.edw.sqlservercdc.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mortbay.log.Log;

import com.elevate.edw.jackson.model.DataSet;
import com.elevate.edw.sqlservercdc.writer.DataSetWriter;
import com.elevate.edw.sqlservercdc.writer.WriteConfigurationException;

public class ReceiverController implements Callable<String> {
	public static final Logger LOG = LogManager.getLogger(ReceiverController.class);
	private volatile boolean running = false;
	private volatile boolean shutdown = false;
	private boolean initialized;
	private Lock stateLock = new ReentrantLock();
	private long _SLEEPINTERVAL = 1000;
	private ReceiverConfiguration receiverConfig;

	private ExecutorService singleThreadExecutor;

	private Integer threadCount;

	private List<ReceiverCallable> consumerList = new ArrayList<ReceiverCallable>();;

	public ReceiverController() {
	}

	public ReceiverController(ReceiverConfiguration receiverConfig) {
		this.receiverConfig = receiverConfig;
	}

	public String startReceiver() {
		if (this.isRunning()) {
			return "receiver is running, cannot be started";
		} else {
			this.setShutdown(false);
			if (!initialized) {
				try {
					stateLock.lock();
					try {
						init();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} finally {
					stateLock.unlock();
				}
			}
			singleThreadExecutor = Executors.newSingleThreadExecutor();
			singleThreadExecutor.submit(this);
			return "receiver started";
		}
	}

	public String stopReceiver() {
		if (!this.isRunning()) {
			return "receiver is not running, cannot be stopped";
		} else {
			this.setShutdown(true);
			try {
				this.singleThreadExecutor.shutdown();
				this.singleThreadExecutor.awaitTermination(_SLEEPINTERVAL * 5, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// ignore exception
			}
			this.running = false;
			return "receiver is shutdown!";
		}
	}
	
	public String rcvSleepInterval(long interval)
	{
		if (interval > 500 && interval < 60000)
		{
			this.set_SLEEPINTERVAL(interval);
			LOG.info("sleep interval set to " + interval);
			return "sleep interval set to " + interval;
		}
		else
			return "interval out of range, sleep interval was not changed.";
	}

	@Override
	public String call() throws Exception {
		this.setRunning(true);
		LOG.info("ReceiverController start executing");
		this.execute();
		this.setRunning(false);
		// the kafkaconsumer cannot be resuled after shutdown.
		// clean up the callable and release resources
		this.deinit();
		LOG.info("ReceiverController is exiting");
		return null;
	}

	private void deinit() {
		try {
			this.stateLock.lock();
			this.consumerList.clear();
			this.initialized = false;
		} finally {
			stateLock.unlock();
		}

	}

	private void execute() throws InterruptedException, ReciverConfigruationException, WriteConfigurationException {

		ExecutorService executor = Executors.newFixedThreadPool(this.threadCount,
				new SimpleThreadFactory(this.receiverConfig.getTopics().get(0) + ".receivercallalbe", 0));
		LOG.info("start kafka listner container");
		List<Future<Map<TopicPartition, OffsetAndMetadata>>> callableReturn = new ArrayList<Future<Map<TopicPartition, OffsetAndMetadata>>>();
		for (ReceiverCallable c : this.consumerList) {
			callableReturn.add(executor.submit(c));
		}
		int loopCnt = 0;
		while (!shutdown) {
			// sleep to check condition
			Thread.sleep(_SLEEPINTERVAL);
			// first let's get the last checkpoint from each callable and
			// persist into history
			loopCnt++;
			try {
				// 300 second persist interval
				if (_SLEEPINTERVAL * loopCnt > 300000) {
					loopCnt = 0;
					persistHistory();

				}
			} catch (BookmarkException e1) {
				LOG.fatal("cannot persist to history" + e1.getMessage());
				shutdown = true;
				break;
			}
			for (Future<Map<TopicPartition, OffsetAndMetadata>> f : callableReturn) {
				if (f.isCancelled()) {
					LOG.fatal("Exception happened in task, receiver is shutting down");
					shutdown = true;
					break;
				}
				if (f.isDone()) {
					try {
						f.get();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						LOG.fatal(e.getMessage());
						e.printStackTrace();
						LOG.fatal("Exception happened in task, receiver is shutting down");
						shutdown = true;
						break;
					}
				}

			}

		}

		LOG.info("stop kafka listner container");
		for (ReceiverCallable c : this.consumerList) {
			c.shutdown();
		}
		try {
			persistHistory();
		} catch (BookmarkException e1) {
			LOG.fatal("cannot persist to history" + e1.getMessage());
		}
		executor.shutdown();
		executor.awaitTermination(_SLEEPINTERVAL * 5, TimeUnit.MILLISECONDS);

		LOG.info("ReceiverController shutdown");
	}

	/**
	 * Persisit history bookmark
	 * 
	 * @throws BookmarkException
	 */
	private void persistHistory() throws BookmarkException {
		DatabaseBookmarkStore bs = receiverConfig.getBookmarkStore();
		Map<TopicPartition, OffsetAndMetadata> currentOffsets = this.getCurrentOffsets();
		for (TopicPartition p : currentOffsets.keySet()) {
			LOG.debug(String.format("persist history partition = %d offset = %d meta = %s", p.partition(),
					currentOffsets.get(p).offset(), currentOffsets.get(p).metadata()));
			bs.storeOffsetHistory(p, currentOffsets.get(p));
		}
	}

	/**
	 * Iterate through the consumers and find the current offset by comparing
	 * partition and offset num
	 * 
	 * @return
	 */
	private Map<TopicPartition, OffsetAndMetadata> getCurrentOffsets() {
		Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<TopicPartition, OffsetAndMetadata>();
		for (ReceiverCallable c : consumerList) {
			Map<TopicPartition, OffsetAndMetadata> toff = c.getCurrentOffsets();
			for (TopicPartition p : toff.keySet()) {
				OffsetAndMetadata o = toff.get(p);
				if (!currentOffsets.containsKey(p) || currentOffsets.get(p).offset() < o.offset()) {
					currentOffsets.put(p, o);
				}
			}
		}
		return currentOffsets;
	}

	/**
	 * Initialize the controlller there is no lock required in this code block
	 * since this method is only called once by execute() which acquired the
	 * lock before calling
	 * 
	 * @throws ReciverConfigruationException
	 * @throws WriteConfigurationException
	 */
	public void init() throws ReciverConfigruationException, WriteConfigurationException {
		LOG.info("init() method is called");
		this.threadCount = receiverConfig.getThreadCount();
		if (receiverConfig.getTopics() != null)

			for (int i = 0; i < this.threadCount; i++) {
				ReceiverCallable r = new ReceiverCallable();

				KafkaConsumer<Integer, DataSet> consumer = new KafkaConsumer<Integer, DataSet>(
						receiverConfig.getKafkaConsumerProps());

				// rebalance listner this listener will adjust the
				// bookmark position for each consumer when partition is
				// assigned

				RebalanceListener rb = new RebalanceListener(r);

				if (receiverConfig.getTopics() != null) {
					consumer.subscribe(receiverConfig.getTopics(), rb);
				} else if (receiverConfig.getTopicsPattern() != null) {
					consumer.subscribe(receiverConfig.getTopicsPattern(), rb);
				} else {
					consumer.close();
					throw new ReciverConfigruationException("cannot find topics in configuraiton");
				}

				r.setConsumer(consumer);
				DataSetWriter writer = receiverConfig.newWriterInstance();
				r.setWriter(writer);
				r.setBookmarkStore(receiverConfig.getBookmarkStore());
				r.setDataTransformer(receiverConfig.getDataTransformer());
				// consumer.subscribe(Collections.singletonList(topic));
				// kafka auto partition assign wont happen until poll
				// https://issues.apache.org/jira/browse/KAFKA-2359
				// simply call a poll to get the partition assigned.
				// consumer.poll(0);

				// LOG.info("CONSUMER ASSIGNED PARTITIONS:
				// "+consumer.assignment());
				consumerList.add(r);
			}
		this.initialized = true;
		LOG.info("init completed");

	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		try {
			stateLock.lock();
			this.running = running;
		} finally {
			stateLock.unlock();
		}

	}

	public boolean isInitialized() {
		try {
			stateLock.lock();
			return this.initialized;
		} finally {
			stateLock.unlock();
		}
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public void setShutdown(boolean shutdown) {
		try {
			stateLock.lock();
			this.shutdown = shutdown;
		} finally {
			stateLock.unlock();
		}
	}
	public long get_SLEEPINTERVAL() {
		return _SLEEPINTERVAL;
	}

	public void set_SLEEPINTERVAL(long _SLEEPINTERVAL) {
		this._SLEEPINTERVAL = _SLEEPINTERVAL;
	}

	public ReceiverConfiguration getReceiverConfig() {
		return receiverConfig;
	}

	public void setReceiverConfig(ReceiverConfiguration receiverConfig) {
		this.receiverConfig = receiverConfig;
	}

	public Map<String, Object> getStatus() {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("isShutdown", this.shutdown);
		ret.put("isInit", this.initialized);
		ret.put("isRunning", this.running);
		Map<TopicPartition, OffsetAndMetadata> offsets = this.getCurrentOffsets();
		Map<String, String> offstr = new TreeMap<String, String>();
		for (TopicPartition tp : offsets.keySet()) {
			offstr.put(tp.toString(), offsets.get(tp).toString());
		}
		ret.put("Bookmarks", offstr);
		return ret;
	}

}
