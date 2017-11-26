package com.elevate.edw.sqlservercdc;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import com.elevate.edw.sqlservercdc.util.DataTypeUtil;
import com.elevate.edw.sqlservercdc.util.MetaUtil;
import com.elevate.edw.sqlservercdc.util.SqlUtil;

public class CDTCallable implements Callable<Boolean> {
	public static final Logger LOG = LogManager.getLogger(CDTCallable.class);
	public static int _BATCHSIZE = 100;
	private long lastSyncVersion;
	private long currentVersion;
	private Table table;
	private DataSource changeds;
	private DataSource datads;
	private MetaUtil metaHelper;
	private int pullInterval;
	private int currentPull;
	private Sender sender;
	private String topic;
	
	private boolean terminate = false;
	private volatile boolean running = false;

	@Override
	public Boolean call() throws CDTException, InterruptedException, ExecutionException, TimeoutException  {
		this.running = true;
		this.terminate = false;
		Thread.currentThread().setName(topic  + "."+this.table.getFullName(false)+".cdtCallable");
		LOG.debug(Thread.currentThread().getName()+ " - "+String.format("Start running cdc for table %s at last sync position: %d ", table.getFullName(true),
				this.lastSyncVersion));
		// always check whether meta changes on the source table.
		// if so, exit
		if (!validateMetaData()) {
			LOG.fatal(Thread.currentThread().getName()+" - "+"Table definition is not matching: " + table.getFullName(true));
			this.running=false;;
			throw new CDTException("Table definition is not matching: " + table.getFullName(true));
		}

		String getChgPkSql = SqlUtil.buildChangeSQL(table, lastSyncVersion);
		String buildTemTblSql = SqlUtil.buildTempTableDDL(table);
		String insertPkSql = SqlUtil.buildInsSql(table);
		LOG.trace(String.format("getChgPkSql= %s ", getChgPkSql));
		LOG.trace(String.format("buildTemTblSql= %s ", buildTemTblSql));
		LOG.trace(String.format("insertPkSql= %s ", insertPkSql));

		DataSet ret = null;
		// cache the changed data and primary key into memory
		// create temp table on read data node and popualte

		try (Connection concdd = datads.getConnection();
				Statement createTempTableSt = concdd.createStatement();
				Connection conpk = changeds.getConnection();
				Statement stpk = conpk.createStatement();
				ResultSet pkrs = stpk.executeQuery(getChgPkSql);) {
			// create temp table on the target platform
			createTempTableSt.executeUpdate(buildTemTblSql);
			// prepare the insert batch statement
			PreparedStatement insSt = concdd.prepareStatement(insertPkSql);
			long rowCnt = 0L;
			// retrieve rows from change table pk and insert it into temp table
			while (pkrs.next()) {
				if(this.terminate)
					break;
				int idx = 1;
				for (Column c : table.getPrimaryKey().getColumns()) {
					String cname = c.getColumnName();

					int sqlType = DataTypeUtil.mapDataTypeToSqlType(c.getDatatype());
					Object o = pkrs.getObject(cname);
					if (pkrs.wasNull())
						insSt.setNull(idx, sqlType);
					else
						insSt.setObject(idx, o, sqlType, c.getPrecision());
					idx++;
				}

				insSt.setString(idx, pkrs.getString("SYS_CHANGE_OPERATION"));

				insSt.addBatch();
				if (++rowCnt % _BATCHSIZE == 0)
					insSt.executeBatch();
			}
			insSt.executeBatch();

			String selectSql = SqlUtil.buildDataSelectSql(table);
			LOG.trace(String.format("selectSQL = %s", selectSql));

			//reset counter
			rowCnt = 0;
			
			int senderPackFactor = table.isContainsLargeBinary() ? 1 : _BATCHSIZE;
			if (table.isContainsLargeBinary()) {
				LOG.debug(Thread.currentThread().getName()+ " - "+String.format("%s contains lob will try send 1 record at a time", table.getFullName(true)));
			}
			
			Map<Integer, DataSet> dataSetMap = new TreeMap<Integer, DataSet>();
			int modFactor = 9;
			rowCnt = 0L;
			
			try (Statement selSt = concdd.createStatement(); ResultSet rs = selSt.executeQuery(selectSql)) {
				
				while (rs.next()) {
					if(this.terminate)
						break;
					Map<String, String> row = new TreeMap<String, String>();
					String pk = "";
					for (Column c : table.getColumns()) {
						row.put(c.getColumnName(), rs.getString(c.getColumnName()));
						if (c.isPk()) {
							pk = pk + rs.getString(c.getColumnName());
						}
					}
					row.put("SYS_CHANGE_OPERATION", rs.getString("SYS_CHANGE_OPERATION"));
					int hashmod = pk.hashCode() % modFactor;
					if (dataSetMap.get(hashmod) == null) {
						dataSetMap.put(hashmod, this.createDataSet());
					}
					DataSet ds = dataSetMap.get(hashmod);
					ds.addRow(row);
					rowCnt++;
					if (ds.getRows().size() % senderPackFactor == 0) {
						//generate a hash. try to keep one tables transaction 
						// stays in a single partition to avoid out of order 
						// record sequence
						LOG.debug(Thread.currentThread().getName()+ " - "+String.format("send data to kafka TABLE =%s ", this.table.getFullName(false)));
						sender.sendMessage(topic, hashmod, ds);
						if(sender.getException()!= null){
							this.shutdownAndDeinit();
							throw new CDTException(sender.getException());
						}
						dataSetMap.remove(hashmod);
					}
				}
				
				// clear the dataset map
				for (Integer hashmod : dataSetMap.keySet()) {
					LOG.debug(Thread.currentThread().getName()+ " - "+String.format("send data to kafka TABLE = %s", this.table.getFullName(false)));
					sender.sendMessage(topic, hashmod, dataSetMap.get(hashmod));
					if (sender.getException() != null) {
						this.shutdownAndDeinit();
						throw new CDTException(sender.getException());
					}

				}
				dataSetMap.clear();				
			}
			LOG.debug(Thread.currentThread().getName()+ " - "+String.format(
					"End running cdc for table %s at last sync position: %d, records count for this run %d",
					table.getFullName(true), this.lastSyncVersion, rowCnt));
		} catch (SQLException ex) {
			//deadlock exception silently ignore
			if(ex.getErrorCode()==1205||("4001").equals(ex.getSQLState())) {
				LOG.warn(Thread.currentThread().getName()+" - "+ex.getMessage());
				LOG.warn(Thread.currentThread().getName()+" - "+"This error is ignored and job is returning");
				this.running = false;
				return Boolean.FALSE;
			}
			LOG.fatal(Thread.currentThread().getName()+" - "+ex.getMessage());
			this.running=false;;
			throw new CDTException(ex.getCause());
		}
		sender.forceFlush();
		if(sender.getException()!= null){
			this.running = false;
			this.shutdownAndDeinit();
			throw new CDTException(sender.getException());
		}
		this.running = false;
		return Boolean.TRUE;
	}
	
	
	/**
	 * Create data set instance
	 * @return
	 * @throws CDTException
	 */
	private DataSet createDataSet() throws CDTException {
		DataSet ret = new DataSet();
		ret.setSchemaName(table.getSchemaname());
		ret.setTableName(table.getTablename());
		ret.setRowMeta(metaHelper.getSourceMetaByName(table.getFullName(false)));
		return ret;
	}
	

	private boolean validateMetaData() throws CDTException {
		if (currentPull == pullInterval)
		{
			Table t = metaHelper.getTableMetaFromSource(table.getSchemaname(), table.getTablename());
			if (!t.equals(table)) {
				return false;
			}
			currentPull = 1;
		}
		else
			currentPull += 1;
		return true;
	}

	public long getLastSyncVersion() {
		return lastSyncVersion;
	}

	public void setLastSyncVersion(long lastSyncVersion) {
		this.lastSyncVersion = lastSyncVersion;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
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

	public DataSource getChangeds() {
		return changeds;
	}

	public void setChangeds(DataSource changeds) {
		this.changeds = changeds;
	}

	public DataSource getDatads() {
		return datads;
	}

	public void setDatads(DataSource datads) {
		this.datads = datads;
	}

	public long getCurrentVersion() {
		return this.currentVersion;
	}

	public void setCurrentVersion(long currentVersion) {
		this.currentVersion = currentVersion;
	}

	/**
	 * Force shutdown the callable thread.
	 */
	public void shutdownAndDeinit() {
		LOG.info(Thread.currentThread().getName() + " - "
				+String.format("TABLE TASK : %s terminate is called", this.table.getFullName(true)));
		this.terminate = true;
		this.sender.close();		
		
	}
	
	public boolean isRunning(){
		return this.running;
	}


	public void setPullInterval(int pullInterval) {
		currentPull = pullInterval;
		this.pullInterval = pullInterval;
	}
}
