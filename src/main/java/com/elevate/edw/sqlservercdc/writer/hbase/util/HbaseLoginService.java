package com.elevate.edw.sqlservercdc.writer.hbase.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HbaseLoginService {
	private static HbaseLoginService _SELF = null;
	private HbaseLoginTask t;
	private static Lock stateLock = new ReentrantLock();
	private ExecutorService executor;

	public static HbaseLoginService getInstance(org.apache.hadoop.conf.Configuration config, String principal,
			String keytab) throws IOException {
		try {
			stateLock.lock();
			if (_SELF == null) {
				_SELF = new HbaseLoginService(config, principal, keytab);
			}else{
				if(_SELF.t.isShutdown()){
					_SELF = new HbaseLoginService(config, principal, keytab);
				}
			}
		} finally {
			stateLock.unlock();
		}
		return _SELF;

	}

	public static void shutdown() {
		try {
			stateLock.lock();
			if (_SELF != null) {
				_SELF.t.setShutdown(true);
				_SELF.executor.shutdown();
				_SELF = null;
			}
		} finally {
			stateLock.unlock();
		}
	}

	private HbaseLoginService(org.apache.hadoop.conf.Configuration config, String principal, String keytab)
			throws IOException {
		t = new HbaseLoginTask(config, principal, keytab);
		executor = Executors.newSingleThreadExecutor();
		executor.submit(t);
	}

}
