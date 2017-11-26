package com.elevate.edw.sqlservercdc.writer.transform;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import com.elevate.edw.jackson.model.DataSet;
//The DataTransformer class is included in the ReceiverCallable
//and modifies the data sets before they are written by the
//HBase writer.  Its single data member is a list of rules
//as specified in the xml file.
public class DataTransformer {
	private List<DataTransformRule> rules;
	private DataSource readDs;
	public DataTransformer(List<DataTransformRule> rules, DataSource readDs)
	{
		this.rules = rules;
		this.readDs = readDs;
		//init();
	}
	public void init() throws DataTransformRuleVerificationException
	{
		//Checks validity of rule using verify method and
		//changes set value accordingly.  Throws error if
		//verification fails.
		verify();
	}
	protected void verify() throws DataTransformRuleVerificationException {
		try {
			//Uses the sqlserver connection to get the necessary metadata
			DatabaseMetaData meta = readDs.getConnection().getMetaData();
			for (DataTransformRule rule : rules)
			{
				rule.verify(meta);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public DataSet transformData(DataSet d)
	{
		//For each rule in the list, modify the data
		for (DataTransformRule rule : rules)
		{
			d = rule.transform(d);
		}
		return d;
	}

}
