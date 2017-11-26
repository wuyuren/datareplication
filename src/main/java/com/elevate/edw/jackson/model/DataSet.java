package com.elevate.edw.jackson.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.elevate.edw.sqlservercdc.metamodel.Column;
import com.elevate.edw.sqlservercdc.metamodel.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class DataSet {

	public DataSet() {
		// TODO Auto-generated constructor stub
	}
	private  String schemaName;
	private  String tableName;
	private Table rowMeta ;
	private List<Map<String,String>> rows = new ArrayList<Map<String,String>>();
	
	
	
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<Map<String,String>> getRows() {
		return rows;
	}
	public void setRows(List<Map<String,String>> rows) {
		this.rows = rows;
	}
	public void setRowMeta(Table rowMeta) {
		this.rowMeta = rowMeta;		
	}
	public  Table getRowMeta(){
		return this.rowMeta;
	}
	
	public void addRow(Map<String,String> r){
		this.rows.add(r);
	}
	
	@JsonIgnore
	public int getMetaHash(){
		return this.rowMeta.hashCode();
	}
}
