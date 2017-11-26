package com.elevate.edw.sqlservercdc.writer;

import java.util.Map;

public class WriterConfiguration {
	private String writerClassString;
	private Map<String,String> writerConf;
	
	public WriterConfiguration(String writerClassString, Map<String,String> writerConf){
		this.writerClassString = writerClassString;
		this.writerConf = writerConf;
	}
	
	/**
	 * @return the writerClassString
	 */
	public String getWriterClassString() {
		return writerClassString;
	}
	/**
	 * @param writerClassString the writerClassString to set
	 */
	public void setWriterClassString(String writerClassString) {
		this.writerClassString = writerClassString;
	}
	/**
	 * @return the writerConf
	 */
	public Map<String, String> getWriterConf() {
		return writerConf;
	}
	/**
	 * @param writerConf the writerConf to set
	 */
	public void setWriterConf(Map<String, String> writerConf) {
		this.writerConf = writerConf;
	}
	
	
	
}
