package com.elevate.edw.sqlservercdc.writer.hbase;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
public class HBaseConfig {

//	@Resource(name = "elastlicHbaseWriterConfig")
//	Map<String, String> receiverConfig;
//
//	@Bean
//	public org.apache.hadoop.conf.Configuration hbaseConfiguration() {
//		org.apache.hadoop.conf.Configuration config = new org.apache.hadoop.conf.Configuration();
//		config.addResource(new Path("C:\\Users\\ywu\\workspace\\sqlservercdc\\config\\core-site.xml"));
//		config.addResource(new Path("C:\\Users\\ywu\\workspace\\sqlservercdc\\config\\hdfs-site.xml"));		
//		config.addResource(new Path("C:\\Users\\ywu\\workspace\\sqlservercdc\\config\\hbase-site.xml"));
//		return config;
//	}
//
//	@Bean
//	public org.apache.hadoop.hbase.client.Connection hbaseConnection() throws IOException {
//		org.apache.hadoop.hbase.client.Connection hbasecon = ConnectionFactory.createConnection(hbaseConfiguration());
//		return hbasecon;
//	}
//
//	@Bean
//	@Scope("prototype")
//	@Lazy
//	public HbaseWriter dataSetWriter() throws IOException {
//		org.apache.hadoop.conf.Configuration config = hbaseConfiguration();
//		for (String key : receiverConfig.keySet())
//			System.out.println("**********************" + key + " = " + receiverConfig.get(key));
//		
//		org.apache.hadoop.hbase.client.Connection hbasecon = hbaseConnection(); 
//		String namespace = receiverConfig.get("writer.hbase.namespace");
//		TableName[] tablenames = hbasecon.getAdmin().listTableNamesByNamespace(namespace);
//		Map<String, Table> tablemap  = new HashMap<String, Table>();
//		for(TableName tn : tablenames){
//			Table t = hbasecon.getTable(tn);
//			String tname = tn.getQualifierAsString();
//			tablemap.put(tname, t);
//		}
//		//Table t = hbaseConnection().getTable(TableName.valueOf((String) receiverConfig.get("reciver.writer.hbase.table")));
//		
//		HbaseWriter writer = new HbaseWriter();
//		writer.setHtableMap(tablemap);
//		writer.setColumnFamily((String) receiverConfig.get("writer.hbase.columnfamily"));
//		return writer;
//	}
}
