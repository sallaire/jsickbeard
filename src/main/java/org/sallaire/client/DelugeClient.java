package org.sallaire.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DelugeClient implements IClient {

	public void addTorrent(Path torrentPath, String torrentName) throws IOException {
		// Deluge response contains application/x-json media type which is not covered by default jackson mapper
		// We have to modify the mapper to accept this syntax
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
		supportedMediaTypes.add(new MediaType("application", "x-json"));
		jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
		converters.add(jsonConverter);

		// Initialize client
		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		restTemplate.setMessageConverters(converters);

		// Authentication
		DelugeRequestBody<String> body = new DelugeRequestBody<>();
		body.setId(1);
		body.setMethod("auth.login");
		body.getParams().add("");
		restTemplate.postForObject("http://37.187.19.83:8112/json", body, DelugeResponseBody.class);
		// TODO check it's ok

		// add the torrent
		DelugeRequestBody<Object> body2 = new DelugeRequestBody<>();
		body2.setId(2);
		String encodedContent = null;
		try (InputStream in = Files.newInputStream(torrentPath)) {
			encodedContent = Base64.getEncoder().encodeToString(IOUtils.toByteArray(in));
		}
		body2.getParams().add(torrentName);
		body2.getParams().add(encodedContent);
		body2.getParams().add(new HashMap<>());
		body2.setMethod("core.add_torrent_file");
		restTemplate.postForObject("http://37.187.19.83:8112/json", body2, DelugeResponseBody.class);
		// TODO check it's ok
	}

	public static void main(String[] args) throws IOException {
		// new DelugeClient().addTorrent();
	}
}
