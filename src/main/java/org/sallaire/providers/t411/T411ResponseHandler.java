package org.sallaire.providers.t411;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class T411ResponseHandler<T> implements ResponseHandler<T> {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private Class<T> clazz;

	public T411ResponseHandler(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T handleResponse(HttpResponse response) throws IOException, HttpResponseException {
		StatusLine statusLine = response.getStatusLine();
		HttpEntity entity = response.getEntity();
		if (statusLine.getStatusCode() >= 300) {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
		if (entity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		if (clazz.equals(String.class)) {
			return (T) IOUtils.toString(entity.getContent(), "UTF-8");
		} else {
			return MAPPER.readValue(entity.getContent(), clazz);
		}
	}

}
