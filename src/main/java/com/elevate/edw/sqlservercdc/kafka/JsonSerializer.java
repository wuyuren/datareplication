package com.elevate.edw.sqlservercdc.kafka;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elevate.edw.jackson.model.DataSet;

public class JsonSerializer implements Closeable, AutoCloseable, Serializer<DataSet>, Deserializer<DataSet> {

	private ObjectMapper mapper;

	public JsonSerializer() {
		this(null);
	}

	public JsonSerializer(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public static JsonSerializer getDefaultInstance() {
		return new JsonSerializer(new ObjectMapper());
	}

	@Override
	public DataSet deserialize(String topic, byte[] data) {

		try {
			return mapper.readValue(data, DataSet.class);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}

	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		if (mapper == null) {
			mapper = new ObjectMapper();
		}

	}

	@Override
	public byte[] serialize(String topic, DataSet data) {
		try {
			return mapper.writeValueAsBytes(data);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}

	}

	@Override
	public void close() {
		mapper = null;

	}

}
