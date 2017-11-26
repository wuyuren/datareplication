package com.elevate.edw.sqlservercdc.metamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Table{
	private String schemaName;
	private String tableName;
	private List<Column> columns;
	private TableConstraint primaryKey;
	
	/**
	 * Static method to clone table object
	 * @param t
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public static Table copyTable(Table t) throws CloneNotSupportedException{
		Table t1 =null;
		if(t == null){
			throw new CloneNotSupportedException();
		}
		t1 = new Table();
		t1.setSchemaname(t.getSchemaname());
		t1.setTableName(t.getTableName());
		//lookup table created to map the column to its coloned instance
		//this structure is used later to do lookup when assign primary key
		//columns
		TreeMap<Column, Column> oldToNew = new TreeMap<Column, Column>();
		for(Column c: t.getColumns()){
			Column newColumn = Column.copyColumn(c);
			oldToNew.put(c, newColumn);
			t1.addColumn(newColumn);
		}
		TableConstraint newPrimaryKey = new TableConstraint(t.getPrimaryKey().getType());
		for(Column oldColumn : t.getPrimaryKey().getColumns()){
			newPrimaryKey.addColumn(oldToNew.get(oldColumn));
		}
		t1.setPrimaryKey(newPrimaryKey);
		
		return t1;
	}
	
	
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

	public boolean isContainsLargeBinary() {
		boolean ret = false;
		for(Column c:columns){
			switch(c.getDatatype()){
			case VARBINARY:
			case TEXT: 
			case BINARY:
			case NTEXT:
			case IMAGE:
			case XML:
				return true;				
			default:
			}
		}
		return ret;
	}


	public Table() {
		columns = new ArrayList<Column>();
		primaryKey = new TableConstraint(TableConstraint.ConstraintType.PK);
	}

	public Table(String schemaName, String tableName) {
		this();
		this.schemaName = schemaName;
		this.tableName = tableName;
	}

	public void addColumn(Column c) {
		this.columns.add(c);
	}

	public void removeColumn(Column c) {
		this.columns.remove(c);
	}

	public void addColumnToPk(Column c) {
		this.primaryKey.addColumn(c);
	}

	public void removeColumnFromPk(Column c) {
		this.primaryKey.removeColumn(c);
	}

	public boolean tableHasPk() {
		return this.primaryKey.getColumns().size() == 0 ? false : true;
	}

	public String getSchemaname() {
		return schemaName;
	}

	public void setSchemaname(String schemaname) {
		this.schemaName = schemaname;
	}

	public String getTablename() {
		return tableName;
	}

	public void setTablename(String tablename) {
		this.tableName = tablename;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public TableConstraint getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(TableConstraint primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean equals(Object other) {
		if(other == null || !(other instanceof Table)){
			return false;
		}
		Table ot = (Table) other;
		
		if (!(this.schemaName.equals(ot.getSchemaname()) && this.tableName.equals(ot.getTablename())))
			return false;
		
		if(!checkColumn (ot.getColumns()))
			return false;
		if(!this.primaryKey.equals(ot.getPrimaryKey()))
			return false;
		return true;
	}


	private boolean checkColumn(List<Column> ocolumns) {
		boolean ret = true;
		if(this.columns.size()!=ocolumns.size())
			return false;
		for (Column oc : ocolumns){
			Column c = getColumnByName(oc.getColumnName());
			if(c == null){
				ret = false;
				break;
			}else{
				if(!c.equals(oc)){
					ret = false;
					break;
				}
			}
		}
		return ret;
	}
	
	private Column getColumnByName(String name){
		Column ret = null;
		for (Column c : columns){
			if(c.getColumnName().equals(name))
				return c;
		}
		return ret;
	}
	
	public String getFullName(boolean quote){
		if(quote)
			return "\""+this.schemaName+"\".\""+this.tableName+"\"";
		else 
			return this.schemaName+"."+this.tableName;
	}


}
