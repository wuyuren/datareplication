package com.elevate.edw.sqlservercdc.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.elevate.edw.sqlservercdc.writer.DataSetWriter;

@Configuration
public class DatasetWriterConfig {

	@Bean
	public DataSetWriter dataSetWriter(){
		return new DummyDatasetWriter();
	}

}
