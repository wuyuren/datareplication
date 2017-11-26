package com.elevate.edw.sqlservercdc.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elevate.edw.sqlservercdc.CDTException;

public class KafkaSender<K,V> implements Sender<K, V>  {
	public static final Logger LOG = LogManager.getLogger(KafkaSender.class);
	private KafkaProducer<K, V> producer;
	private Callback callback;
	private Throwable callbackException;

	/**
	 * interval to control a blocking flush call to avoid batch expired error 
	 * if caller is sending record faster than the producer can send.  
	 */
	private int flushInterval = 100;
	
	private int sendCnt = 0;
	public KafkaSender() {
		callback = new Callback() {

			@Override
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				if (exception != null) {
					LOG.fatal(exception.getMessage());
					callbackException = exception.getCause();
				} else {
					LOG.debug(String.format("Record Send: topic = %s, partition = %d, offset = %d", metadata.topic(),
							metadata.partition(), metadata.offset()));
					;					
				}
			}

		};
	}

	public KafkaSender(KafkaProducer<K, V> kafkaProducer) {
		this();
		this.producer = kafkaProducer;
	}

	public void sendMessage(String topic, K key, V message) throws CDTException {
		sendCnt++;
		if(sendCnt % this.flushInterval ==0){
			LOG.debug("FORCE FLUSH!!"+ sendCnt);
			producer.flush();
			sendCnt =0;
		}
		producer.send(new ProducerRecord<K, V>(topic, key, message), callback);
		
		if (callbackException != null) {
			throw new CDTException(callbackException);
		}

	}
	
//	public void sendMessage(String topic , DataSet message){
//		Set<Column> pk = message.getRowMeta().getPrimaryKey().getColumns();
//		int partCnt = this.producer.partitionsFor(topic).size();
//		List<DataSet> ds  = new ArrayList<DataSet>();
//		for(int i = 0; i < partCnt; i++){
//			DataSet d = new DataSet();
//			d.setRowMeta(message.getRowMeta());
//			d.setSchemaName(message.getSchemaName());
//			d.setTableName(message.getTableName());
//			ds.add(d);
//		}
//		for(Map<String, String> row : message.getRows()){
//			long pkval = getPkHash(row, pk);
//			ds.get((int) pkval % partCnt).addRow(row);
//		}
//		for(int i = 0;i < ds.size(); i++){
//			this.sendMessage(topic,i,ds.get(i) );
//		}
//	}
//
//	private long getPkHash(Map<String, String> row, Set<Column> pk) {
//		long ret = 0;
//		for(Column c : pk){
//			String cname= c.getColumnName();
//			String cval = row.get(cname);
//			ret = ret*100 + cval.hashCode() % 7 ;
//		}
//		return ret;
//	}

	public KafkaProducer<K, V> getProducer() {
		return producer;
	}

	public void setProducer(KafkaProducer<K, V> producer) {
		this.producer = producer;
	}

	@Override
	public void close() {
		LOG.info("kafka sender close is called!");
		this.producer.flush();
		//do not close producer, otherwise have to recreate
		//this.producer.close();				
	}

	@Override
	public Throwable getException() {
		// TODO Auto-generated method stub
		return this.callbackException;
	}

	@Override
	public void forceFlush() {
		this.producer.flush();
		
	}
	

}
