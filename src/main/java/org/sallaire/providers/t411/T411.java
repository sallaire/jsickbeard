package org.sallaire.providers.t411;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.sallaire.dto.TvShowConfiguration.Quality;
import org.sallaire.providers.IProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class T411 implements IProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(T411.class);

	private static String token = null;

	@Override
	public void findEpisode(String name, String audioLang, Integer season, Integer number, Quality quality) {
		URIBuilder builder = new URIBuilder().setScheme("https").setHost("api.t411.io").setPath("/torrents/search/" + name) //
				// .setParameter("search", name) //
				.setParameter("cat", "210") //
				.setParameter("subcat", "433") //
				.setParameter("term[45][]", "" + (967 + season)) //
				.setParameter("term[46][]", "" + (936 + number));
		if ("fr".equals(audioLang)) {
			builder.setParameter("term[51][]", "1210");
		} else {
			builder.setParameter("term[51][]", "1212") //
					.setParameter("term[51][]", "1216");
		}
		;
		try {
			HttpGet httpGet = new HttpGet(builder.build());
			System.out.println(executeRequest(httpGet, String.class));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		new T411().findEpisode("Les revenants", "fr", 2, 2, Quality.SD);
	}
}
