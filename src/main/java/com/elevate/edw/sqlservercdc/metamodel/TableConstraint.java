package com.elevate.edw.sqlservercdc.metamodel;

import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TableConstraint {

	public enum ConstraintType {
		PK, FK
	};

	private Set<Column> columns;
	private ConstraintType type = ConstraintType.PK;


	public TableConstraint() {
		columns = new TreeSet<Column>();
	}

	public TableConstraint(ConstraintType type) {
		this();
		this.type = type;
	}

	public void addColumn(Column c) {
		this.columns.add(c);
	}

	public void removeColumn(Column c) {
		this.columns.remove(c);
	}

	public Set<Column> getColumns() {
		return columns;
	}

	public void setColumns(Set<Column> columns) {
		this.columns = columns;
	}

	public ConstraintType getType() {
		return type;
	}

	public void setType(ConstraintType type) {
		this.type = type;
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof TableConstraint)) {
			return false;
		}
		TableConstraint otc = (TableConstraint) o;
		if (!otc.type.equals(this.type))
			return false;
		if (!checkConstraintColumns(otc.getColumns()))
			return false;
		return true;
	}

	private boolean checkConstraintColumns(Set<Column> otcColumns) {
		for (Column otcc : otcColumns) {
			Column c = this.getConstraintColumnByName(otcc.getColumnName());
			if (c == null)
				return false;
			if (!c.equals(otcc)) {
				return false;
			}
		}
		return true;
	}

	private Column getConstraintColumnByName(String columnName) {
		Column ret = null;
		for (Column c : this.columns) {
			if (c.getColumnName().equals(columnName)) {
				ret = c;
				break;
			}
		}
		return ret;
	}

}
