package com.elevate.edw.sqlservercdc.kafka;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReceiverConfig implements ApplicationEventPublisherAware {
	private ApplicationEventPublisher publisher;
//
//	@Resource(name = "elasticKafkareceiverconfig")
//	Map<String, String> receiverConfig;
//
//	@Autowired
//	@Qualifier("elasticWriterConfig")
//	private WriterConfiguration writerConfiguration;
//
//	@Autowired
//	@Qualifier("elasticBookmarkStore")
//	private DatabaseBookmarkStore bookmarkStore;
//
//	@Bean
//	public Map consumerConfigs() {
//		Map props = new HashMap<>();
//		// list of host:port pairs used for establishing the initial connections
//		// to the Kakfa cluster
//		// bootstrap.servers
//		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, receiverConfig.get("kafka.bootstrap.servers"));
//		// key.deserializer
//		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
//		// value.deserializer
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//		// consumer groups allow a pool of processes to divide the work of
//		// consuming and processing records
//		// group.id
//		props.put(ConsumerConfig.GROUP_ID_CONFIG, receiverConfig.get("kafka.group.id"));
//		// max.partition.fetch.bytes
//		props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "10485760");
//		// security.protocol
//		String securityProtocol = receiverConfig.get("kafka.security.protocol");
//		String kerberosServiceName = receiverConfig.get("kafka.sasl.kerberos.service.name");
//		if (securityProtocol == null) {
//			securityProtocol = "PLAINTEXT";
//			kerberosServiceName = "";
//		}
//		;
//		props.put("security.protocol", securityProtocol);
//		props.put("sasl.kerberos.service.name", kerberosServiceName);
//
//		return props;
//	}
//
//	@Bean
//	@Qualifier(value = "elasticreceiverconfig")
//	public ReceiverConfiguration receiverConfiguration() throws ReciverConfigruationException {
//		return new ReceiverConfiguration(receiverConfig, writerConfiguration, bookmarkStore);
//
//	}
//
//	@Bean
//	public ReceiverController receiverController() throws ReciverConfigruationException {
//		ReceiverController ret = new ReceiverController();
//		ret.setReceiverConfig(receiverConfiguration());
//		// ret.setListener(receiverImpl());
//		return ret;
//	}
//
//	@Bean
//	public ConsumerFactory<Integer, DataSet> consumerFactory() {
//		return new DefaultKafkaConsumerFactory<Integer, DataSet>(consumerConfigs());
//	}
//
//
//	@Bean
//	public ConcurrentKafkaListenerContainerFactory<Integer, DataSet> kafkaListenerContainerFactory() {
//
//		ConcurrentKafkaListenerContainerFactory<Integer, DataSet> factory = new ConcurrentKafkaListenerContainerFactory<Integer, DataSet>();
//		factory.setConsumerFactory(consumerFactory());
//
//		return factory;
//	}
//
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;

	}

}
