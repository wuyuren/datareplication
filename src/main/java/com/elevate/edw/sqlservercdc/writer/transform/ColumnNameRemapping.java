package com.elevate.edw.sqlservercdc.writer.transform;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.elevate.edw.jackson.model.DataSet;
import com.elevate.edw.sqlservercdc.metamodel.Column;
import com.elevate.edw.sqlservercdc.metamodel.Table;
//ColumnTableMapping implements DataTransformRule and modifies the
//DataSet by changing the column names based on the mapping specified
//in the xml file.  It requires a Map<String, String> in the constructor as well as the
//schema and table names.
public class ColumnNameRemapping implements DataTransformRule {
	String schemaName;
	String tableName;
	Map<String, String> replacements;	
	ColumnNameRemapping(Map<String, String> replacements, String schemaName, String tableName)
	{
		this.replacements = replacements;
		this.schemaName = schemaName;
		this.tableName = tableName;
	}
	public DataSet transform(DataSet data)
	{
		//To improve efficiency, a modified DataSet is only created and returned if the
		//schema and table of the DataSet match that of the rule.  Each table
		//must have its own ColumnNameRemapping rule that is added to the list of
		//DataTransformRules in the xml file.
		if (data.getSchemaName().equals(schemaName) && data.getTableName().equals(tableName))
		{
			DataSet d = new DataSet();
			Table transformRowMeta = data.getRowMeta();
			//Changing metadata column names
			List<Column> columns = data.getRowMeta().getColumns();
			for (Column c: columns)
			{
				//Set for error using containsKey
				String newName = replacements.get(c.getColumnName());
				if (newName != null && !newName.isEmpty())
					c.setColumnName(newName);
			}
			transformRowMeta.setColumns(columns);
			d.setRowMeta(transformRowMeta);
			//Creates a new map of rows and transfers the row data to the new
			//object, replacing column names if specified
			for (Map<String, String> row : data.getRows())
			{
				Map<String, String> transformRow = new TreeMap<String, String>();
				for (Map.Entry<String, String> entry : row.entrySet())
				{
				    String col = entry.getKey();
				    String val = entry.getValue();
				    col = transformUtil(col);
				    transformRow.put(col, val);
				}
				d.getRows().add(transformRow);
			}
			d.setSchemaName(data.getSchemaName());
			d.setTableName(data.getTableName());
			return d;
		}
		else
			return data;
	}
	//Used to streamline replacement
	private String transformUtil(String col)
	{
		if (replacements.containsKey(col))
			return replacements.get(col);
		else
			return col;
	}
	@Override
	public void verify(DatabaseMetaData meta) throws DataTransformRuleVerificationException {
		boolean exist = false;
		boolean schema = false;
		boolean table = false;
		try {
			ResultSet tables = meta.getTables(null, null, "%", null);
			while (tables.next())
			{
				if (tables.getString("TABLE_SCHEM").equalsIgnoreCase(schemaName))
				{
					schema = true;
					if (tables.getString("TABLE_NAME").equals(tableName))
					{
						table = true;
						Set<String> keys = replacements.keySet();
						for (String key : keys)
						{
							ResultSet columns = meta.getColumns(null, null, tables.getString("TABLE_NAME"), null);
							while (columns.next())
							{
								exist = (key.equals(columns.getString("COLUMN_NAME")));
								if (exist)
									break;
							}
							if (!exist)
							{
								throw new DataTransformRuleVerificationException("Verification failed.  Column " + key + " was not found in "
										+ schemaName + "." + tableName + ".");
							}
						}
						break;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Throws appropriate exception message if 
		if (!schema)
			throw new DataTransformRuleVerificationException("Verification failed.  Schema " + schemaName + " was not found.");
		else if (!table)
			throw new DataTransformRuleVerificationException("Verification failed.  Table " + tableName + " was not found in "
					+ "schema " + schemaName + ".");
			
	}
}
