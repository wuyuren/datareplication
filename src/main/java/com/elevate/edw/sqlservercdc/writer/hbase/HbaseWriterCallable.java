package com.elevate.edw.sqlservercdc.writer.hbase;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elevate.edw.jackson.model.DataSet;
import com.elevate.edw.sqlservercdc.writer.DataSetWriter;

public class HbaseWriterCallable implements Callable<Boolean> {
	public static final Logger LOG = LogManager.getLogger(HbaseWriterCallable.class);
	private ConsumerRecords<Integer, DataSet> records;
	private Map<TopicPartition, OffsetAndMetadata> currentOffsets;
	private DataSetWriter writer;

	private boolean shutdown = false;

	public HbaseWriterCallable(Map<TopicPartition, OffsetAndMetadata> currentOffsets, DataSetWriter writer) {
		this.currentOffsets = currentOffsets;
		this.writer = writer;
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}

	public void setRecords(ConsumerRecords<Integer, DataSet> records) {
		this.records = records;
	}

	@Override
	public Boolean call() throws IOException, InterruptedException {
		Set<TopicPartition> partitions = records.partitions();
		try {
			for (TopicPartition p : partitions) {
				if (shutdown)
					break;
				List<ConsumerRecord<Integer, DataSet>> rs = records.records(p);
				for (ConsumerRecord<Integer, DataSet> r : rs) {
					if (shutdown)
						break;
					DataSet val = r.value();
					int dataSetSize = val.getRows().size();
					String metaschema = val.getSchemaName();
					String metatbl = val.getTableName();
					LOG.debug(String.format("Threadname= %s, metaschema=%s, metatbl = %s, rowcnt=%d",
							Thread.currentThread().getName(), metaschema, metatbl, dataSetSize));
					writer.writeDataSet(val);
				}
				// commit and log the bookmark into the bookmarkstore.
				OffsetAndMetadata lastoffset = new OffsetAndMetadata(rs.get(rs.size() - 1).offset(),
						Thread.currentThread().getName());
				// consumer.commitSync(Collections.singletonMap(p, lastoffset));
				// persistBookmark(p, lastoffset);
				// in memory store
				LOG.debug("threadname=" + Thread.currentThread().getName() + " put offset" + lastoffset.toString());
				currentOffsets.put(p, lastoffset);
			}
		} catch (Exception e) {
			LOG.fatal(Thread.currentThread().getName()+" is logging a fatal error!");
			LOG.fatal(e);
			e.printStackTrace();
			return false;
		}
		return true;

	}

}
