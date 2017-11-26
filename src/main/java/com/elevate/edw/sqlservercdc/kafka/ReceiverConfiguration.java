package com.elevate.edw.sqlservercdc.kafka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import com.elevate.edw.sqlservercdc.writer.DataSetWriter;
import com.elevate.edw.sqlservercdc.writer.DataSetWriterFactory;
import com.elevate.edw.sqlservercdc.writer.WriteConfigurationException;
import com.elevate.edw.sqlservercdc.writer.WriterConfiguration;
import com.elevate.edw.sqlservercdc.writer.transform.DataTransformRule;
import com.elevate.edw.sqlservercdc.writer.transform.DataTransformer;

/**
 * Configuration object , this class contains required properties to start
 * ReceiverController. All required proeprties are obtained from receiverConfig
 * map.
 * 
 * @author ywu
 *
 */
public class ReceiverConfiguration {

	private Map<String, String> receiverConfig;

	/**
	 * Readonly objects. All values are set by receiverConfig set method
	 */
	private Map<String, Object> kafkaConsumerProps;
	private List<String> topics;
	private Pattern topicsPattern;
	private DatabaseBookmarkStore bookmarkStore;
	private WriterConfiguration writerConf;
	private DataTransformer dataTransformer;


	private int threadCount;

	public ReceiverConfiguration(Map<String, String> receiverConfig, WriterConfiguration writerConf,
			DatabaseBookmarkStore bookmarkStore, DataTransformer dataTransformer) throws ReciverConfigruationException {
		this.setReceiverConfig(receiverConfig);
		this.writerConf = writerConf;
		this.bookmarkStore = bookmarkStore;
		this.dataTransformer = dataTransformer;
	}
	public DataTransformer getDataTransformer()
	{
		return dataTransformer;
	}

	/**
	 * return shared instance of bookmarkstore.
	 * 
	 * @return
	 */
	public DatabaseBookmarkStore getBookmarkStore() {
		return this.bookmarkStore;
	}

	/**
	 * Return new writer
	 * 
	 * @return
	 * @throws WriteConfigurationException
	 */
	public DataSetWriter newWriterInstance() throws WriteConfigurationException {
		try {
			return DataSetWriterFactory.newWriterInstance(writerConf);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new WriteConfigurationException(e);
		}
	}

	/**
	 * @return the kafkaReceiverProperties
	 */
	public Map<String, Object> getKafkaConsumerProps() {

		return kafkaConsumerProps;
	}

	/**
	 * @return the receiverConfig
	 */
	public Map<String, String> getReceiverConfig() {
		return receiverConfig;
	}

	/**
	 * @param receiverConfig
	 *            the receiverConfig to set
	 * @throws ReciverConfigruationException
	 */
	private void setReceiverConfig(Map<String, String> receiverConfig) throws ReciverConfigruationException {
		this.receiverConfig = receiverConfig;
		if (receiverConfig == null) {
			throw new ReciverConfigruationException("receiverConfig map cannot be null!");
		}
		buildProeprties();
	}

	/**
	 * @return the threadCount
	 * @throws Exception
	 */
	public int getThreadCount() throws ReciverConfigruationException {
		return threadCount;
	}

	/**
	 * @return the topics
	 * @throws ReciverConfigruationException
	 */
	public List<String> getTopics() throws ReciverConfigruationException {

		return topics;
	}

	/**
	 * @return the topicsPattern
	 * @throws ReciverConfigruationException
	 */
	public Pattern getTopicsPattern() throws ReciverConfigruationException {

		return topicsPattern;
	}

	private void buildProeprties() throws ReciverConfigruationException {

		kafkaConsumerProps = buildKafkaProperties();

		// need at least one property, either topics, comma delimited list or
		// a topic pattern.
		if (receiverConfig.get("receiver.kafka.topics") == null
				&& receiverConfig.get("receiver.kafka.topicspattern") == null)
			throw new ReciverConfigruationException(
					"missing property :receiver.kafka.topics or receiver.kafka.topicspattern");

		String topic = (String) this.receiverConfig.get("receiver.kafka.topics");
		String patternStr = (String) this.receiverConfig.get("receiver.kafka.topicspattern");
		/*** topics and patterns are mutually exclusive params **/
		if (topic != null && patternStr != null) {
			new ReciverConfigruationException(
					String.format("topics and topicspattern are mutually exclusive!%ntopic = %s , topicspattern = %s",
							topic, patternStr));
		}
		if (topic != null && !topic.trim().equals("")) {
			topics = Arrays.asList(topic.split(","));
		} else if (patternStr != null && !patternStr.trim().equals("")) {
			Pattern.compile(patternStr);
		} else {
			throw new ReciverConfigruationException(String.format(
					"topics or topicspattern configuration error!topic = %s , topicspattern = %s", topic, patternStr));
		}

		// receiver thread count default value 1
		if (this.receiverConfig.containsKey("receiver.thread.count")) {
			this.threadCount = Integer.valueOf((String) this.receiverConfig.get("receiver.thread.count"));
		} else {
			this.threadCount = 1;
		}

	}

	private Map<String, Object> buildKafkaProperties() throws ReciverConfigruationException {

		// retrieve required attributes from receiver config.
		// If any of these properies is null raise exception

		Map<String, Object> props = new HashMap<>();
		// list of host:port pairs used for establishing the initial connections
		// to the Kakfa cluster
		// bootstrap.servers
		if (receiverConfig.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG) == null)
			throw new ReciverConfigruationException("missing property :" + ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, receiverConfig.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));

		if (receiverConfig.get(ConsumerConfig.GROUP_ID_CONFIG) == null)
			throw new ReciverConfigruationException("missing property :" + ConsumerConfig.GROUP_ID_CONFIG);
		// consumer groups allow a pool of processes to divide the work of
		// consuming and processing records
		// group.id
		props.put(ConsumerConfig.GROUP_ID_CONFIG, receiverConfig.get(ConsumerConfig.GROUP_ID_CONFIG));

		// enable.auto.commit = false
		// commit is manually handled in the thread
		props.put("enable.auto.commit", false);

		// key.deserializer
		if (receiverConfig.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG) != null)
			props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
					receiverConfig.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
		else
			props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		// value.deserializer
		if (receiverConfig.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG) != null)
			props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
					receiverConfig.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
		else
			props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		// max.partition.fetch.bytes is optional, if not specified, set it to 1M
		// default
		if (receiverConfig.get(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG) != null)
			props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
					receiverConfig.get(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG));
		else
			props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "10485760");

		if (receiverConfig.get(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG) != null) {
			props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
					receiverConfig.get(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG));
		} else {
			props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		}

		if (receiverConfig.get(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG) != null) {
			props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,
					receiverConfig.get(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG));
		} else {
			// force hearbeat to be 1/3 of session timeout
			props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, String
					.valueOf(Long.parseLong((String) props.get(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG)) / 3 + 100));
		}

		if (receiverConfig.get(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG) != null) {
			props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG,
					receiverConfig.get(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG));
		} else {
			props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, String
					.valueOf(Long.parseLong((String) props.get(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG)) + 10000));
		}
		if (receiverConfig.get("max.poll.records") != null) {
			props.put("max.poll.records", receiverConfig.get("max.poll.records"));
		} else {
			props.put("max.poll.records", 100);
		}
		// security features. If not specified, default to plain text
		// with no security
		// security.protocol
		String securityProtocol = (String) receiverConfig.get("security.protocol");
		String kerberosServiceName = (String) receiverConfig.get("sasl.kerberos.service.name");
		if (securityProtocol == null) {
			securityProtocol = "PLAINTEXT";
			kerberosServiceName = "";
		}
		;

		if (securityProtocol == null) {
			securityProtocol = "PLAINTEXT";
		}

		if (securityProtocol.equals("SASL_SSL") || securityProtocol.equals("SSL")) {
			// mandatory check on trusstore and password
			String truststore = (String) receiverConfig.get("ssl.truststore.location");
			String truststorepassword = (String) receiverConfig.get("ssl.truststore.password");
			if (truststore == null || truststore.trim().equals("") || truststorepassword == null) {

				throw new ReciverConfigruationException("Kafka SSL protocol requires trustore and trusstore password!");
			}
			props.put("ssl.truststore.location", truststore);
			props.put("ssl.truststore.password", truststorepassword);
		}
		props.put("security.protocol", securityProtocol);
		props.put("sasl.kerberos.service.name", kerberosServiceName);

		return props;
	}

	/**
	 * 
	 * @return
	 */
	public WriterConfiguration getWriterConf() {
		return writerConf;
	}
}
