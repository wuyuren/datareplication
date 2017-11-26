package com.elevate.edw.sqlservercdc.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.InvalidOffsetException;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elevate.edw.jackson.model.DataSet;
import com.elevate.edw.sqlservercdc.writer.DataSetWriter;
import com.elevate.edw.sqlservercdc.writer.hbase.HbaseWriterCallable;
import com.elevate.edw.sqlservercdc.writer.transform.DataTransformer;

/**
 * 
 * @author ywu
 *
 */
public class ReceiverCallable implements Callable<Map<TopicPartition, OffsetAndMetadata>> {
	public static final Logger LOG = LogManager.getLogger(ReceiverCallable.class);

	private KafkaConsumer<Integer, DataSet> consumer;
	private DataSetWriter writer;
	private DatabaseBookmarkStore bookmarkStore;
	private String threadName;
	private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new ConcurrentHashMap<TopicPartition, OffsetAndMetadata>();
	// shutdown mechanism
	private AtomicBoolean closed = new AtomicBoolean();
	private CountDownLatch shutdownLatch = new CountDownLatch(1);
	// futres holder to cancel the writer thread
	private ArrayList<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();

	private HbaseWriterCallable hbaseWriterCallable;
	private DataTransformer dataTransformer;

	// shutdown the consumer by interrupt poll operation
	public void shutdown() {
		try {
			this.hbaseWriterCallable.setShutdown(true);
			closed.set(true);
			LOG.debug("CLOSED = " + closed.get());
			consumer.wakeup();
			// shutdown gracefully
			shutdownLatch.await(5, TimeUnit.SECONDS);
			LOG.info("thread :" + this.threadName + " latch released!");
		} catch (InterruptedException e) {
			LOG.error("Error", e);
		}
	}

	public DataSetWriter getWriter() {
		return writer;
	}

	public void setWriter(DataSetWriter writer) {
		this.writer = writer;
	}

	public KafkaConsumer<Integer, DataSet> getConsumer() {
		return consumer;
	}

	public void setConsumer(KafkaConsumer<Integer, DataSet> consumer) {
		this.consumer = consumer;
	}

	public DatabaseBookmarkStore getBookmarkStore() {
		return bookmarkStore;
	}

	public void setBookmarkStore(DatabaseBookmarkStore bookmarkStore) {
		this.bookmarkStore = bookmarkStore;
	}
	public DataTransformer getDataTransformer() {
		return dataTransformer;
	}

	public void setDataTransformer(DataTransformer dataTransformer) {
		this.dataTransformer = dataTransformer;
	}

	@Override
	public Map<TopicPartition, OffsetAndMetadata> call() throws ReceiverException {
		TopicPartition[] emptyArray = new TopicPartition[0];
		this.hbaseWriterCallable = new HbaseWriterCallable(currentOffsets, writer);
		currentOffsets.clear();
		this.threadName = Thread.currentThread().getName();
		// hbase writer executor
		ExecutorService executor = Executors.newFixedThreadPool(1,
				new SimpleThreadFactory(this.threadName + "_hbaseWriter"));
		this.closed.set(false);

		// adjust position to makesure the consumer start from persisted
		// offset
		try {
			consumer.poll(0);
		} catch (RuntimeException e) {
			LOG.fatal(e);
			throw new ReceiverException(e);
		}

		try {
			for (TopicPartition p : consumer.assignment()) {
				this.adjustPositionForPartitionFromBookmarkStore(p);
			}
		} catch (BookmarkException b) {
			LOG.error(b.getMessage());
			throw new ReceiverException(b);
		}

		try {
			LOG.info("receiver callable thread start!" + this.threadName);
			LOG.info(this.threadName + "  Consumer partitions: " + consumer.assignment());
			while (!closed.get()) {
				ConsumerRecords<Integer, DataSet> records = consumer.poll(1000);
				// skip if no record found
				if (records.isEmpty()) {
					LOG.debug(this.threadName + " poll return zero records");
					continue;
				}
				// pause all partitions
				consumer.pause(consumer.assignment().toArray(emptyArray));
				LOG.debug(this.threadName + " paused consumer " + consumer.assignment());
				records = processTransformations(records);
				hbaseWriterCallable.setRecords(records);
				Future<Boolean> future = executor.submit(hbaseWriterCallable);
				futures.add(future);
				// handle poll to the writer thread
				Boolean writerCompleted = false;
				while (!writerCompleted && !closed.get()) {
					try {
						writerCompleted = future.get(3, TimeUnit.SECONDS);
					} catch (TimeoutException e) {
						LOG.debug(this.threadName + " Time out , send poll to keep kafka alive");
						consumer.poll(0);
						commitOffsets(currentOffsets);
					} catch (ExecutionException | InterruptedException e) {
						LOG.error("error while consumer records");
						throw new ReceiverException(e);
					} catch (WakeupException e) {
						LOG.warn(e);
						continue;
					} catch (KafkaException e) {
						LOG.fatal(e);
						throw new ReceiverException(e);
					}
				}
				// resume consumption and commit offset
				futures.remove(future);
				consumer.resume(consumer.assignment().toArray(emptyArray));
				LOG.debug(this.threadName + " resumed consumer " + consumer.assignment());
				this.commitOffsets(currentOffsets);
			}
			LOG.debug("closed " + closed.get());
		} catch (BookmarkException e) {
			LOG.error("Bookmark exception occured " + this.threadName);
			throw new ReceiverException(e);
		} catch (Exception e) {
			LOG.fatal(e);
			throw new ReceiverException(e);
		} finally {
			try {
				LOG.debug(this.threadName + "closed =  " + closed.get());

				LOG.info("shutdown executor in " + this.threadName);
				executor.shutdown();
				while (!executor.awaitTermination(5, TimeUnit.SECONDS))
					;
			} catch (InterruptedException e) {
				LOG.fatal(e);
			}

			consumer.close();
			shutdownLatch.countDown();
			LOG.info("receiver callable thread ended! threadnmae = " + this.threadName);
		}
		return currentOffsets;
	}

	private void commitOffsets(Map<TopicPartition, OffsetAndMetadata> currentOffsets) throws BookmarkException {
		if (!currentOffsets.isEmpty()) {
			for (TopicPartition p : currentOffsets.keySet()) {
				persistBookmark(p, currentOffsets.get(p));
			}
			try {
				consumer.commitSync(currentOffsets);
			} catch (CommitFailedException e) {
				
				//ignore commit failures.. 
				LOG.warn(e);
			}

		}
	}

	private void persistBookmark(TopicPartition p, OffsetAndMetadata lastoffset) throws BookmarkException {
		bookmarkStore.storeBookmark(new Bookmark(p, lastoffset));
	}

	protected void persistPosition(TopicPartition p, long pos) throws BookmarkException {
		LOG.debug(
				"thread : " + this.threadName + " is persisit position partition =" + p.partition() + " pos = " + pos);
		bookmarkStore.storeBookmark(new Bookmark(p, new OffsetAndMetadata(pos, this.threadName)));

	}

	/**
	 * Adjust the bookmark position for a partition based on bookmark
	 * 
	 * @param p
	 * @throws BookmarkException
	 */
	public void adjustPositionForPartitionFromBookmarkStore(TopicPartition p) throws BookmarkException {
		Bookmark b = bookmarkStore.retrieveBookmark(p.topic(), p.partition());
		if (b != null) {
			LOG.info("thread : " + this.threadName + " is seeking position partition =" + p.partition() + " pos = "
					+ b.getOffset().offset());
			if (b.getOffset().offset() == 0)
				consumer.seekToBeginning(p);
			// consumer.seekToBeginning(Collections.singleton(p));
			consumer.seek(p, b.getOffset().offset());
		}

	}

	public Map<TopicPartition, OffsetAndMetadata> getCurrentOffsets() {
		return currentOffsets;
	}

	public ArrayList<Future<Boolean>> getFutures() {
		return futures;
	}

	public String getThreadName() {
		return this.threadName;
	}
    public ConsumerRecords<Integer, DataSet> processTransformations(ConsumerRecords<Integer, DataSet> records) {
        ConsumerRecords<Integer, DataSet> ret = null;
        Map<TopicPartition, List<ConsumerRecord<Integer, DataSet>>> newData = new HashMap<TopicPartition, List<ConsumerRecord<Integer, DataSet>>>();  
        for(TopicPartition p : records.partitions()){
            List<ConsumerRecord<Integer, DataSet>> rs = records.records(p);
            List<ConsumerRecord<Integer, DataSet>> transformSet = new ArrayList<ConsumerRecord<Integer, DataSet>>();
            for (ConsumerRecord<Integer, DataSet> r : rs) {
				DataSet val = r.value();
				DataSet newds = dataTransformer.transformData(val);
				Integer x = r.key();
				//System.out.println(r.value().getSchemaName());
				//System.out.println(r.value().getTableName());
				transformSet.add(new ConsumerRecord<Integer, DataSet>(r.topic(), r.partition(), r.offset(), x, newds));
			}
            newData.put(p, transformSet);
        }
        ret = new ConsumerRecords<Integer, DataSet>(newData);
        return ret;
 }

}
