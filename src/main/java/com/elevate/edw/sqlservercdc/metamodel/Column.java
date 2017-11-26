package com.elevate.edw.sqlservercdc.metamodel;

import com.elevate.edw.sqlservercdc.CDTException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Column implements Comparable<Column> {
	private String columnName;
	private DataType datatype = null;
	// precision applies to numeric values. NUMERIC(p,s)
	// precision applies as length for character type
	// precision applies to date time as time precision
	private int precision = 0;
	// scale applies nuemric values NUMERIC(p,s)
	private int scale = 0;

	private boolean nullable = false;

	private boolean isPk = false;
	
	
	public static  Column copyColumn(Column c) throws CloneNotSupportedException{
		if(c == null){
			throw new CloneNotSupportedException();
		}
		Column clonedCopy = new Column(c.getColumnName());
		clonedCopy.setDatatype(c.datatype);
		clonedCopy.setNullable(c.nullable);
		clonedCopy.setPk(c.isPk);
		clonedCopy.setPrecision(c.precision);
		clonedCopy.setScale(c.scale);
		return clonedCopy;
	}


	public Column(){
		
	}
	public Column(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName(boolean quote) {
		if (quote)
			return "\"" + this.getColumnName() + "\"";
		else
			return this.getColumnName();
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public DataType getDatatype() {
		return datatype;
	}

	public void setDatatype(DataType datatype) {
		this.datatype = datatype;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isPk() {
		return isPk;
	}

	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}

	public void populateAttributes(boolean isPk, boolean isNullable, String dataTypeStr, Integer charlen,
			Integer numPrecision, Integer numScale, Integer dtPrecision) throws CDTException {
		this.nullable = isNullable;
		this.isPk = isPk;
		switch (dataTypeStr.trim().toUpperCase()) {
		case "IMAGE":
			this.datatype = DataType.IMAGE;
			this.precision = charlen;
			break;
		case "SQL_VARIANT":
			this.datatype = DataType.SQL_VARIANT;
			this.precision = 0;
			break;
		case "VARBINARY":
			this.datatype = DataType.VARBINARY;
			this.precision = charlen;
			break;
		case "TEXT":
			this.datatype = DataType.TEXT;
			this.precision = charlen;
			break;
		case "NTEXT":
			this.datatype = DataType.NTEXT;
			this.precision = charlen;
			break;
		case "BINARY":
			this.datatype = DataType.BINARY;
			this.precision = (int) Math.pow(2, charlen);
			break;
		case "MONEY":
			this.datatype = DataType.MONEY;
			this.precision = numPrecision;
			this.scale = numScale;
			break;
		case "DECIMAL":

			this.datatype = DataType.DECIMAL;
			this.precision = numPrecision;
			this.scale = numScale;
			break;
		case "NUMERIC":
			this.datatype = DataType.NUMERIC;
			this.precision = numPrecision;
			this.scale = numScale;
			break;

		case "FLOAT":
			this.datatype = DataType.FLOAT;
			this.precision = 30;
			this.scale = 10;
			break;
		case "SMALLMONEY":
			this.datatype = DataType.SMALLMONEY;
			this.precision = numPrecision;
			this.scale = numScale;
			break;
		case "INT":
			this.datatype = DataType.INT;
			this.precision = numPrecision;
			this.scale = numScale;
			break;
		case "SMALLINT":
			this.datatype = DataType.SMALLINT;
			this.precision = numPrecision;
			this.scale = numScale;
			break;
		case "UNIQUEIDENTIFIER":
			this.datatype = DataType.UNIQUEIDENTIFIER;
			this.precision = 36;
			this.scale = 0;
			break;
		case "TINYINT":
			this.datatype = DataType.TINYINT;
			this.precision = numPrecision;
			this.scale = numScale;
			break;
		case "BIGINT":
			this.datatype = DataType.BIGINT;
			this.precision = numPrecision;
			this.scale = numScale;
			break;
		case "BIT":
			this.datatype = DataType.BIT;
			this.precision = 1;
			this.scale = 0;
			break;
		case "CHAR":
			this.datatype = DataType.CHAR;
			this.precision = charlen;
			this.scale = 0;
			break;
		case "VARCHAR":
			this.datatype = DataType.VARCHAR;
			this.precision = charlen;
			this.scale = 0;
			break;
		case "NVARCHAR":
			this.datatype = DataType.NVARCHAR;
			this.precision = charlen;
			this.scale = 0;
			break;
		case "DATETIME":
			this.datatype = DataType.DATETIME;
			this.precision = dtPrecision;
			this.scale = 0;
			break;
		case "SMALLDATETIME":
			this.datatype = DataType.SMALLDATETIME;
			this.precision = dtPrecision;
			this.scale = 0;
			break;
		case "DATE":
			this.datatype = DataType.DATE;
			this.precision = dtPrecision;
			this.scale = 0;
			break;
		case "DATETIME2":
			this.datatype = DataType.DATETIME2;
			this.precision = dtPrecision;
			this.scale = 0;
			break;
		case "XML":
			this.datatype = DataType.XML;
			this.precision = (int) Math.pow(2, 16)-1;
			this.scale = 0;
			break;
		default:
			throw new CDTException("Unrecongized data type: " + dataTypeStr);
		}

	}

	@Override
	public int compareTo(Column o) {

		return this.columnName.compareTo(((Column) o).columnName);
	}

	public boolean equals(Object c) {
		if (c == null || !(c instanceof Column)) {
			return false;
		}
		Column oc = (Column) c;
		boolean ret = true;
		ret = oc.columnName.equals(this.columnName) && oc.datatype.equals(this.datatype) && (oc.isPk == this.isPk)
				&& (oc.nullable == this.nullable) && (oc.precision == this.precision) && (oc.scale == this.scale);
		return ret;
	}


}
