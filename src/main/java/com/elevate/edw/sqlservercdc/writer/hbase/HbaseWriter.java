package com.elevate.edw.sqlservercdc.writer.hbase;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elevate.edw.jackson.model.DataSet;
import com.elevate.edw.sqlservercdc.metamodel.Column;
import com.elevate.edw.sqlservercdc.metamodel.Table;
import com.elevate.edw.sqlservercdc.writer.DataSetWriter;
import com.elevate.edw.sqlservercdc.writer.WriteConfigurationException;
import com.elevate.edw.sqlservercdc.writer.hbase.util.HbaseLoginService;

public class HbaseWriter implements DataSetWriter {
	private static final Logger LOG = LogManager.getLogger(HbaseWriter.class);
	org.apache.hadoop.hbase.client.Connection hbasecon;
	private Map<String, org.apache.hadoop.hbase.client.Table> htableMap;
	private String columnFamily;
	private int _BATCHSIZE = 100;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
	private Map<String, String> conf;

	private HbaseLoginService loginService = null;
	private MessageDigest digest ;

	public void writeDataSet(DataSet dataSet) throws IOException, InterruptedException {
		Table meta = dataSet.getRowMeta();

		if (meta == null || meta.getPrimaryKey() == null)
			throw new IOException("meta for data set is null or pk column def is null");
		String tname = meta.getFullName(false);
		UserGroupInformation.getLoginUser().checkTGTAndReloginFromKeytab();
		org.apache.hadoop.hbase.client.Table htable = htableMap.get(tname);
		if (htable == null) {
			throw new IOException(String.format("table %s cannot be found!", tname));
		}
		Set<Column> pk = meta.getPrimaryKey().getColumns();
		List<Column> columns = meta.getColumns();
		List<Map<String, String>> rows = dataSet.getRows();
		List<Row> batch = new ArrayList<Row>();
		int rowCnt = 0;
		String timestamp = format.format(new java.util.Date());
		for (Map<String, String> row : rows) {

			// String pkVal = meta.getFullName(false) + ":";
			String pkVal = "";
			for (Column c : pk) {
				pkVal = pkVal + ":" + row.get(c.getColumnName());
			}
			Row action = null;

			// if (row.get("SYS_CHANGE_OPERATION").equals("D")) {
			// action = new Delete(Bytes.toBytes(pkVal));
			// ((Delete) action).addFamily(Bytes.toBytes(columnFamily));
			// } else {
			// action = new Put(Bytes.toBytes(pkVal));
			// for (Column c : columns) {
			// String v = row.get(c.getColumnName(false));
			// v = (v == null) ? "NULL" : v;
			// if (action instanceof Put)
			// ((Put) action).addColumn(Bytes.toBytes(columnFamily),
			// Bytes.toBytes(c.getColumnName(false)),
			// Bytes.toBytes(v));
			// }
			// }
			StringBuilder sb = new StringBuilder(pkVal).reverse();
			// use sha 256 to encode the pk .
			byte[] hash = digest.digest(sb.toString().getBytes("UTF-8"));
			//action = new Put(Bytes.toBytes(sb.toString()));
			action = new Put(bytesToHex(hash));
			((Put) action).addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("SYS_CHANGE_OPERATION"),
					Bytes.toBytes(row.get("SYS_CHANGE_OPERATION")));
			// TODO -- timestamp and sys change operation.
			// rewrite logic
			((Put) action).addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("HBASE_UPD_TS"),
					Bytes.toBytes(timestamp));

			for (Column c : columns) {
				String v = row.get(c.getColumnName(false));
				v = (v == null) ? "NULL" : v;
				if (action instanceof Put)
					((Put) action).addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(c.getColumnName(false)),
							Bytes.toBytes(v));
			}
			batch.add(action);
			if (++rowCnt % _BATCHSIZE == 0) {
				Object[] batchret = new Object[batch.size()];
				htable.batch(batch, batchret);
				batch.clear();
			}
		}

		if (rowCnt % _BATCHSIZE != 0) {
			Object[] batchret = new Object[batch.size()];
			htable.batch(batch, batchret);
			batch.clear();
		}
	}

	public Map<String, org.apache.hadoop.hbase.client.Table> getHtableMap() {
		return htableMap;
	}

	public void setHtableMap(Map<String, org.apache.hadoop.hbase.client.Table> htableMap) {
		this.htableMap = htableMap;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}

	public int get_BATCHSIZE() {
		return _BATCHSIZE;
	}

	public void set_BATCHSIZE(int _BATCHSIZE) {
		this._BATCHSIZE = _BATCHSIZE;
	}

	/**
	 * init method to setup proper resources assocaited with the writer.
	 * 
	 * @throws WriteConfigurationException
	 */
	@Override
	public void init(Map<String, String> conf) throws WriteConfigurationException {
		this.conf = conf;
		for (String key : conf.keySet())
			LOG.debug(String.format("HbaseWriter Config key = %s , value = %s", key, conf.get(key)));

		String hadoopConfDir = conf.get("writer.hbase.hadoopconfigbasedir");
		org.apache.hadoop.conf.Configuration hbaseConfig = new org.apache.hadoop.conf.Configuration();
		hbaseConfig.addResource(new Path(hadoopConfDir + File.separator + "core-site.xml"));
		hbaseConfig.addResource(new Path(hadoopConfDir + File.separator + "hdfs-site.xml"));
		hbaseConfig.addResource(new Path(hadoopConfDir + File.separator + "hbase-site.xml"));
		try {
			// force login
			this.loginService = HbaseLoginService.getInstance(hbaseConfig, conf.get("writer.hbase.login.principal"),
					conf.get("writer.hbase.login.keytab"));
			hbasecon = ConnectionFactory.createConnection(hbaseConfig);
			String namespace = conf.get("writer.hbase.namespace");
			TableName[] tablenames = hbasecon.getAdmin().listTableNamesByNamespace(namespace);

			// construct the lookup table between source tablename and the
			// target hbase table
			// object
			this.htableMap = new HashMap<String, org.apache.hadoop.hbase.client.Table>();
			for (TableName tn : tablenames) {
				org.apache.hadoop.hbase.client.Table t = hbasecon.getTable(tn);
				String tname = tn.getQualifierAsString();
				this.htableMap.put(tname, t);
			}
			this.columnFamily = conf.get("writer.hbase.columnfamily");
		} catch (IOException e) {
			throw new WriteConfigurationException(e);
		}
		
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			LOG.fatal(e.getMessage());
			throw new WriteConfigurationException(e);
		};

	}

	
	public static byte[] bytesToHex(byte[] bytes) {
	    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	    byte[] hexChars = new byte[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = (byte) hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = (byte) hexArray[v & 0x0F];
	    }
	    return hexChars;
	}
}
