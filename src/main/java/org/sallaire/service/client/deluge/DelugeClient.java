package org.sallaire.service.client.deluge;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.CookieSpecRegistries;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider.CompatibilityLevel;
import org.sallaire.dao.db.entity.ClientConfiguration;
import org.sallaire.dao.db.entity.Episode;
import org.sallaire.service.client.IClient;
import org.sallaire.service.provider.Torrent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DelugeClient implements IClient {

	public static final String ID = "deluge";

	private static final Logger LOGGER = LoggerFactory.getLogger(DelugeClient.class);

	@Autowired
	private DelugeConfiguration configuration;

	private ClientConfiguration userConfiguration;

	private RestTemplate restTemplate;

	private HttpHeaders headers = new HttpHeaders();

	@PostConstruct
	private void init() {
		// Deluge response contains application/x-json media type which is not covered by default jackson mapper
		// We have to modify the mapper to accept this syntax
		LOGGER.debug("Initializing deluge client");
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		LOGGER.debug("Initializing custom deluge converter");
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
		supportedMediaTypes.add(new MediaType("application", "x-json"));
		jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
		converters.add(jsonConverter);
		LOGGER.debug("Custom deluge converter initialized");

		// Initialize client
		LOGGER.debug("Initializing deluge rest client");
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		CloseableHttpClient client = HttpClients.custom() //
				.setDefaultCookieSpecRegistry( //
						CookieSpecRegistries.createDefaultBuilder() //
								.register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider(CompatibilityLevel.DEFAULT, PublicSuffixMatcherLoader.getDefault(), new String[] { "EEE, dd MMM yyyy HH:mm:ss z" }, false)) //
								.build()) //
				.build();
		factory.setHttpClient(client);
		restTemplate = new RestTemplate(factory);
		restTemplate.setMessageConverters(converters);

		// Deluge accept only content-type "application/json" with no charset
		// Charset is by default added by spring, we have to override manually content-type in each request
		headers.setContentType(MediaType.APPLICATION_JSON);
		LOGGER.debug("Deluge rest client initialized");
		LOGGER.debug("Deluge client initialized");
	}

	public void addTorrent(Torrent torrent, Path location, Episode episode) throws IOException {

		// Authentication
		String delugeUrl = userConfiguration.getUrl() + configuration.getPath();
		LOGGER.debug("Authenticating to deluge");
		DelugeRequestBody<String> authBody = new DelugeRequestBody<>();
		authBody.setId(1);
		authBody.setMethod(configuration.getAuthMethod());
		authBody.getParams().add(userConfiguration.getPassword());
		LOGGER.info("Requesting deluge with [{}] and method [{}]", authBody.getMethod());

		DelugeResponseBody response = restTemplate.postForObject(delugeUrl, new HttpEntity<DelugeRequestBody<String>>(authBody, headers), DelugeResponseBody.class);
		checkReponse(response);
		LOGGER.debug("Authenticating to deluge done with success");

		// add the torrent
		LOGGER.debug("Sending torrent to deluge");
		DelugeRequestBody<Object> addTorrentParams = new DelugeRequestBody<>();
		addTorrentParams.setId(2);
		String encodedContent = null;
		try (InputStream in = Files.newInputStream(torrent.getPath())) {
			encodedContent = Base64.getEncoder().encodeToString(IOUtils.toByteArray(in));
		}
		addTorrentParams.getParams().add(torrent.getName());
		addTorrentParams.getParams().add(encodedContent);
		addTorrentParams.getParams().add(getParameters(location, episode));
		addTorrentParams.setMethod(configuration.getAddTorrentMethod());
		LOGGER.info("Requesting deluge with [{}] and method [{}]", authBody.getMethod());
		response = restTemplate.postForObject(delugeUrl, new HttpEntity<DelugeRequestBody<Object>>(addTorrentParams, headers), DelugeResponseBody.class);
		checkReponse(response);
		LOGGER.debug("Torrent sent to deluge with success");
	}

	private void checkReponse(DelugeResponseBody response) throws IOException {
		if (response.getError() != null) {
			throw new IOException("Error while sending requestto deluge code=[" + response.getError().getCode() + "], message=[" + response.getError().getMessage() + "]");
		}
	}

	private Map<String, String> getParameters(Path location, Episode episode) {
		Map<String, String> params = new HashMap<>();
		if (userConfiguration.getMoveShow()) {
			params.put("move_completed", "true");
			if (userConfiguration.getSeasonPattern() != null) {
				Path path = location.resolve(String.format(userConfiguration.getSeasonPattern(), episode.getSeason()));
				params.put("move_completed_path", path.toString());
			} else {
				params.put("move_completed_path", location.toString());
			}
		}
		if (userConfiguration.getStopRatio() != null) {
			params.put("stop_at_ratio", "true");
			params.put("stop_ratio", userConfiguration.getStopRatio().toString());
		}
		// TODO remove_at_ration, label
		return params;
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
