package com.elevate.edw.sqlservercdc.test;

import java.io.IOException;
import java.util.Map;

import com.elevate.edw.jackson.model.DataSet;
import com.elevate.edw.sqlservercdc.writer.DataSetWriter;

public class DummyDatasetWriter implements DataSetWriter {

	public DummyDatasetWriter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeDataSet(DataSet dataSet) throws IOException {
		

	}

	@Override
	public void init(Map<String, String> conf) {
		// TODO Auto-generated method stub
		
	}

}
