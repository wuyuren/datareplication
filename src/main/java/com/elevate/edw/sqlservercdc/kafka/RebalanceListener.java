package com.elevate.edw.sqlservercdc.kafka;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RebalanceListener implements ConsumerRebalanceListener {
	public static final Logger LOG = LogManager.getLogger(ConsumerRebalanceListener.class);
	private ReceiverCallable callable;

	public RebalanceListener(ReceiverCallable callable) {
		this.callable = callable;
	}

	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		LOG.info("Partition revoked for thread : " + callable.getThreadName());
		LOG.info("Partition List: " + partitions.toArray());
		if (!callable.getFutures().isEmpty()) {

			callable.getFutures().get(0).cancel(true);
		}
		for (TopicPartition p : partitions) {
			try {
				if (callable.getCurrentOffsets().get(p) != null)
					callable.persistPosition(p, callable.getCurrentOffsets().get(p).offset());
			} catch (BookmarkException e) {
				LOG.fatal(e.getMessage());
				throw new RuntimeException(e);
			}

		}
	}

	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		LOG.info("Partition revoked for thread : " + callable.getThreadName());
		LOG.info("Partition List: " + partitions.toArray());
		for (TopicPartition p : partitions) {
			try {
				callable.adjustPositionForPartitionFromBookmarkStore(p);
			} catch (BookmarkException e) {
				LOG.fatal(e.getMessage());
				throw new RuntimeException(e);
			}
		}

	}

}
