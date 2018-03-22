package org.sallaire.service.provider.ygg;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.sallaire.dto.user.Quality;
import org.sallaire.service.provider.IProvider;
import org.sallaire.service.provider.Torrent;
import org.sallaire.service.provider.t411.dto.Authorization;
import org.sallaire.service.provider.t411.dto.SearchResult;
import org.sallaire.service.provider.t411.dto.SearchResults;
import org.sallaire.service.util.RegexFilterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class YggProvider implements IProvider {

	public static final String ID = "t411";
	public static final String USER = "user";
	public static final String PASSWORD = "password";

	private static final Logger LOGGER = LoggerFactory.getLogger(YggProvider.class);

	@Autowired
	private YggConfiguration configuration;

	@Autowired
	private RegexFilterConfiguration regex;

	private RestTemplate restTemplate;

	private AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor();

	private String user, password;

	@PostConstruct
	public void init() {
		LOGGER.debug("Initializing T411 Provider");
		// Initialize rest client
		LOGGER.debug("Initializing T411 rest client");
		restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

		LOGGER.debug("Initializing custom T411 converter");
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
		supportedMediaTypes.add(new MediaType("text", "html"));
		jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
		restTemplate.getMessageConverters().add(jsonConverter);
		LOGGER.debug("Custom deluge converter initialized");

		LOGGER.debug("Setting T411 interceptor");
		restTemplate.setInterceptors(Arrays.asList(authenticationInterceptor));
		LOGGER.debug("T411 interceptor set");

		LOGGER.debug("T411 Provider initialized");
	}

	@Override
	public Torrent findEpisode(Collection<String> names, String audioLang, Integer season, Integer number, Quality quality, List<String> excludedFiles) throws IOException {
		LOGGER.debug("Authenticating to T411");
		authenticationInterceptor.setToken(login());

		List<SearchResult> results = null;
		for (String name : names) {
			for (Integer option : Option.getOptionsToCheck()) {
				results = findEpisode(name, audioLang, season, number, quality, option, excludedFiles);
				if (CollectionUtils.isNotEmpty(results)) {
					LOGGER.debug("Results found for episode, stopping search");
					break;
				}
			}
		}

		if (CollectionUtils.isNotEmpty(results)) {
			try {
				SearchResult torrentToRetrieve = findBestResult(results);
				LOGGER.debug("Torrent {} will be downloaded", torrentToRetrieve.getName());
				URIBuilder builder = new URIBuilder().setScheme(configuration.getProtocol()).setHost(configuration.getHost()).setPath("/torrents/download/" + torrentToRetrieve.getId());
				Path torrent = Files.createTempFile(null, null);
				LOGGER.debug("Downloading torrent with url [{}] to [{}]", builder.build(), torrent);
				byte[] bytes = restTemplate.getForObject(builder.build(), byte[].class);
				Files.write(torrent, bytes);
				return new Torrent(torrentToRetrieve.getName(), torrent);
			} catch (URISyntaxException e) {
				throw new IOException(e);
			}
		}
		return null;

	}

	private SearchResult findBestResult(List<SearchResult> results) {
		if (CollectionUtils.isEmpty(results)) {
			return null;
		}
		if (results.size() > 1) {
			LOGGER.debug("More than one result has been found, picking best one");
			/**
			 * We have to take the 'best' one : proper / verified / seeders
			 */
			LOGGER.debug("Searching PROPER episodes");
			Pattern pattern = Pattern.compile("PROPER", Pattern.CASE_INSENSITIVE);
			List<SearchResult> filtered = results.stream().filter(s -> pattern.matcher(s.getName()).matches()).collect(Collectors.toList());
			if (!filtered.isEmpty()) {
				LOGGER.debug("{} PROPER episodes found", filtered.size());
				results = filtered;
			}
			if (results.size() == 1) {
				return results.get(0);
			} else {
				LOGGER.debug("Searching verified episodes");
				filtered = results.stream().filter(s -> s.isVerified()).collect(Collectors.toList());
			}
			if (!filtered.isEmpty()) {
				LOGGER.debug("{} verified episodes found", filtered.size());
				results = filtered;
			}
			if (results.size() == 1) {
				return results.get(0);
			} else {
				LOGGER.debug("returning episode with most seeders");
				return results.stream().max(Comparator.comparing(SearchResult::getSeeders)).get();
			}
		} else {
			LOGGER.debug("Only one result has been found, picking it by default");
			return results.get(0);
		}
	}

	private List<SearchResult> findEpisode(String name, String audioLang, Integer season, Integer number, Quality quality, Integer option, List<String> excludedFiles) throws IOException {
		URIBuilder builder = new URIBuilder().setScheme(configuration.getProtocol()).setHost(configuration.getHost()).setPath(configuration.getSearchPath() + name) //
				.addParameter(configuration.getCategoryKey(), configuration.getCategory()) //
				.addParameter(configuration.getSubCategoryKey(), configuration.getSubCategory());

		if (Option.NUMBER.isActivated(option)) {
			builder.addParameter(configuration.getSeasonKey(), String.valueOf(configuration.getSeason(season))) //
					.addParameter(configuration.getEpisodeKey(), String.valueOf(configuration.getEpisode(number)));
		} else {
			builder.setPath("/torrents/search/" + String.format(regex.getEpisodeFormat(), name, season, number));
		}

		if (Option.LANG.isActivated(option)) {
			List<String> params = configuration.getLangValue(audioLang);
			for (String param : params) {
				builder.addParameter(configuration.getLangKey(), param);
			}
		}

		if (Option.QUALITY.isActivated(option)) {
			List<String> params = configuration.getQualityValue(quality);
			for (String param : params) {
				builder.addParameter(configuration.getQualityKey(), param);
			}
		}

		try {
			LOGGER.info("Searching episode with request [{}]", builder.build());
			SearchResults results = restTemplate.getForObject(builder.build(), SearchResults.class);

			if (CollectionUtils.isNotEmpty(results.getTorrents())) {
				LOGGER.info("{} found for request", results.getTorrents().size());
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Torrents found with request :");
					results.getTorrents().stream().forEach(t -> LOGGER.debug(" - {}", t.getName()));
				}

				// Filter not wanted files
				results.setTorrents(results.getTorrents().stream().filter(t -> {
					if (excludedFiles.contains(t.getName())) {
						LOGGER.debug("Exclude result {} because it's an unwanted one", t.getName());
						return false;
					} else {
						return true;
					}
				}).collect(Collectors.toList()));

				// Now we have to filter results according to
				// quality/language/episode name
				LOGGER.info("Filter results for episode name [{}]", name);
				results.setTorrents(filterResults(results.getTorrents(), name, quality, audioLang, season, number));
				LOGGER.info("{} results after filter", results.getTorrents().size());
			} else {
				LOGGER.info("No result found for request");
			}
			return results.getTorrents();
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}

	private List<SearchResult> filterResults(List<SearchResult> results, String showName, Quality quality, String lang, Integer season, Integer number) {
		return results.stream().filter(t -> {
			return regex.matchEpisode(t.getName(), showName, season, number, quality, lang);
		}).collect(Collectors.toList());
	}

	private String login() throws IOException {
		LOGGER.debug("Authenticating to T411");

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("username", user);
		map.add("password", password);
		LOGGER.debug("Parameters for T411 authentication : {} -> {}", user, StringUtils.repeat("X", password.length()));

		LOGGER.debug("Posting request to T411 authentication [{}]", configuration.getAuthPath());
		return restTemplate.postForObject(configuration.getAuthPath(), map, Authorization.class).getToken();
		// TODO tester retour en cas de mauvais password
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void configurationChanged(Map<String, String> parameters) {
		if (MapUtils.isNotEmpty(parameters)) {
			LOGGER.debug("T411 configuration changed, update parameters : ");
			user = parameters.get(USER);
			LOGGER.debug(" user = {}", user);
			password = parameters.get(PASSWORD);
			LOGGER.debug(" password = {}", StringUtils.repeat("X", password.length()));
		} else {
			LOGGER.debug("Cleaning T411 configuration");
			user = "";
			password = "";
		}
	}

}
