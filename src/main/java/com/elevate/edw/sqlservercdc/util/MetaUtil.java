package com.elevate.edw.sqlservercdc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elevate.edw.sqlservercdc.CDTException;
import com.elevate.edw.sqlservercdc.metamodel.Column;
import com.elevate.edw.sqlservercdc.metamodel.DataType;
import com.elevate.edw.sqlservercdc.metamodel.Table;

/***
 * MetaUtil is a class created to support metadata build/lookup operations.
 * Original design is a pure utility class without holding references to the
 * metadata elements. The latest effort on this code on April 11 2017 modified
 * the class to hold two metadata references. original source metadata and
 * amended metadata based on source with CDC specific columns. The main
 * consideration of this change is to make it threadsafe while also reduce the
 * overhead of going back to database. The getTableMetaFromSource function is
 * still exposed as public as a way to force re-gen a Table entity for
 * validateion purposes.
 * 
 * @author ywu
 *
 */
public class MetaUtil {
	private DataSource ds;
	public static final Logger LOG = LogManager.getLogger(MetaUtil.class);

	private Map<String, Table> sourceMeta = null;
	private Map<String, Table> sourceMetaWithCdc = null;
	// make it threadsafe
	private Lock stateLock = new ReentrantLock();
	private boolean initialized = false;

	private List<String> tableNameFilter;

	public MetaUtil(DataSource ds, List<String> tableNameFilter) {
		this(ds);
		this.tableNameFilter = tableNameFilter;
	}

	public MetaUtil(DataSource ds) {
		this.ds = ds;
	}

	public Table getTableMetaFromSource(String schemaName, String tableName) throws CDTException {
		Table ret = null;
		try (Connection ds1 = ds.getConnection();
				PreparedStatement tableSt = ds1.prepareStatement(SqlUtil.getTableMetaSql())) {
			LOG.trace("======================================");
			LOG.trace("Table : " + schemaName + "." + tableName);
			tableSt.setString(1, schemaName);
			tableSt.setString(2, tableName);
			ResultSet rs2 = tableSt.executeQuery();

			if (!rs2.next()) {
				throw new CDTException("Table " + schemaName + "." + tableName + " cannot be found!");
			} else {
				ret = new Table(schemaName, tableName);
				do {
					Column c = new Column(rs2.getString("COLUMN_NAME"));
					boolean isPk = "Y".equals(rs2.getString("PK_FLG")) ? true : false;
					boolean isNullable = "YES".equals(rs2.getString("IS_NULLABLE")) ? true : false;
					String dataTypeStr = rs2.getString("DATA_TYPE");
					Integer charlen = rs2.getInt("CHARACTER_MAXIMUM_LENGTH");
					Integer numPrecision = rs2.getInt("NUMERIC_PRECISION");
					Integer numScale = rs2.getInt("NUMERIC_SCALE");
					Integer dtPrecision = rs2.getInt("DATETIME_PRECISION");

					c.populateAttributes(isPk, isNullable, dataTypeStr, charlen, numPrecision, numScale, dtPrecision);
					ret.addColumn(c);
					if (isPk)
						ret.addColumnToPk(c);
					LOG.trace("COLUMN attributes:");
					LOG.trace("\tName = " + c.getColumnName());
					LOG.trace("\tisPk = " + c.isPk());
					LOG.trace("\tisNullable = " + c.isNullable());
					LOG.trace("\tDataType = " + c.getDatatype());
					LOG.trace("\tPrecision = " + c.getPrecision());
					LOG.trace("\tScale = " + c.getScale());
				} while (rs2.next());
			}

		} catch (SQLException e) {
			throw new CDTException(e.getCause());
		}
		return ret;
	}

	public Map<String, Table> getSourceMetaWithCdc() throws CDTException {
		if (!initialized) {
			stateLock.lock();
			init();
			stateLock.unlock();
		}
		return this.sourceMetaWithCdc;
	}

	public Table getSourceMetaWithCdcByName(String name) throws CDTException {
		if (!initialized) {
			stateLock.lock();
			init();
			stateLock.unlock();
		}
		return this.sourceMetaWithCdc.get(name);

	}

	public Map<String, Table> getSourceMeta() throws CDTException {
		if (!initialized) {
			stateLock.lock();
			init();
			stateLock.unlock();
		}
		return this.sourceMeta;
	}

	public Table getSourceMetaByName(String name) throws CDTException {
		if (!initialized) {
			stateLock.lock();
			init();
			stateLock.unlock();
		}
		return this.sourceMeta.get(name);
	}

	private void init() throws CDTException {
		if (initialized)
			return;
		sourceMeta = new TreeMap<String, Table>();
		sourceMetaWithCdc = new TreeMap<String, Table>();
		LOG.debug("Build metadata for the cdc tables");
		try (Connection ds1 = ds.getConnection(); Statement st = ds1.createStatement();) {
			ResultSet rs1 = st.executeQuery(SqlUtil.listCdcTables);
			Set<String> enabledCdcTableNames = new HashSet<String>();
			while (rs1.next()) {
				String schemaName = rs1.getString(1);
				String tableName = rs1.getString(2);
				enabledCdcTableNames.add(schemaName + "." + tableName);
				// if the tablename filter does not contain the cdc table skip.
				// otherwise proceed.
				if (this.tableNameFilter != null && tableNameFilter.size() > 0
						&& !tableNameFilter.contains(schemaName + "." + tableName))
					continue;
				else {

					LOG.trace("======================================");
					LOG.trace("Table : " + schemaName + "." + tableName);
					Table t = this.getTableMetaFromSource(schemaName, tableName);
					String key = schemaName + "." + tableName;
					sourceMeta.put(key, t);
					sourceMetaWithCdc.put(key, this.amendMetaWithCdc(t));
				}
			}
			// added a step to throw warning message if table filter specify a
			// table which does not have CDT turn on
			if (tableNameFilter != null && tableNameFilter.size() > 0)
				for (String n : tableNameFilter) {
					if (!enabledCdcTableNames.contains(n)) {
						LOG.warn("CDT Table List contains " + n
								+ ", However this table does not have change tracking enabled");
					}
				}
			this.initialized = true;
		} catch (SQLException e) {
			throw new CDTException(e.getCause());
		} catch (CloneNotSupportedException e) {
			throw new CDTException(e.getCause());
		}
	}

	/**
	 * Create new instance of metadata to accommodate additional CDC columns
	 * 
	 * @param table
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Table amendMetaWithCdc(Table table) throws CloneNotSupportedException {
		Table ret = Table.copyTable(table);
		// TODO: Create CDC columns
		Column changeOp = new Column("SYS_CHANGE_OPERATION");
		changeOp.setDatatype(DataType.CHAR);
		changeOp.setNullable(false);
		changeOp.setPk(false);
		changeOp.setPrecision(20);
		changeOp.setScale(0);
		ret.addColumn(changeOp);
		return ret;
	}
}
