package com.elevate.edw.sqlservercdc.kafka;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.elevate.edw.sqlservercdc.CDTException;

public interface Sender<K, V> {

	public void sendMessage(String topic, K key, V message)
			throws InterruptedException, ExecutionException, TimeoutException, CDTException;

	public void close();
	public void forceFlush();
	public Throwable getException();

}