package com.elevate.edw.sqlservercdc;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.kafka.clients.producer.KafkaProducer;

import com.elevate.edw.sqlservercdc.kafka.KafkaSender;
import com.elevate.edw.sqlservercdc.kafka.Sender;
import com.elevate.edw.sqlservercdc.metamodel.Table;

public class CDTConfiguration {
	private DataSource transDs;
	private DataSource readDs;
	
	private List<String> tableList;

	private int maxThreadCount = 1;
	
	private String kafkatopic;
	private Map<String, Object> producerConfig;

	private VersionStore<Table> versionStore;
	
	private int pullInterval;
	
	
	public VersionStore<Table> getVersionStore() {
		return versionStore;
	}

	public void setVersionStore(VersionStore<Table> versionStore) {
		this.versionStore = versionStore;
	}

	public int getMaxThreadCount() {
		return maxThreadCount;
	}

	public void setMaxThreadCount(int maxThreadCount) {
		this.maxThreadCount = maxThreadCount;
	}

	
	public List<String> getTableList() {
		return tableList;
	}

	public void setTableList(List<String> tableList) {
		this.tableList = tableList;
	}

	public DataSource getTransDs() {
		return transDs;
	}

	public void setTransDs(DataSource transDs) {
		this.transDs = transDs;
	}

	public DataSource getReadDs() {
		return readDs;
	}

	public void setReadDs(DataSource readDs) {
		this.readDs = readDs;
	}

	public String getKafkatopic() {
		return kafkatopic;
	}

	public void setKafkatopic(String kafkatopic) {
		this.kafkatopic = kafkatopic;
	}

	public Sender getSender() {
		return new KafkaSender(new KafkaProducer(this.producerConfig));
	}



	public Map<String, Object> getProducerConfig() {
		return producerConfig;
	}

	public void setProducerConfig(Map<String, Object> producerConfig) {
		this.producerConfig = producerConfig;
	}
	
	public int getPullInterval()
	{
		return pullInterval;
	}
	
	public void setPullInterval(int pullInterval)
	{
		this.pullInterval = pullInterval;
	}
	
}
