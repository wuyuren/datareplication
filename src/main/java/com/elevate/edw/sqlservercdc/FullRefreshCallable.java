package com.elevate.edw.sqlservercdc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elevate.edw.jackson.model.DataSet;
import com.elevate.edw.sqlservercdc.kafka.Sender;
import com.elevate.edw.sqlservercdc.metamodel.Column;
import com.elevate.edw.sqlservercdc.metamodel.Table;
import com.elevate.edw.sqlservercdc.util.MetaUtil;
import com.elevate.edw.sqlservercdc.util.SqlUtil;

public class FullRefreshCallable implements Callable<Boolean> {
	public static final Logger LOG = LogManager.getLogger(FullRefreshCallable.class);
	public static int _BATCHSIZE = 100;
	private Table table;
	private DataSource datads;
	private MetaUtil metaHelper;
	private Sender sender;
	private String topic;
	private long lastSyncedVersion;

	private volatile boolean running = false;
	private boolean terminate = false;

	@Override
	public Boolean call() throws CDTException, InterruptedException, ExecutionException, TimeoutException {
		this.running = true;
		this.terminate = false;
		Thread.currentThread().setName(topic + "." + table.getFullName(false) + ".fullrefreshcallable");
		LOG.info(Thread.currentThread().getName() + " - "
				+ String.format("Table %s full refresh task started running!", table.getFullName(true)));
		// always check whether meta changes on the source table.
		// if so, exit
		if (!validateMetaData()) {
			LOG.fatal(Thread.currentThread().getName() + " - " + "Table definition is not matching: "
					+ table.getFullName(true));
			this.running = false;
			;
			throw new CDTException("Table definition is not matching: " + table.getFullName(true));
		}
		String fullRefreshSql = SqlUtil.buildFullSelectSql(table);
		LOG.trace(Thread.currentThread().getName() + " - " + String.format("fullRefreshSql = %s ", fullRefreshSql));
		// create a collection to store multiple datasets
		// each dataset will contain only the values whose key hash
		// equals the integer.
		Map<Integer, DataSet> dataSetMap = new TreeMap<Integer, DataSet>();
		int modFactor = 10;
		long rowCnt = 0L;
		try (Connection readcon = datads.getConnection();
				Statement readSt = readcon.createStatement();
				ResultSet readrs = readSt.executeQuery(fullRefreshSql)) {

			int senderPackFactor = table.isContainsLargeBinary() ? 10 : _BATCHSIZE;
			if (table.isContainsLargeBinary()) {
				LOG.debug(Thread.currentThread().getName() + " - "
						+ String.format("%s contains lob will try send 1 record at a time", table.getFullName(true)));
			}

			while (readrs.next()) {
				if (this.terminate)
					break;
				Map<String, String> row = new TreeMap<String, String>();
				String pk = "";
				for (Column c : table.getColumns()) {
					row.put(c.getColumnName(), readrs.getString(c.getColumnName()));
					if (c.isPk()) {
						pk = pk + readrs.getString(c.getColumnName());
					}
				}
				row.put("SYS_CHANGE_OPERATION", readrs.getString("SYS_CHANGE_OPERATION"));
				int hashmod = pk.hashCode() % modFactor;
				if (dataSetMap.get(hashmod) == null) {
					dataSetMap.put(hashmod, this.createDataSet());
				}
				DataSet ds = dataSetMap.get(hashmod);
				ds.addRow(row);
				rowCnt++;
				if (ds.getRows().size() % senderPackFactor == 0) {
					// generate a hash. try to keep one tables transaction
					// stays in a single partition to avoid out of order
					// record sequence
					LOG.debug(Thread.currentThread().getName() + " - "
							+ String.format("send data to kafka TABLE = %s", this.table.getFullName(false)));
					sender.sendMessage(topic, hashmod, ds);
					if (sender.getException() != null) {
						this.shutdownAndDeinit();
						throw new CDTException(sender.getException());
					}
					dataSetMap.remove(hashmod);
				}
			}

		} catch (SQLException ex) {
			
			if(ex.getErrorCode()==1205||ex.getSQLState().equals("4001")) {
				//deadlock exception silently ignore
				LOG.warn(Thread.currentThread().getName()+" - "+ex.getMessage());
				LOG.warn(Thread.currentThread().getName()+" - "+"This error is ignored and job is returning");
				this.running = false;
				return Boolean.FALSE;
			}
			LOG.fatal(Thread.currentThread().getName() + " - " + ex.getMessage());
			this.running = false;
			;
			throw new CDTException(ex.getCause());
		}

		// clear the dataset map
		for (Integer hashmod : dataSetMap.keySet()) {
			LOG.debug(Thread.currentThread().getName() + " - "
					+ String.format("send data to kafka TABLE = %s", this.table.getFullName(false)));
			sender.sendMessage(topic, hashmod, dataSetMap.get(hashmod));
			if (sender.getException() != null) {
				this.shutdownAndDeinit();
				throw new CDTException(sender.getException());
			}

		}

		dataSetMap.clear();
		sender.forceFlush();
		if (sender.getException() != null) {
			this.running = false;
			this.shutdownAndDeinit();
			throw new CDTException(sender.getException());
		}
		this.running = false;
		LOG.info(Thread.currentThread().getName() + " - "
				+ String.format("Table %s full refresh completed, Row count = %d, before refresh version= %d ",
						table.getFullName(true), rowCnt, this.lastSyncedVersion));
		return true;
	}

	private DataSet createDataSet() throws CDTException {
		DataSet ret = new DataSet();

		ret.setSchemaName(table.getSchemaname());
		ret.setTableName(table.getTablename());
		ret.setRowMeta(metaHelper.getSourceMetaByName(table.getFullName(false)));
		return ret;

	}

	public void shutdownAndDeinit() {
		LOG.info(Thread.currentThread().getName() + " - "
				+ String.format("TABLE TASK : %s terminate is called", this.table.getFullName(true)));
		this.terminate = true;
		this.sender.close();
	}

	private boolean validateMetaData() throws CDTException {
		Table t = metaHelper.getTableMetaFromSource(table.getSchemaname(), table.getTablename());
		if (!t.equals(table)) {
			return false;
		}
		return true;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public DataSource getDatads() {
		return datads;
	}

	public void setDatads(DataSource datads) {
		this.datads = datads;
	}

	public MetaUtil getMetaHelper() {
		return metaHelper;
	}

	public void setMetaHelper(MetaUtil metaHelper) {
		this.metaHelper = metaHelper;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public long getLastSyncedVersion() {
		return lastSyncedVersion;
	}

	public void setLastSyncedVersion(long lastSyncedVersion) {
		this.lastSyncedVersion = lastSyncedVersion;
	}

}
