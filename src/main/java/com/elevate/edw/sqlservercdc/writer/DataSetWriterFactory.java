package com.elevate.edw.sqlservercdc.writer;

public class DataSetWriterFactory {
	public static DataSetWriter newWriterInstance(WriterConfiguration conf)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, WriteConfigurationException {
		DataSetWriter writer = null;
		writer = (DataSetWriter) Class.forName(conf.getWriterClassString()).newInstance();
		writer.init(conf.getWriterConf());
		return writer;
	}
}
