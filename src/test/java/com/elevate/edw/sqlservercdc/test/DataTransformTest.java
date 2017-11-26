package com.elevate.edw.sqlservercdc.test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elevate.edw.jackson.model.DataSet;
import com.elevate.edw.sqlservercdc.kafka.RebalanceListener;
import com.elevate.edw.sqlservercdc.kafka.ReceiverCallable;
import com.elevate.edw.sqlservercdc.kafka.ReceiverConfiguration;
import com.elevate.edw.sqlservercdc.kafka.ReciverConfigruationException;
import com.elevate.edw.sqlservercdc.metamodel.Column;
import com.elevate.edw.sqlservercdc.metamodel.DataType;
import com.elevate.edw.sqlservercdc.metamodel.Table;
import com.elevate.edw.sqlservercdc.writer.DataSetWriter;
import com.elevate.edw.sqlservercdc.writer.WriteConfigurationException;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@Configuration
@ImportResource({ "file:///${cdt.app.config.path}/app_main.xml" })
public class DataTransformTest extends AbstractTest implements BeanPostProcessor{

	@Test
	public void testCtxLoad() {
		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		DataSource d = (DataSource) ctx.getBean("elasticnls.readds");
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}

	}
	//Tests the usage of the DataTransformer for elasticnls- because the list of
	//rules for the DataTransformer are empty, the data should not be modified.
	//If the data set isn't changed, the callable is tested to ensure there are no
	//errors in writing.
	@Test
	public void elasticCallableTest() throws WriteConfigurationException {
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		ReceiverConfiguration receiverConfig = (ReceiverConfiguration) ctx.getBean("elasticnls.receiver.config");
		ReceiverCallable r = new ReceiverCallable();
		r.setBookmarkStore(receiverConfig.getBookmarkStore());
		r.setDataTransformer(receiverConfig.getDataTransformer());
		KafkaConsumer<Integer, DataSet> consumer = new KafkaConsumer<Integer, DataSet>(
				receiverConfig.getKafkaConsumerProps());
		r.setConsumer(consumer);
		RebalanceListener rb = new RebalanceListener(r);
		try {
			if (receiverConfig.getTopics() != null) {
				consumer.subscribe(receiverConfig.getTopics(), rb);
			} else if (receiverConfig.getTopicsPattern() != null) {
				consumer.subscribe(receiverConfig.getTopicsPattern(), rb);
			} else {
				consumer.close();
				throw new ReciverConfigruationException("cannot find topics in configuraiton");
			}
		} catch (ReciverConfigruationException e) {
			e.printStackTrace();
		}
		int cnt = 0;
		int cnt2 = 100;
		ConsumerRecords<Integer, DataSet> cr = consumer.poll(1000);
		while (cnt == 0 && cnt2 != 0) {
			cr = consumer.poll(1000);
			cnt = cr.count();
			System.out.println(String.format("====================record count = %d==================", cnt));
			cnt2--;
		}
		ConsumerRecords<Integer, DataSet> newRecords = r.processTransformations(cr);
		if(areEqual(cr, newRecords))
		{
			assert(true);
			DataSetWriter writer = receiverConfig.newWriterInstance();
			r.setWriter(writer);
			singleThreadExecutor.submit(r);
	
			try {
			Thread.sleep(15000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		else
		{
			assert(false);
		}
	}
	//Tests the usage of the DataTransformer for pdolive.  The rules
	//specified in the pdolive xml file replace the column names for
	//the ACH_RECEIPT table to eliminate hashtags.  The test check for
	//hashtags and, if none are found, test the callable.
	@Test
	public void pdoliveHashtagTest() throws WriteConfigurationException {
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		ReceiverConfiguration receiverConfig = (ReceiverConfiguration) ctx.getBean("pdolive.receiver.config");
		ReceiverCallable r = new ReceiverCallable();
		r.setBookmarkStore(receiverConfig.getBookmarkStore());
		r.setDataTransformer(receiverConfig.getDataTransformer());
		KafkaConsumer<Integer, DataSet> consumer = new KafkaConsumer<Integer, DataSet>(
				receiverConfig.getKafkaConsumerProps());
		r.setConsumer(consumer);
		RebalanceListener rb = new RebalanceListener(r);
		try {
			if (receiverConfig.getTopics() != null) {
				consumer.subscribe(receiverConfig.getTopics(), rb);
			} else if (receiverConfig.getTopicsPattern() != null) {
				consumer.subscribe(receiverConfig.getTopicsPattern(), rb);
			} else {
				consumer.close();
				throw new ReciverConfigruationException("cannot find topics in configuraiton");
			}
		} catch (ReciverConfigruationException e) {
			e.printStackTrace();
		}
		int cnt = 0;
		int cnt2 = 100;
		ConsumerRecords<Integer, DataSet> cr = consumer.poll(1000);
		while (cnt == 0 && cnt2 != 0) {
			cr = consumer.poll(1000);
			cnt = cr.count();
			System.out.println(String.format("====================record count = %d==================", cnt));
			cnt2--;
		}
		ConsumerRecords<Integer, DataSet> newRecords = r.processTransformations(cr);
		if (noHashTags(newRecords))
		{
			assert(true);
			DataSetWriter writer = receiverConfig.newWriterInstance();
			r.setWriter(writer);
			singleThreadExecutor.submit(r);
			try {
			Thread.sleep(15000);
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
		}
		else
		{
			assert(false);
		}
	}
	//Checks a ConsumerRecords object for hashtags.
	private boolean noHashTags(ConsumerRecords<Integer, DataSet> newRecords) {
		boolean noHashTags = true;
		Iterator<TopicPartition> it = newRecords.partitions().iterator();
		while (it.hasNext())
		{
			TopicPartition p = it.next();
			List<ConsumerRecord<Integer, DataSet>> rs = newRecords.records(p);
			int resultsize = rs.size();
			for (int i = 0; i < resultsize; i++)
			{
				ConsumerRecord<Integer, DataSet> r = rs.get(i);
				for (Column c : r.value().getRowMeta().getColumns())
				{
					if (c.getColumnName().contains("#"))
						noHashTags = false;
				}
				List<Map<String, String>> rows = r.value().getRows();
				for (Map<String, String> row : rows)
					for (Map.Entry<String, String> entry : row.entrySet())
						if (entry.getKey().contains("#"))
							noHashTags = false;
			}
		}
		return noHashTags;
	}

	//Compares two ConsumerRecords objects and returns true if they contain equal data.
	private boolean areEqual(ConsumerRecords<Integer, DataSet> oldRecords, ConsumerRecords<Integer, DataSet> newRecords) {
		boolean equal = true;
		Iterator<TopicPartition> it1 = oldRecords.partitions().iterator();
		Iterator<TopicPartition> it2 = newRecords.partitions().iterator();
		while (it1.hasNext())
		{
			TopicPartition p1 = it1.next();
			TopicPartition p2 = it2.next();
			if (p1.topic().equals(p2.topic()))
			{
				List<ConsumerRecord<Integer, DataSet>> rs1 = oldRecords.records(p1);
				List<ConsumerRecord<Integer, DataSet>> rs2 = newRecords.records(p2);
				int resultsize = rs1.size();
				for (int i = 0; i < resultsize; i++)
				{
					ConsumerRecord<Integer, DataSet> r1 = rs1.get(i);
					ConsumerRecord<Integer, DataSet> r2 = rs2.get(i);
					if (!r1.key().equals(r2.key()))
						equal = false;
					else if (!r1.value().getRowMeta().equals(r2.value().getRowMeta()))
						equal = false;
					else if (r1.partition() != r2.partition())
						equal = false;
					else if (r1.offset() != r2.offset())
						equal = false;
					else
					{
						int datasize = r1.value().getRows().size();
						for (int j = 0; j < datasize; j++)
						{
							Map<String, String> map1 = r1.value().getRows().get(j);
							Map<String, String> map2 = r2.value().getRows().get(j);
							if (!map1.equals(map2))
								equal = false;
						}
					}
				}
			}
			else
				equal = false;
		}
		return equal;
	}


	private DataSet makeTestDataSet(String[] columnNames) {
		Table rowMeta = new Table();
		Column c = new Column();
		TreeMap<String, String> rowData = new TreeMap<String, String>();
		DataSet dataSet = new DataSet();
		for (String name : columnNames) {
			c = new Column();
			c.setColumnName(name);
			c.setDatatype(DataType.INT);
			rowMeta.addColumn(c);

		}
		for (int i = 1; i < 5; i++) {
			rowData = new TreeMap<String, String>();
			for (String name : columnNames) {
				rowData.put(name, "22");
			}
			dataSet.addRow(rowData);
			dataSet.setSchemaName("TestSchema");
			dataSet.setTableName("TestTable");
		}

		rowMeta.setSchemaname("pdolive");
		rowMeta.setTableName("pdolivetest");
		dataSet.setRowMeta(rowMeta);

		return dataSet;
	}
	//Required to set the parameter for the Spring Configuration
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("cdt.app.config.path", "C:/Users/mmuralidhar/Desktop/sqlservercdc/sqlservercdc/config/stg");
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}
}
