package com.elevate.edw.sqlservercdc.util;

import com.elevate.edw.sqlservercdc.CDTException;
import com.elevate.edw.sqlservercdc.metamodel.Column;
import com.elevate.edw.sqlservercdc.metamodel.Table;

public class SqlUtil {

	public static String listCdcTables = "SELECT	UPPER(sys.schemas.name) as TABLE_SCHEMA,                          "
			+ "	UPPER(sys.tables.name) as TABLE_NAME                                  "
			+ "FROM	sys.change_tracking_tables                                 "
			+ "	INNER JOIN                                                     "
			+ "	sys.tables                                                     "
			+ "	ON                                                             "
			+ "	sys.tables.object_id = sys.change_tracking_tables.object_id    "
			+ "	INNER JOIN                                                     "
			+ "	sys.schemas on sys.schemas.schema_id = sys.tables.schema_id    "
			+ " ORDER BY UPPER(sys.schemas.name), UPPER(sys.tables.name) ";

	public static String buildTempTableDDL(Table t) throws CDTException {
		String sql = "IF OBJECT_ID(N'tempdb..#T') IS NOT NULL BEGIN DROP TABLE #T END; CREATE TABLE #T (";
		for (Column c : t.getPrimaryKey().getColumns()) {
			sql = sql + c.getColumnName(true) + DataTypeUtil.getDatatypeDDL(c) + ",";
		}
		sql = sql + "SYS_CHANGE_OPERATION CHAR(1), PRIMARY KEY (";
		int pksize = t.getPrimaryKey().getColumns().size();
		int idx = 0;
		for (Column c : t.getPrimaryKey().getColumns()) {
			idx++;
			sql = sql + c.getColumnName(true);
			if(idx != pksize){
				sql = sql+",";
			}
		}
		sql = sql + "))";
		return sql;
	}

	public static String buildChangeSQL(Table t, long lastSyncVersion) {
		String sql = "SELECT ";
		for (Column c : t.getPrimaryKey().getColumns()) {
			sql = sql + c.getColumnName(true) + ",";
		}
		sql = sql + "SYS_CHANGE_OPERATION FROM CHANGETABLE(CHANGES  " + t.getFullName(true) +","+ lastSyncVersion
				+ ") AS CT";
		return sql;
	}

	public static String getPrimaryKeySql() {
		return "SELECT	A.CONSTRAINT_CATALOG,	A.CONSTRAINT_NAME,	A.CONSTRAINT_SCHEMA,	B.TABLE_SCHEMA,"
				+ "B.TABLE_NAME,	B.COLUMN_NAME "
				+ "FROM	INFORMATION_SCHEMA.TABLE_CONSTRAINTS A	INNER JOIN	INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE B"
				+ "ON	A.CONSTRAINT_CATALOG = B.CONSTRAINT_CATALOG AND	A.CONSTRAINT_NAME = B.CONSTRAINT_NAME AND	"
				+ "A.CONSTRAINT_SCHEMA = B.CONSTRAINT_SCHEMA" + "WHERE	CONSTRAINT_TYPE='PRIMARY KEY' AND "
				+ "A.TABLE_SCHEMA = ? AND A.TABLE_NAME = ? ORDER BY B.COLUMN_NAME";
	}

	public static String getCurrentVersionSql() {
		return "select CHANGE_TRACKING_CURRENT_VERSION() as CURRENT_VERSION";
	}

	public static String getTableMetaSql() {
		String sql = "                                                                            "
				+ "SELECT	UPPER(A.TABLE_SCHEMA) AS TABLE_SCHEMA,                         "
				+ "	UPPER(A.TABLE_NAME) AS TABLE_NAME,                                     "
				+ "	UPPER(A.COLUMN_NAME) AS COLUMN_NAME,                                   "
				+ "	CASE WHEN PK.COLUMN_NAME IS NOT NULL THEN 'Y' ELSE 'N' END AS PK_FLG,  "
				+ "	A.ORDINAL_POSITION,                                                    "
				+ "	A.IS_NULLABLE,                                                         "
				+ "	UPPER(A.DATA_TYPE) AS DATA_TYPE,                                       "
				+ "	A.CHARACTER_MAXIMUM_LENGTH,                                            "
				+ "	A.NUMERIC_PRECISION,                                                   "
				+ "	A.NUMERIC_SCALE,                                                       "
				+ "	A.DATETIME_PRECISION                                                   "
				+ "FROM	INFORMATION_SCHEMA.COLUMNS A                                       "
				+ "	LEFT OUTER JOIN                                                        "
				+ "(                                                                       "
				+ "SELECT	A.CONSTRAINT_CATALOG,                                          "
				+ "	A.CONSTRAINT_NAME,                                                     "
				+ "	A.CONSTRAINT_SCHEMA,                                                   "
				+ "	B.TABLE_SCHEMA,                                                        "
				+ "	B.TABLE_NAME,                                                          "
				+ "	B.COLUMN_NAME                                                          "
				+ "FROM	INFORMATION_SCHEMA.TABLE_CONSTRAINTS A                             "
				+ "	INNER JOIN                                                             "
				+ "	INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE B                           "
				+ "	ON                                                                     "
				+ "	A.CONSTRAINT_CATALOG = B.CONSTRAINT_CATALOG AND                        "
				+ "	A.CONSTRAINT_NAME = B.CONSTRAINT_NAME AND                              "
				+ "	A.CONSTRAINT_SCHEMA = B.CONSTRAINT_SCHEMA                              "
				+ "WHERE	CONSTRAINT_TYPE='PRIMARY KEY'                                  "
				+ ") PK                                                                    "
				+ "ON                                                                      "
				+ "A.TABLE_NAME = PK.TABLE_NAME AND                                        "
				+ "A.TABLE_SCHEMA = PK.TABLE_SCHEMA AND                                    "
				+ "A.COLUMN_NAME = PK.COLUMN_NAME                                          "
				+ "WHERE                                                                   "
				+ "A.TABLE_SCHEMA = ? AND                                                  "
				+ "A.TABLE_NAME = ?                                                        "
				+ "ORDER BY A.ORDINAL_POSITION                                              ";

		return sql;
	}

	/***
	 * Insert sql to temp table #t to populate PK and change operation
	 * 
	 * @param table
	 * @return
	 */
	public static String buildInsSql(Table table) {
		String sql = "INSERT INTO #T (";
		for (Column c : table.getPrimaryKey().getColumns()) {
			sql = sql + c.getColumnName(true) + ",";
		}
		sql = sql + "SYS_CHANGE_OPERATION) values (";
		for (Column c : table.getPrimaryKey().getColumns()) {
			sql = sql + "?,";
		}
		sql = sql + "?)";
		return sql;
	}

	

	public static String buildFullSelectSql(Table table){
		StringBuffer sql = new StringBuffer("SELECT ");
		for(Column c: table.getColumns()){
			sql.append(c.getColumnName(true)+",");
		}
		sql.append("'I' AS SYS_CHANGE_OPERATION ");
		sql.append(" FROM "+table.getFullName(true)+" (nolock)");
		return sql.toString();
	}
	public static String buildDataSelectSql(Table table) {
		StringBuffer sql = new StringBuffer("SELECT ");
		for(Column c : table.getColumns()){
			//if it is pk, the data should come from change tracking
			if(c.isPk())
				sql.append("B."+c.getColumnName(true)+",");
			else
				sql.append("A."+c.getColumnName(true)+",");
		}
		sql.append("SYS_CHANGE_OPERATION FROM #T B (NOLOCK) LEFT OUTER JOIN "+table.getFullName(true)+" A (NOLOCK) ON 1=1 ");
		for(Column c : table.getPrimaryKey().getColumns()){
			sql.append(" AND A."+c.getColumnName(true)+" = B."+c.getColumnName(true));
		}
		
		return sql.toString();
	}

	
	
	
}
