package com.elevate.edw.sqlservercdc.writer;

import java.io.IOException;
import java.util.Map;

import com.elevate.edw.jackson.model.DataSet;

public interface DataSetWriter {
	public void writeDataSet(DataSet dataSet) throws IOException, InterruptedException;

	public void init(Map<String, String> conf) throws WriteConfigurationException;
}
