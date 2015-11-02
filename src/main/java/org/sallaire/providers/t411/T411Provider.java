package org.sallaire.providers.t411;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.sallaire.dto.TvShowConfiguration.Quality;
import org.sallaire.providers.IProvider;
import org.sallaire.providers.t411.dto.Authorization;
import org.sallaire.providers.t411.dto.SearchResult;
import org.sallaire.providers.t411.dto.SearchResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(T411Provider.class);

	@Autowired
	private T411Configuration configuration;

	private volatile String token = null;

	@Override
	public void findEpisode(String name, String audioLang, Integer season, Integer number, Quality quality) throws IOException {

		// First check with all params
		Integer allOptions = 0;
		for (Option option : Option.values()) {
			allOptions |= option.getValue();
		}

		List<SearchResult> results = findEpisode(name, audioLang, season, number, quality, allOptions);

		if (CollectionUtils.isEmpty(results)) {
			// If not found, check by removing one option
			for (int i = 0; i < Option.values().length; i++) {
				Integer withoutOption = allOptions & ~i;
				results = findEpisode(name, audioLang, season, number, quality, withoutOption);
				if (CollectionUtils.isNotEmpty(results)) {
					break;
				}
			}
		}

		if (CollectionUtils.isEmpty(results)) {
			// Not found yet,
			for (int i = 0; i < Option.values().length; i++) {
				Integer oneOption = 0 | i;
				results = findEpisode(name, audioLang, season, number, quality, oneOption);
				if (CollectionUtils.isNotEmpty(results)) {
					break;
				}
			}
		}

		if (CollectionUtils.isEmpty(results)) {
			results = findEpisode(name, audioLang, season, number, quality, 0);
		}

		/**
		 * We have to take the 'best' one : proper / size / seeders ?
		 */
		if (CollectionUtils.isNotEmpty(results)) {
			try {
				Long idToRetrieve = results.get(0).getId();
				URIBuilder builder = new URIBuilder().setScheme(configuration.getProtocol()).setHost(configuration.getHost()).setPath("/torrents/download/" + idToRetrieve);
				HttpGet httpGet = new HttpGet(builder.build());
				Path torrent = executeDownload(httpGet);
				try (InputStream in = Files.newInputStream(torrent)) {
					String encodedContent = Base64.getEncoder().encodeToString(IOUtils.toByteArray(in));
					System.out.println(encodedContent);
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
			if ("fr".equals(audioLang)) {
				builder.addParameter(configuration.getLangKey(), configuration.getLangVf());
			} else {
				String[] params = configuration.getLangVo().split(",");
				for (String param : params) {
					builder.addParameter(configuration.getLangKey(), param);
				}
			}
		} else {
			filterLang = true;
		}

		boolean filterQuality = false;
		if (Option.QUALITY.isActivated(option)) {
			String[] params = new String[0];
			switch (quality) {
			case SD:
				params = configuration.getQualitySD().split(",");
				break;
			case P720:
				params = configuration.getQuality720().split(",");
				break;
			case P1080:
				params = configuration.getQuality1080().split(",");
				break;
			default:
				break;
			}
			for (String param : params) {
				builder.addParameter(configuration.getQualityKey(), param);
			}
		}
		filterQuality = true;

		HttpGet httpGet;
		try {
			httpGet = new HttpGet(builder.build());
			SearchResults results = executeRequest(httpGet, SearchResults.class);

			if (CollectionUtils.isNotEmpty(results.getTorrents())) {
				// Now we have to filter results according to quality/language if necessary
				if (filterLang) {

				}
				if (filterQuality) {

				}
			}
			return results.getTorrents();
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}

	private <T> T executeRequest(HttpUriRequest request, Class<T> clazz) throws IOException {
		if (token == null) {
			login();
		}
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			request.addHeader("Authorization", token);
			return httpclient.execute(request, new T411ResponseHandler<>(clazz));
		} catch (HttpResponseException e) {
			if (e.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				// Either token is not set, or it's expired, get a new one
				login();
				// Now re-try the request
				return executeRequest(request, clazz);
			} else {
				throw e;
			}
		}
	}

	private Path executeDownload(HttpUriRequest request) throws IOException {
		if (token == null) {
			login();
		}
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			request.addHeader("Authorization", token);
			try (CloseableHttpResponse response = httpclient.execute(request)) {
				if (response.getStatusLine().getStatusCode() >= 300) {
					throw new HttpResponseException(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
				}
				try (InputStream inputStream = response.getEntity().getContent()) {
					LOGGER.debug("[TVDB] Processing archive result");
					Path download = Files.createTempFile(null, null);
					Files.copy(inputStream, download, StandardCopyOption.REPLACE_EXISTING);
					return download;
				}
			}
		} catch (HttpResponseException e) {
			if (e.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				// Either token is not set, or it's expired, get a new one
				login();
				// Now re-try the request
				return executeDownload(request);
			} else {
				throw e;
			}
		}
	}

	private void login() throws IOException {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost("https://api.t411.in/auth");
			List<NameValuePair> formParams = new ArrayList<>();
			formParams.add(new BasicNameValuePair("username", ""));
			formParams.add(new BasicNameValuePair("password", ""));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
			httpPost.setEntity(entity);
			Authorization auth = httpclient.execute(httpPost, new T411ResponseHandler<>(Authorization.class));
			token = auth.getToken();
		}
	}

	public static void main(String[] args) throws IOException {
		new T411Provider().findEpisode("Les revenants", "fr", 2, 2, Quality.SD);
	}
}
