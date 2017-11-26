package com.elevate.edw.sqlservercdc.kafka;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
/**
 * Class created to encapsulate the partition offset
 * controlelr receives application event using this class as the source
 * 
 * @author ywu
 *
 */
public class Bookmark {

	TopicPartition topicPartition;
	OffsetAndMetadata offset;
	
	public Bookmark(TopicPartition topicPartition, OffsetAndMetadata offset){
		
		this.topicPartition = topicPartition;
		this.offset = offset;
	}
	public TopicPartition getTopicPartition() {
		return topicPartition;
	}
	public void setTopicPartition(TopicPartition topicPartition) {
		this.topicPartition = topicPartition;
	}
	public OffsetAndMetadata getOffset() {
		return offset;
	}
	public void setOffset(OffsetAndMetadata offset) {
		this.offset = offset;
	}
	
	public boolean equals(Object other){
		if(other instanceof Bookmark){
			Bookmark o = (Bookmark) other;
			if(o != null && o.topicPartition.equals(this.topicPartition) && o.offset.equals(this.offset) ){
				return true;
			}
		}else{
			return false;
		}
		return false;
	}
	public int hashCode(){
		return (int) (this.topicPartition.topic().hashCode()* 1000 + this.topicPartition.partition()*100+this.offset.offset() %10);
	}
}
