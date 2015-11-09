package org.sallaire.client.deluge;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.sallaire.client.IClient;
import org.sallaire.dto.ClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DelugeClient implements IClient {

	private static final String ID = "deluge";

	@Autowired
	private DelugeConfiguration configuration;

	private ClientConfiguration userConfiguration;

	private RestTemplate restTemplate;

	@PostConstruct
	private void init() {
		// Deluge response contains application/x-json media type which is not covered by default jackson mapper
		// We have to modify the mapper to accept this syntax
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
		supportedMediaTypes.add(new MediaType("application", "x-json"));
		jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
		converters.add(jsonConverter);

		// Initialize client
		restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		restTemplate.setMessageConverters(converters);
	}

	public void addTorrent(Path torrentPath, String torrentName) throws IOException {

		// Authentication
		DelugeRequestBody<String> authBody = new DelugeRequestBody<>();
		authBody.setId(1);
		authBody.setMethod(configuration.getAuthMethod());
		authBody.getParams().add(userConfiguration.getPassword());
		restTemplate.postForObject(userConfiguration.getUrl() + configuration.getPath(), authBody, DelugeResponseBody.class);
		// TODO check it's ok

		// add the torrent
		DelugeRequestBody<Object> addTorrentParams = new DelugeRequestBody<>();
		addTorrentParams.setId(2);
		String encodedContent = null;
		try (InputStream in = Files.newInputStream(torrentPath)) {
			encodedContent = Base64.getEncoder().encodeToString(IOUtils.toByteArray(in));
		}
		addTorrentParams.getParams().add(torrentName);
		addTorrentParams.getParams().add(encodedContent);
		addTorrentParams.getParams().add(new HashMap<>());
		addTorrentParams.setMethod(configuration.getAddTorrentMethod());
		restTemplate.postForObject(userConfiguration.getUrl() + configuration.getPath(), addTorrentParams, DelugeResponseBody.class);
		// TODO check it's ok
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void configurationChanged(ClientConfiguration configuration) {
		this.userConfiguration = configuration;
	}
}
