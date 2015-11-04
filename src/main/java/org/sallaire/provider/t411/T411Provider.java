package org.sallaire.provider.t411;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.client.utils.URIBuilder;
import org.sallaire.dto.TvShowConfiguration.Quality;
import org.sallaire.provider.IProvider;
import org.sallaire.provider.Torrent;
import org.sallaire.provider.t411.dto.Authorization;
import org.sallaire.provider.t411.dto.SearchResult;
import org.sallaire.provider.t411.dto.SearchResults;
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
public class T411Provider implements IProvider {

	private enum Option {
		NUMBER(0), LANG(1), QUALITY(2);

		private Option(int value) {
			this.value = 1 << value;
		}

		private int value;

		public int getValue() {
			return value;
		}

		/**
		 * Identify if the given option is activated or not.
		 * 
		 * @param options
		 *            current options value
		 * @param option
		 *            asked option
		 * @return boolean true if the option is activated
		 */
		public boolean isActivated(int options) {
			return (options & this.getValue()) != 0;
		}

		public static List<Integer> getOptionsToCheck() {
			Integer allOptions = 0;
			for (Option option : values()) {
				allOptions |= option.getValue();
			}

			List<Integer> optionsToCheck = new ArrayList<>();
			optionsToCheck.add(allOptions);
			for (int i = 0; i < values().length; i++) {
				optionsToCheck.add(allOptions & ~i);
			}
			for (int i = 0; i < values().length; i++) {
				optionsToCheck.add(0 | i);
			}
			optionsToCheck.add(0);

			return optionsToCheck;
		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(T411Provider.class);

	@Autowired
	private T411Configuration configuration;

	private RestTemplate restTemplate;

	private AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor();

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		restTemplate.setInterceptors(Arrays.asList(authenticationInterceptor));
		// List<HttpMessageConverter<?>> converters = new ArrayList<>();
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
		supportedMediaTypes.add(new MediaType("text", "html"));
		jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
		// converters.add(jsonConverter);
		restTemplate.getMessageConverters().add(jsonConverter);
		// restTemplate.setMessageConverters(Arrays.asList(new T411MessageConverter(), new ByteArrayHttpMessageConverter(), new FormHttpMessageConverter()));
	}

	@Override
	public Torrent findEpisode(String name, String audioLang, Integer season, Integer number, Quality quality) throws IOException {

		authenticationInterceptor.setToken(login());

		List<SearchResult> results = null;
		for (Integer option : Option.getOptionsToCheck()) {
			results = findEpisode(name, audioLang, season, number, quality, option);
			if (CollectionUtils.isNotEmpty(results)) {
				break;
			}
		}

		if (CollectionUtils.isNotEmpty(results)) {
			try {
				SearchResult torrentToRetrieve = findBestResult(results);
				URIBuilder builder = new URIBuilder().setScheme(configuration.getProtocol()).setHost(configuration.getHost()).setPath("/torrents/download/" + torrentToRetrieve.getId());
				Path torrent = Files.createTempFile(null, null);
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
			/**
			 * We have to take the 'best' one : proper / verified / seeders
			 */
			Pattern pattern = Pattern.compile("PROPER", Pattern.CASE_INSENSITIVE);
			List<SearchResult> filtered = results.stream().filter(s -> pattern.matcher(s.getName()).matches()).collect(Collectors.toList());
			if (!filtered.isEmpty()) {
				results = filtered;
			}
			if (results.size() == 1) {
				return results.get(0);
			} else {
				filtered = results.stream().filter(s -> s.isVerified()).collect(Collectors.toList());
			}
			if (!filtered.isEmpty()) {
				results = filtered;
			}
			if (results.size() == 1) {
				return results.get(0);
			} else {
				return results.stream().max(Comparator.comparing(SearchResult::getSeeders)).get();
			}
		} else {
			return results.get(0);
		}
	}

	private List<SearchResult> findEpisode(String name, String audioLang, Integer season, Integer number, Quality quality, Integer option) throws IOException {
		URIBuilder builder = new URIBuilder().setScheme(configuration.getProtocol()).setHost(configuration.getHost()).setPath(configuration.getSearchPath() + name) //
				.addParameter(configuration.getCategoryKey(), configuration.getCategory()) //
				.addParameter(configuration.getSubCategoryKey(), configuration.getSubCategory());

		if (Option.NUMBER.isActivated(option)) {
			builder.addParameter(configuration.getSeasonKey(), "" + (configuration.getSeason() + season)) //
					.addParameter(configuration.getEpisodeKey(), "" + (configuration.getEpisode() + number));
		} else {
			builder.setPath("/torrents/search/" + String.format(configuration.getEpsiodeFormat(), name, season, number));
		}

		boolean filterLang = false;
		if (Option.LANG.isActivated(option)) {
			List<String> params = configuration.getLangValue(audioLang);
			for (String param : params) {
				builder.addParameter(configuration.getLangKey(), param);
			}
		} else {
			filterLang = true;
		}

		boolean filterQuality = false;
		if (Option.QUALITY.isActivated(option)) {
			List<String> params = configuration.getQualityValue(quality);
			for (String param : params) {
				builder.addParameter(configuration.getQualityKey(), param);
			}
		}
		filterQuality = true;

		try {
			SearchResults results = restTemplate.getForObject(builder.build(), SearchResults.class);

			if (CollectionUtils.isNotEmpty(results.getTorrents())) {
				// Now we have to filter results according to quality/language if necessary
				if (filterLang) {
					filterResults(results.getTorrents(), configuration.getLangRegex(audioLang));
				}
				if (filterQuality) {
					filterResults(results.getTorrents(), configuration.getQualityRegex(quality));
				}
			}
			return results.getTorrents();
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}

	private void filterResults(List<SearchResult> results, final List<Pattern> patterns) {
		results.stream().filter(t -> {
			for (Pattern rx : patterns)
				if (rx.matcher(t.getName()).matches())
					return true;
			return false;
		});
	}

	private String login() throws IOException {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("username", "");
		map.add("password", "");

		return restTemplate.postForObject("https://api.t411.in/auth", map, Authorization.class).getToken();
	}

}
