package com.elevate.edw.sqlservercdc.kafka;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UExceptionLogger implements UncaughtExceptionHandler {
	public static final Logger LOG = LogManager.getLogger(UExceptionLogger.class);
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		e.printStackTrace();
		LOG.fatal(t.getName() + "throws "+e,e);
		

	}

}
