package com.elevate.edw.sqlservercdc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elevate.edw.sqlservercdc.kafka.SimpleThreadFactory;
import com.elevate.edw.sqlservercdc.kafka.TableVersion;
import com.elevate.edw.sqlservercdc.metamodel.Table;
import com.elevate.edw.sqlservercdc.util.MetaUtil;
import com.elevate.edw.sqlservercdc.util.SqlUtil;

public class CDTController implements Callable<String> {
	public static final Logger LOG = LogManager.getLogger(CDTController.class);
	private CDTConfiguration config;
	private volatile boolean running = false;
	private volatile boolean shutdown = false;
	private boolean initialized = false;
	private MetaUtil metaUtil = null;
	private Map<String, Table> tableMeta;
	private ExecutorService singleThreadExecutor;

	private int maxThreadCount = 1;

	private long _SLEEPINTERVAL = 1000;
	// concurrenthashmap.. this is the run queue.. need to be
	// concurrent so that it will be threadsafe
	private ConcurrentHashMap<Table, CDTCallable> callableMap;
	private ConcurrentHashMap<Table, CDTCallable> runMap;
	private ConcurrentHashMap<Table, FullRefreshCallable> fullRefreshMap;
	private Lock stateLock = new ReentrantLock();

	private boolean debug = false;

	private VersionStore<Table> versionStore;

	public CDTController(CDTConfiguration config) {
		this.config = config;
	}

	private void execute() throws CDTException {
		if (!initialized) {
			try {
				stateLock.lock();
				init();
			} catch (Exception e) {
				LOG.fatal(e.getMessage());
				throw new CDTException(e);
			} finally {
				stateLock.unlock();
				LOG.debug("state lock released");
			}
		}

		SimpleThreadFactory cdtFactory = new SimpleThreadFactory(this.config.getKafkatopic() + ".CDTCallable", 0);
		SimpleThreadFactory refreshFactory = new SimpleThreadFactory(
				this.config.getKafkatopic() + ".FullrefreshCallable", 0);
		ExecutorService cdtExecutor = Executors.newFixedThreadPool(maxThreadCount, cdtFactory);
		ExecutorService fullRefreshExecutor = Executors.newFixedThreadPool(maxThreadCount, refreshFactory);
		LOG.debug(Thread.currentThread().getName() + " successfully created all thread pools");
		Map<Table, Future<Boolean>> cdtReturn = new HashMap<Table, Future<Boolean>>();
		Map<Table, Future<Boolean>> fullRefreshReturn = new HashMap<Table, Future<Boolean>>();
		while (!shutdown) {
			if (!debug) {
				// keep a record of current versionnum
				long currentVersionNum = getCurrentVersion();
				try {
					for (Table t : runMap.keySet()) {						
						this.runCdtTask(t, cdtReturn, currentVersionNum, cdtExecutor);
					}
					// full refreshmap behaves like a queue.
					// the jobs completed will be removed from this map
					// the new jobs will be added by calling fullRefresh(table)
					if (!this.fullRefreshMap.isEmpty())
						for (Table t : fullRefreshMap.keySet()) {					
							this.runFullRefreshTask(t, fullRefreshReturn, currentVersionNum, fullRefreshExecutor);
						}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					LOG.fatal(Thread.currentThread().getName() + " - " + e.getMessage());
					LOG.fatal(e);
					this.stopAllThreads();
					cdtExecutor.shutdown();
					fullRefreshExecutor.shutdown();
					this.setShutdown(true);
					throw new CDTException(e.getCause());
				}
			} // if !debug

			LOG.debug("controller going to sleep for " + _SLEEPINTERVAL);
			try {
				Thread.sleep(_SLEEPINTERVAL);
			} catch (InterruptedException e) {
				LOG.fatal(e.getMessage());
				cdtExecutor.shutdown();
				throw new CDTException(e);
			}
		}

		// safe check
		this.stopAllThreads();
		cdtExecutor.shutdown();

	}

	/**
	 * this method is called by main execute loop. the main loop will iterate
	 * through the full refresh map and check the status of each job (if
	 * submitted). if a job has not been submitted, it will be also submitted
	 * here in this method. Before full refresh is executed, version -1 is
	 * stored in version store (persist) to indicate table is in full refresh
	 * mode. The current version will be stored in the fullrefreshtask callable
	 * Table cdt task will be removed from runmap and full refresh will start.
	 * After full refresh is completed, this method will put the table cdt
	 * callable task back into the runmap. Moreover, it will retrieve the
	 * version from fullrefreshcallable, update version store and set last sync
	 * version in the cdt callable. Then the cdt callable is ready to start
	 * execute in the next iteration of the main loop.
	 * 
	 * @param t
	 * @param fullRefreshReturn
	 * @param currentVersionNum
	 * @param fullRefreshExecutor
	 * @throws CDTException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private void runFullRefreshTask(Table t, Map<Table, Future<Boolean>> fullRefreshReturn, long currentVersionNum,
			ExecutorService fullRefreshExecutor) throws CDTException, InterruptedException, ExecutionException {
		if (fullRefreshReturn.get(t) == null) {
			// job has not been submitted yet, initiate the job and start run

			// setup the version num to -1 before submit the job
			this.versionStore.storeVersion(t, -1L);

			// prevent the cdt kickoff by removing it out of the run map
			this.runMap.remove(t);
			FullRefreshCallable task = fullRefreshMap.get(t);
			// store the version number before job is kicked off
			task.setLastSyncedVersion(currentVersionNum);
			// submit job
			fullRefreshReturn.put(t, fullRefreshExecutor.submit(task));
			LOG.info("FULL REFRESH TABLE TASK " + t.getFullName(false) + " is submitting to execution service");
		} else {
			if (fullRefreshReturn.get(t).isDone()) {
				if (fullRefreshReturn.get(t).get()) {
					// rebase the synced version of callable to the synced
					// version
					// before full refresh was called.
					long versionBeforeFullRefresh = this.fullRefreshMap.get(t).getLastSyncedVersion();
					CDTCallable cdt = this.callableMap.get(t);
					cdt.setLastSyncVersion(versionBeforeFullRefresh);
					this.versionStore.storeVersion(t, versionBeforeFullRefresh);
					// full refresh is done need to insert the job back into the
					// run
					// queue and remove it from
					// the full refreshmap
					this.fullRefreshMap.remove(t);
					fullRefreshReturn.remove(t);
					this.runMap.put(t, cdt);
					LOG.info("FULL REFRESH TABLE TASK " + t.getFullName(false) + " finished successfully");
				} else {
					// fullrefreseh return false.
					// silently suppress the warning exception.. such as
					// deadlock
					// resubmit the job
					FullRefreshCallable task = fullRefreshMap.get(t);
					fullRefreshReturn.put(t, fullRefreshExecutor.submit(task));
				}

			}
		}

	}

	/**
	 * Refactor this function to just execute CDT tasks for a table
	 * 
	 * @param t
	 * @param cdtReturn
	 * @param currentVersionNum
	 * @param cdtExecutor
	 * @throws CDTException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private void runCdtTask(Table t, Map<Table, Future<Boolean>> cdtReturn, long currentVersionNum,
			ExecutorService cdtExecutor) throws CDTException, InterruptedException, ExecutionException {
		CDTCallable c = runMap.get(t);

		// if future does not exists then set the version directly

		if (cdtReturn.get(t) == null) {
			c.setCurrentVersion(currentVersionNum);
			cdtReturn.put(t, cdtExecutor.submit(c));
		} else {
			if (cdtReturn.get(t).isDone()) {
				// if future is returned and not null, then the
				// task has been submitted before, use the
				// currentversion recorded from last execution from
				// task to
				// set the last known version

				if (cdtReturn.get(t).get()) {
					versionStore.storeVersion(t, c.getCurrentVersion());
					c.setLastSyncVersion(c.getCurrentVersion());
					c.setCurrentVersion(currentVersionNum);
					// submit the job that is done
					LOG.debug(String.format("Tables %s run successful, version move from %d to %d", t.getFullName(true),
							c.getLastSyncVersion(), c.getCurrentVersion()));
				} else {
					// job return with false and no exception
					// job can silently suppress a known exception such as
					// dead lock victim
					// do not switch version num and re-add it to the run
					// map.
				}

				// safety .. do not overun the queue
				if (((ThreadPoolExecutor) cdtExecutor).getQueue().size() <= this.maxThreadCount * 10)
					cdtReturn.put(t, cdtExecutor.submit(c));

			} else {
				// the job is not done.. then .. do nothing
			}
		}
	}

	/**
	 * Private method to change state
	 * 
	 * @param running
	 */
	private void setRunning(boolean running) {
		try {
			stateLock.lock();
			this.running = running;

		} finally {
			stateLock.unlock();
		}

	}

	private void stopAllThreads() {

		for (Table t : this.runMap.keySet()) {
			CDTCallable c = this.runMap.get(t);
			c.shutdownAndDeinit();
		}
		if (!this.fullRefreshMap.isEmpty())
			for (Table t : this.fullRefreshMap.keySet()) {
				FullRefreshCallable c = this.fullRefreshMap.get(t);
				c.shutdownAndDeinit();
			}
	}

	/***
	 * Retrieve the current change tracking version number .
	 * 
	 * @return
	 * @throws CDTException
	 */
	private long getCurrentVersion() throws CDTException {
		String sql = SqlUtil.getCurrentVersionSql();
		long ret = 0;
		try (Connection con = this.config.getTransDs().getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			rs.next();
			ret = rs.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new CDTException(e);
		}
		return ret;
	}

	/**
	 * Build full refresh callable for a table
	 * 
	 * @param t
	 * @return
	 */
	private FullRefreshCallable buildFullRefreshCallable(Table t) {
		// TODO: build fullrefrehscallable
		FullRefreshCallable c = new FullRefreshCallable();
		c.setDatads(config.getReadDs());
		c.setMetaHelper(metaUtil);
		c.setSender(config.getSender());
		c.setTable(t);
		c.setTopic(config.getKafkatopic());
		return c;
	}

	/**
	 * private method to build callable object.
	 * 
	 * @param t
	 * @return
	 * @throws CDTException
	 */
	private CDTCallable buildCDTCallable(Table t) throws CDTException {
		CDTCallable wk = new CDTCallable();
		wk.setPullInterval(config.getPullInterval());
		wk.setChangeds(config.getTransDs());
		wk.setDatads(config.getReadDs());
		wk.setMetaHelper(metaUtil);
		wk.setSender(config.getSender());
		wk.setTable(t);
		wk.setTopic(config.getKafkatopic());
		wk.setLastSyncVersion(versionStore.retrieveVersion(t));
		return wk;
	}

	/**
	 * Initial init method. ideally only called once during the lifecycle of
	 * controller
	 * 
	 * @throws CDTException
	 */
	private void init() throws CDTException {

		LOG.info("Master controller init() is called");
		this.maxThreadCount = config.getMaxThreadCount();
		versionStore = config.getVersionStore();
		// metautil initialized with datasource and the table filter list.
		metaUtil = new MetaUtil(config.getReadDs(), config.getTableList()); //Changed to read-only
		tableMeta = metaUtil.getSourceMeta();
		if (tableMeta == null || tableMeta.size() == 0) {
			LOG.fatal("init failed. " + (tableMeta == null ? "tableMeta is null" : "tableMeta size is zero"));
			throw new CDTException("init failed. No change tracking table metadata returned from database.");
		}

		// create a set of callables
		callableMap = new ConcurrentHashMap<Table, CDTCallable>();
		runMap = new ConcurrentHashMap<Table, CDTCallable>();
		this.fullRefreshMap = new ConcurrentHashMap<Table, FullRefreshCallable>();

		for (String tableName : tableMeta.keySet()) {
			Table t = tableMeta.get(tableName);
			CDTCallable wk = this.buildCDTCallable(t);
			// added logic here to detect the job was set as a
			// full refresh from repo
			if (versionStore.retrieveVersion(t) == -1L) {
				this.fullRefresh(t);
			} else {

				runMap.put(t, wk);
			}
			callableMap.put(t, wk);
		}
		initialized = true;
		LOG.info("Master controller init() call completed");
	}

	/**
	 * Public method to attempt putting a table by name into full refresh tasks.
	 * 
	 * @param tableName
	 * @param quoted
	 *            quoted tablename with double quote
	 * @return return error/success messages
	 * @throws CDTException
	 */
	public String fullRefresh(String tableName, boolean quoted) throws CDTException {
		if (!this.isInitialized()) {
			this.stateLock.lock();
			this.init();
			this.stateLock.unlock();
		}
		for (Table t : this.callableMap.keySet()) {
			if (t.getFullName(quoted).equals(tableName.trim().toUpperCase())) {
				if (fullRefresh(t)) {
					return "Table " + tableName + " is successfully putting into full refresh queue";
				} else {
					return "Tabble " + tableName + " is executing full refresh. cannot be added again!";
				}
			}
		}
		return "Table " + tableName + " is not in this configuration. Please verify tablename";
	}

	/**
	 * Private method to add a full refresh task. and put into execution queue
	 * 
	 * @param t
	 * @return
	 */
	private boolean fullRefresh(Table t) {
		if (this.fullRefreshMap.get(t) == null) {
			this.fullRefreshMap.put(t, this.buildFullRefreshCallable(t));
			LOG.info(String.format("Table %s is added into full refresh queue", t.getFullName(false)));
			return true;
		} else {
			LOG.warn(String.format("Tabble %s is ready in full refresh mode. cannot add!", t.getFullName(false)));
			return false;
		}
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public void setShutdown(boolean shutdown) {
		try {
			stateLock.lock();
			LOG.info("Controller set shutdown is called with flag= " + String.valueOf(shutdown));
			this.shutdown = shutdown;
		} finally {
			stateLock.unlock();
		}
	}

	public boolean isRunning() {
		synchronized (this) {
			return running;
		}
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isDebug() {
		return debug;
	}

	public long get_SLEEPINTERVAL() {
		return _SLEEPINTERVAL;
	}

	public void set_SLEEPINTERVAL(long _SLEEPINTERVAL) {
		this._SLEEPINTERVAL = _SLEEPINTERVAL;
		LOG.info("_SLEEPINTERVAL set to " + _SLEEPINTERVAL);
	}

	@Override
	public String call() throws CDTException, InterruptedException {
		this.setRunning(true);
		LOG.info("Master controller start executing.. ");
		this.execute();
		this.setRunning(false);
		LOG.info("Master controller is exiting.. ");
		return "shutdown";
	}

	public boolean isInitialized() {
		try {
			stateLock.lock();
			return initialized;
		} finally {
			stateLock.unlock();
		}

	}

	public VersionStore<Table> getVersionStore() {
		return versionStore;
	}

	public List<TableVersion> listVersionStoreStatus() {
		List<TableVersion> ret = new ArrayList<TableVersion>();
		if (this.isInitialized()) {

			for (Table t : this.callableMap.keySet()) {
				CDTCallable c = callableMap.get(t);
				ret.add(new TableVersion(t.getFullName(true), c.getLastSyncVersion()));
			}
			
		} 
		return ret;		
	}

	public Map<String, Object> getStatus() {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("isInitialized", this.isInitialized());
		ret.put("isRunning", this.isRunning());
		ret.put("isDebug", this.isDebug());
		ret.put("versions", this.listVersionStoreStatus());

		List<String> fullRefeshTableList = new ArrayList<String>();
		if (this.isInitialized()) {
			LOG.debug(String.format("full refresh map is empty? %s", fullRefreshMap.isEmpty()));
			if (!this.fullRefreshMap.isEmpty())
				for (Table t : this.fullRefreshMap.keySet()) {
					fullRefeshTableList.add(t.getFullName(true));
				}
		}
		ret.put("fullrefreshtables", fullRefeshTableList);
		return ret;
	}

	public boolean stopTable(String tableName) throws CDTException {
		boolean ret = false;
		if (!this.isInitialized()) {
			this.stateLock.lock();
			this.init();
			this.stateLock.unlock();
		}
		Set<Table> tables = this.callableMap.keySet();
		for (Table t : tables) {
			if (t.getFullName(false).equals(tableName.toUpperCase())) {
				CDTCallable callable = runMap.get(t);
				if (callable.isRunning()) {
					runMap.remove(t);
					callable.shutdownAndDeinit();
				}
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Set table version by tablename
	 * 
	 * @param tableName
	 * @param version
	 * @return
	 * @throws CDTException
	 */
	public boolean setTableVersion(String tableName, long version) throws CDTException {
		boolean ret = false;
		if (!this.isInitialized()) {
			this.stateLock.lock();
			this.init();
			this.stateLock.unlock();
		}
		Set<Table> tables = this.callableMap.keySet();
		for (Table t : tables) {
			if (t.getFullName(false).equals(tableName.toUpperCase())) {
				this.versionStore.storeVersion(t, version);
				ret = true;
			}
		}
		return ret;
	}

	public void setAllTablesVersion(long version) throws CDTException {
		if (!this.isInitialized()) {
			this.stateLock.lock();
			this.init();
			this.stateLock.unlock();
		}
		Set<Table> tables = this.callableMap.keySet();
		for (Table t : tables) {
			this.versionStore.storeVersion(t, version);
		}
	}

	public String startController() {
		if (this.isRunning()) {
			return "cdtcontroller is running, cannot be started";
		} else {
			this.setShutdown(false);
			SimpleThreadFactory factory = new SimpleThreadFactory(this.config.getKafkatopic() + ".cdtcontroller");
			singleThreadExecutor = Executors.newSingleThreadExecutor(factory);
			singleThreadExecutor.submit(this);
			return "cdtcontroller started";
		}
	}

	public String stopController() {
		if (!this.isRunning()) {
			return "cdtcontroller is not running, cannot be stopped";
		} else {
			this.setShutdown(true);
			this.stopAllThreads();
			try {

				this.singleThreadExecutor.shutdown();
				this.singleThreadExecutor.awaitTermination(_SLEEPINTERVAL * 5, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// ignore exception
			}
			this.running = false;
			return "cdtcontroller is shutdown!";
		}
	}
	
	public String cdtSleepInterval(long interval)
	{
		if (interval >= 500 && interval <= 60000)
		{
			this.set_SLEEPINTERVAL(interval);
			LOG.info("sleep interval set to " + interval);
			return "sleep interval set to " + interval;
		}
		else
			return "interval out of range, sleep interval was not changed.";
	}
}
