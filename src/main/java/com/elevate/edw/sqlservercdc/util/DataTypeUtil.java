package com.elevate.edw.sqlservercdc.util;

import com.elevate.edw.sqlservercdc.CDTException;
import com.elevate.edw.sqlservercdc.metamodel.Column;
import com.elevate.edw.sqlservercdc.metamodel.DataType;

public class DataTypeUtil {

	public static int mapDataTypeToSqlType(DataType dataType) throws CDTException {
		int ret = 0;
		switch (dataType) {
		case BIGINT:
			ret = java.sql.Types.BIGINT;
			break;
		case BINARY:
			ret = java.sql.Types.BINARY;
			break;
		case BIT:
			ret = java.sql.Types.BIT;
			break;
		case CHAR:
			ret = java.sql.Types.CHAR;
			break;

		case DATETIME:
			ret = java.sql.Types.TIMESTAMP;
			break;

		case DECIMAL:
			ret = java.sql.Types.DECIMAL;
			break;
		case FLOAT:
			ret = java.sql.Types.DOUBLE;
			break;
		case IMAGE:
			ret = java.sql.Types.LONGVARBINARY;
			break;
		case INT:
			ret = java.sql.Types.INTEGER;
			break;
		case MONEY:
			ret = java.sql.Types.DECIMAL;
			break;

		case NTEXT:
			ret = java.sql.Types.LONGVARCHAR;
			break;

		case NUMERIC:
			ret = java.sql.Types.NUMERIC;
			break;
		case NVARCHAR:
			ret = java.sql.Types.VARCHAR;
			break;
		case VARCHAR:
			ret = java.sql.Types.VARCHAR;
			break;

		case SMALLDATETIME:
			ret = java.sql.Types.TIMESTAMP;
			break;
		case SMALLINT:
			ret = java.sql.Types.SMALLINT;
			break;
		case SMALLMONEY:
			ret = java.sql.Types.DECIMAL;
			break;
		case TEXT:
			ret = java.sql.Types.LONGVARCHAR;
			break;

		case TINYINT:
			ret = java.sql.Types.TINYINT;
			break;

		case UNIQUEIDENTIFIER:
			ret = java.sql.Types.CHAR;
			break;
		case VARBINARY:
			ret = java.sql.Types.VARBINARY;
			break;
		// TODO: need research on this...
		case SQL_VARIANT:
			ret = java.sql.Types.VARCHAR;
			break;
		case DATE:
			ret = java.sql.Types.DATE;
			break;
		case DATETIME2:
			ret = java.sql.Types.TIMESTAMP;
		case XML:
			ret = java.sql.Types.SQLXML;
		default:
			throw new CDTException("no corresponding sqltypes for " + dataType);
		}
		return ret;
	}

	public static String dataTypeToString(DataType dataType) throws CDTException {
		String dtStr = "";
		switch (dataType) {
		case IMAGE:
			dtStr = "IMAGE";
			break;
		case SQL_VARIANT:
			dtStr = "SQL_VARIANT";
			break;
		case MONEY:
			dtStr = "MONEY";
			break;
		case INT:
			dtStr = "INT";
			break;
		case DECIMAL:
			dtStr = "DECIMAL";
			break;
		case VARBINARY:
			dtStr = "VARBINARY";
			break;
		case TEXT:
			dtStr = "TEXT";
			break;
		case SMALLINT:
			dtStr = "SMALLINT";
			break;
		case VARCHAR:
			dtStr = "VARCHAR";
			break;
		case BINARY:
			dtStr = "BINARY";
			break;
		case DATETIME:
			dtStr = "DATETIME";
			break;
		case DATETIME2:
			dtStr = "DATETIME2";
			break;
		case DATE:
			dtStr = "DATE";
			break;
		case NUMERIC:
			dtStr = "NUMERIC";
			break;
		case UNIQUEIDENTIFIER:
			dtStr = "UNIQUEIDENTIFIER";
			break;
		case TINYINT:
			dtStr = "TINYINT";
			break;
		case SMALLDATETIME:
			dtStr = "SMALLDATETIME";
			break;
		case FLOAT:
			dtStr = "FLOAT";
			break;
		case CHAR:
			dtStr = "CHAR";
			break;
		case BIGINT:
			dtStr = "BIGINT";
			break;
		case NTEXT:
			dtStr = "NTEXT";
			break;
		case XML:
			dtStr = "XML";
			break;
		case NVARCHAR:
			dtStr = "NVARCHAR";
			break;
		case SMALLMONEY:
			dtStr = "SMALLMONEY";
			break;
		case BIT:
			dtStr = "BIT";
			break;
		default:
			throw new CDTException("no corresponding sqltypes for " + dataType);
		}
		return dtStr;
	}


	public static String getDatatypeDDL(Column c ) throws CDTException {
		String ddl = "";
		switch (c.getDatatype()) {
		case IMAGE:
			ddl = "IMAGE";
			break;
		case SQL_VARIANT:
			ddl = "SQL_VARIANT";
			break;
		case MONEY:
			ddl = "MONEY";
			break;
		case INT:
			ddl = "INT";
			break;
		case DECIMAL:
			ddl = "DECIMAL("+c.getPrecision()+","+c.getScale()+")";
			break;
		case VARBINARY:
			ddl = "VARBINARY(" + c.getPrecision()+")";
			break;
		case TEXT:
			ddl = "TEXT";
			break;
		case SMALLINT:
			ddl = "SMALLINT";
			break;
		case VARCHAR:
			ddl = "VARCHAR("+c.getPrecision()+")";
			break;
		case BINARY:
			ddl = "BINARY("+c.getPrecision()+")";
			break;
		case DATETIME:
			ddl = "DATETIME";
			break;
		case NUMERIC:
			ddl = "NUMERIC("+c.getPrecision()+","+c.getScale()+")";
			break;
		case UNIQUEIDENTIFIER:
			ddl = "UNIQUEIDENTIFIER";
			break;
		case TINYINT:
			ddl = "TINYINT";
			break;
		case SMALLDATETIME:
			ddl = "SMALLDATETIME";
			break;
		case FLOAT:
			ddl = "FLOAT";
			break;
		case CHAR:
			ddl = "CHAR("+c.getPrecision()+")";
			break;
		case BIGINT:
			ddl = "BIGINT";
			break;
		case NTEXT:
			ddl = "NTEXT";
			break;
		case NVARCHAR:
			ddl = "NVARCHAR("+c.getPrecision()+")";;
			break;
		case SMALLMONEY:
			ddl = "SMALLMONEY";
			break;
		case BIT:
			ddl = "BIT";
			break;
		case XML:
			ddl="XML";
			break;
		case DATETIME2:
			ddl = "DATETIME2";
			break;
		case DATE:
			ddl = "DATE";
			break;
		default: 
			throw new CDTException("no corresponding sqltypes for " + c.getDatatype());
		}
		return ddl;
	}
}
