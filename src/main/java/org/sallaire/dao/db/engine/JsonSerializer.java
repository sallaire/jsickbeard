package org.sallaire.dao.db.engine;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.mapdb.Serializer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializer<T> extends Serializer<T> {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(Include.NON_NULL).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private Class<T> clazz;

	public JsonSerializer(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public void serialize(DataOutput out, T value) throws IOException {
		out.writeUTF(OBJECT_MAPPER.writeValueAsString(value));
	}

	@Override
	public T deserialize(DataInput in, int available) throws IOException {
		return OBJECT_MAPPER.readValue(in.readUTF(), clazz);
	}

}
