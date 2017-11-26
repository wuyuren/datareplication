package com.elevate.edw.sqlservercdc.writer.transform;

import java.sql.DatabaseMetaData;

import com.elevate.edw.jackson.model.DataSet;
//DataTransformRule is an interface with 2 methods: transform,
//which takes a DataSet as an argument and returns a modified DataSet,
//and verify, which verifies that the tables that the mappings will be
//performed on exist
public interface DataTransformRule {
	public DataSet transform(DataSet d);
	public void verify(DatabaseMetaData meta) throws DataTransformRuleVerificationException;
}
