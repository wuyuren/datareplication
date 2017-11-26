package com.elevate.edw.sqlservercdc.kafka;

public class TableVersion {
	private String tableName;
	private long changeVersion;
	
	public TableVersion(String tableName, long changeVersion){
		this.tableName = tableName;
		this.changeVersion = changeVersion;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public long getChangeVersion() {
		return changeVersion;
	}
	public void setChangeVersion(long changeVersion) {
		this.changeVersion = changeVersion;
	}
	

}
