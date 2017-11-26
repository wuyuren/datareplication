package com.elevate.edw.sqlservercdc.test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import com.elevate.edw.jackson.model.DataSet;
import com.elevate.edw.sqlservercdc.CDTConfiguration;
import com.elevate.edw.sqlservercdc.CDTController;
import com.elevate.edw.sqlservercdc.CDTException;
import com.elevate.edw.sqlservercdc.VersionStore;
import com.elevate.edw.sqlservercdc.kafka.Bookmark;
import com.elevate.edw.sqlservercdc.kafka.BookmarkException;
import com.elevate.edw.sqlservercdc.kafka.DatabaseBookmarkStore;
import com.elevate.edw.sqlservercdc.metamodel.Column;
import com.elevate.edw.sqlservercdc.metamodel.DataType;
import com.elevate.edw.sqlservercdc.metamodel.Table;
import com.elevate.edw.sqlservercdc.metamodel.TableConstraint;
import com.elevate.edw.sqlservercdc.metamodel.TableConstraint.ConstraintType;
import com.elevate.edw.sqlservercdc.util.MetaUtil;
import com.elevate.edw.sqlservercdc.writer.hbase.HbaseWriter;


public class CDCConTest extends AbstractTest implements BeanPostProcessor {

	


	// @Test
	// public void assertCon(){
	// SqlServerCDCCon cdcCon = new SqlServerCDCCon();
	// cdcCon.setDatabaseName("PDO_Live");
	// cdcCon.setPortNum("1433");
	// cdcCon.setPassword("6G4MpUmvRdeN7831ou3i");
	// cdcCon.setEncryption("N");
	// cdcCon.setAuthMode("D");
	// cdcCon.setReadServerName("PRDBISEDW04.exclaim-prd.com");
	// cdcCon.setSourceSysName("PDO");
	// cdcCon.setTransServerName("PRDBISEDW04.exclaim-prd.com");
	// cdcCon.setUserName("InformaticaSQLUser_R");
	// try {
	// DataSource ds = cdcCon.getReadCon();
	// Connection con = ds.getConnection();
	//
	// } catch (ParameterCheckException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	//
	// }

	@Test
	public void databaseBookmarkStoreTest() {
		DatabaseBookmarkStore bs = (DatabaseBookmarkStore) ctx.getBean("elasticBookmarkStore");
		Bookmark b = new Bookmark(new TopicPartition("elasticnls", 0), new OffsetAndMetadata(200, "test2"));
		try {
			bs.storeBookmark(b);
			Bookmark b1 = bs.retrieveBookmark("elasticnls", 0);
			assert b1.equals(b);
		} catch (BookmarkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void metaUtilTest() {
		MetaUtil metaUtil = (MetaUtil) ctx.getBean("metaUtil");
		try {
			Map<String, Table> tableMeta = metaUtil.getSourceMeta();
			CDTConfiguration elastic = (CDTConfiguration) ctx.getBean("elasticCdcConfig");
			VersionStore<Table> vs = elastic.getVersionStore();
			for (String key : tableMeta.keySet()) {
				Table t = tableMeta.get(key);
				Long version = vs.retrieveVersion(t);
				vs.storeVersion(t, version + 100);
				Long version2 = vs.retrieveVersion(t);
				System.out.print(
						String.format("Table Name = %s \t Version = %d\t Version2 = %d%n", key, version, version2));
			}

		} catch (CDTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void sqlCllable() {
		CDTConfiguration elastic = (CDTConfiguration) ctx.getBean("elasticCdcConfig");
		try (Connection con = elastic.getReadDs().getConnection(); Statement st = con.createStatement();) {
			st.executeUpdate(
					"IF OBJECT_ID(N'tempdb..#T') IS NOT NULL BEGIN DROP TABLE #T END; CREATE TABLE #T (ID INT)");
			ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM #T");
			rs.next();
			System.out.println(rs.getString(1));
			rs.close();
			st.executeUpdate(
					"IF OBJECT_ID(N'tempdb..#T') IS NOT NULL BEGIN DROP TABLE #T END; CREATE TABLE #T (ID INT)");
			st.executeUpdate("INSERT INTO #T values (1)");
			rs = st.executeQuery("SELECT COUNT(*) FROM #T");
			rs.next();
			System.out.println(rs.getString(1));
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCtxLoad() {
		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}

	}

	@Test
	public void testMasterController() {
		CDTController controller = ctx.getBean(CDTController.class);
		controller.setDebug(false);

		Future f = Executors.newSingleThreadExecutor().submit(controller);
		try {
			Thread.sleep(controller.get_SLEEPINTERVAL() * 5);
			controller.setShutdown(true);
			f.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	@Test
	public void testHbaseWriter() {
		for (int j = 1; j <= 10; j++) {
			Table rowMeta = new Table();
			TableConstraint pk = new TableConstraint();
			pk.setType(ConstraintType.PK);
			TreeMap<String, String> rowData = new TreeMap<String, String>();
			Column c = new Column();
			c.setColumnName("ID");
			c.setDatatype(DataType.INT);
			c.setPk(true);
			rowMeta.addColumn(c);
			pk.addColumn(c);

			c = new Column();
			c.setColumnName("DOB");
			c.setDatatype(DataType.VARCHAR);
			c.setPrecision(10);
			rowMeta.addColumn(c);

			c = new Column();
			c.setColumnName("VOL");
			c.setDatatype(DataType.DECIMAL);
			c.setPrecision(10);
			c.setScale(2);
			rowMeta.addColumn(c);

			rowMeta.setPrimaryKey(pk);
			rowMeta.setSchemaname("unittestschema");
			rowMeta.setTableName("unittesttable");

			DataSet dataSet = new DataSet();

			dataSet.setRowMeta(rowMeta);

			for (int i = 1; i < 1000; i++) {
				rowData = new TreeMap<String, String>();
				rowData.put("ID", String.valueOf(i));
				rowData.put("DOB", "2010-02-01");
				rowData.put("VOL", String.valueOf(i * 1.22));
				dataSet.addRow(rowData);

				dataSet.setSchemaName("TestSchema");
				dataSet.setTableName("TestTable");
			}
			String hadoopConfDir = "C:/Users/ywu/workspace/sqlservercdc/config/";
			org.apache.hadoop.conf.Configuration hbaseConfig = new org.apache.hadoop.conf.Configuration();
			hbaseConfig.addResource(new Path(hadoopConfDir + File.separator + "core-site.xml"));
			hbaseConfig.addResource(new Path(hadoopConfDir + File.separator + "hdfs-site.xml"));
			hbaseConfig.addResource(new Path(hadoopConfDir + File.separator + "hbase-site.xml"));
			UserGroupInformation.setConfiguration(hbaseConfig);
			try {
				UserGroupInformation ugi = UserGroupInformation.getLoginUser();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try (org.apache.hadoop.hbase.client.Connection hbasecon = ConnectionFactory.createConnection(hbaseConfig);
					org.apache.hadoop.hbase.client.Table table = hbasecon.getTable(TableName.valueOf("cdctest1"))) {

				HbaseWriter writer = new HbaseWriter();

				// writer.setHtable(table);
				// TODO writer accepts map of string,htable
				writer.setColumnFamily("row_data");
				writer.writeDataSet(dataSet);

				table.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testHBaseConnection() throws IOException {
		org.apache.hadoop.conf.Configuration config = new org.apache.hadoop.conf.Configuration();
		String hadoopconfigpath = ((ConfigurableBeanFactory) ctx.getAutowireCapableBeanFactory())
				.resolveEmbeddedValue("${elasticnls.hbase.config.basedir}");
		config.addResource(new Path(hadoopconfigpath + File.separator + "core-site.xml"));
		config.addResource(new Path(hadoopconfigpath + File.separator + "hdfs-site.xml"));
		config.addResource(new Path(hadoopconfigpath + File.separator + "hbase-site.xml"));

		UserGroupInformation.setConfiguration(config);
		String principal = ((ConfigurableBeanFactory) ctx.getAutowireCapableBeanFactory())
				.resolveEmbeddedValue("${elasticnls.hbase.config.login.principal}");
		String keytab = ((ConfigurableBeanFactory) ctx.getAutowireCapableBeanFactory())
				.resolveEmbeddedValue("${elasticnls.hbase.config.login.keytab}");

		System.out.println(hadoopconfigpath);
		System.out.println(principal);
		System.out.println(keytab);
		UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal, keytab);
		UserGroupInformation.setLoginUser(ugi);

		org.apache.hadoop.hbase.client.Connection hcon = ConnectionFactory.createConnection(config);
		// ctx.getBean(org.apache.hadoop.hbase.client.Connection.class);

		Admin admin = hcon.getAdmin();
		HTableDescriptor[] tableDescriptor = admin.listTables();

		// printing all the table names.
		for (int i = 0; i < tableDescriptor.length; i++) {
			System.out.println(tableDescriptor[i].getNameAsString());
		}

	}

	@Test
	public void testKafkaProducer() {
		CDTConfiguration config = (CDTConfiguration) ctx.getBean("elasticnls.cdt.config");
		KafkaProducer producer = new KafkaProducer(config.getProducerConfig());
		assert producer != null;
		Table rowMeta = new Table();
		TableConstraint pk = new TableConstraint();
		pk.setType(ConstraintType.PK);
		TreeMap<String, String> rowData = new TreeMap<String, String>();
		Column c = new Column();
		c.setColumnName("ID");
		c.setDatatype(DataType.INT);
		c.setPk(true);
		rowMeta.addColumn(c);
		pk.addColumn(c);

		c = new Column();
		c.setColumnName("DOB");
		c.setDatatype(DataType.VARCHAR);
		c.setPrecision(10);
		rowMeta.addColumn(c);

		c = new Column();
		c.setColumnName("VOL");
		c.setDatatype(DataType.DECIMAL);
		c.setPrecision(10);
		c.setScale(2);
		rowMeta.addColumn(c);

		rowMeta.setPrimaryKey(pk);
		rowMeta.setSchemaname("unittestschema");
		rowMeta.setTableName("unittesttable");

		DataSet dataSet = new DataSet();

		dataSet.setRowMeta(rowMeta);

		for (int i = 1; i < 1000; i++) {
			rowData = new TreeMap<String, String>();
			rowData.put("ID", String.valueOf(i));
			rowData.put("DOB", "2010-02-01");
			rowData.put("VOL", String.valueOf(i * 1.22));
			dataSet.addRow(rowData);

			dataSet.setSchemaName("TestSchema");
			dataSet.setTableName("TestTable");
		}
		producer.send(new ProducerRecord("elasticnls", 100, dataSet));
		producer.flush();
		producer.close();
	}


	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("BeforeInitialization : " + beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("AfterInitialization : " + beanName);
		return bean;
	}
}
