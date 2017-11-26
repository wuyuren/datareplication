package com.elevate.edw.sqlservercdc.kafka;

import java.util.concurrent.ThreadFactory;

public class SimpleThreadFactory implements ThreadFactory {
	private String name;
	private UExceptionLogger uelog = new UExceptionLogger();
	private int cnt = -1;
	public SimpleThreadFactory(String name){
		this.name = name;
	}
	
	public SimpleThreadFactory(String name, int enablecnt){
		this.name = name;
		this.cnt = 0;
	}
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		if(cnt == -1)
		t.setName(name);
		else{
			cnt ++;
			t.setName(name +"-"+cnt);
		}
		t.setDaemon(false);
		t.setUncaughtExceptionHandler(this.uelog);
		return t;
	}

}
