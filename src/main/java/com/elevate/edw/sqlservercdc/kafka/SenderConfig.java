package com.elevate.edw.sqlservercdc.kafka;

import org.apache.logging.log4j.LogManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;


@Configuration
@Lazy
public class SenderConfig<K,V> {
	
	
	
	
//	
//	@Resource(name="producerConfig")
//	@Lazy
//	Map<String,Object> producerConfig;
//	
//	
//	
//	
//	@Bean
//	@Lazy
//	@Scope("prototype")
//	public ProducerFactory<Integer,DataSet> producerFactory(){
////		producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
////		producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//		for(String s: producerConfig.keySet())
//		LOG.info(String.format("Key = %s, Value = %s", s, producerConfig.get(s)));
//		
//		return new DefaultKafkaProducerFactory<Integer,DataSet>(producerConfig);
//	}
//	
//	@Bean
//	@Lazy
//	@Scope("prototype")
//	public KafkaTemplate<Integer,DataSet> kafkaTemplate(){
//		return new KafkaTemplate<Integer,DataSet>(producerFactory());
//	}
//	
//	@Bean
//	@Lazy
//	@Scope("prototype")
//	public KafkaProducer<Integer,DataSet>  kafkaProducer(){
////		producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
////		producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//		return new KafkaProducer<Integer,DataSet> (producerConfig);
//	}
//	
//
//	@Bean
//	@Lazy
//	@Scope("prototype")
//	public Sender<Integer, DataSet> sender(){
//		Sender<Integer, DataSet> sender = new KafkaSender<Integer,DataSet>(kafkaProducer());		
//		return sender;
//	}
}
