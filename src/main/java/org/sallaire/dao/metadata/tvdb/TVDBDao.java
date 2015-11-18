package org.sallaire.dao.metadata.tvdb;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.metadata.IMetaDataDao;
import org.sallaire.dto.metadata.Episode;
import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.dto.metadata.TvShow;
import org.sallaire.dto.tvdb.ShowData;
import org.sallaire.dto.tvdb.TVDBSearchResult;
import org.sallaire.dto.tvdb.TVDBSearchResults;
import org.sallaire.dto.tvdb.UpdateItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Repository
public class TVDBDao implements IMetaDataDao {
	private static final String API_KEY = "CCB5F7ABBDFFA0DF";
	private static final String TVDB_URL = "thetvdb.com/api/";
	private static final String SEARCH_QUERY = "GetSeries.php";
	private static final String UPDATE_QUERY = "Updates.php";

	private static final Logger LOGGER = LoggerFactory.getLogger(TVDBDao.class);

	public static class TVDBHandler<T> implements ResponseHandler<T> {

		private Class<T> clazz;

		public TVDBHandler(Class<T> clazz) {
			this.clazz = clazz;
		}

		@Override
		public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			StatusLine statusLine = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			if (statusLine.getStatusCode() >= 300) {
				throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
			}
			if (entity == null) {
				throw new ClientProtocolException("Response contains no content");
			}
			return JAXB.unmarshal(entity.getContent(), clazz);
		}

	}

	public List<SearchResult> searchForShows(String name, String lang) throws DaoException {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder();
			builder.setScheme("http").setHost(TVDB_URL).setPath(SEARCH_QUERY).setParameter("seriesname", name).setParameter("lang", lang);
			HttpGet httpget = new HttpGet(builder.build());
			List<TVDBSearchResult> results = httpclient.execute(httpget, new TVDBHandler<TVDBSearchResults>(TVDBSearchResults.class)).getResults();
			return TVDBConverter.convertSearchResults(results);
		} catch (IOException | URISyntaxException e) {
			throw new DaoException("Error while querying TVDB url", e);
		}
	}

	public List<Long> getShowsToUpdate(Long fromTime) throws DaoException {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder();
			builder.setScheme("http").setHost(TVDB_URL).setPath(UPDATE_QUERY).setParameter("time", fromTime.toString());
			HttpGet httpget = new HttpGet(builder.build());
			return httpclient.execute(httpget, new TVDBHandler<UpdateItems>(UpdateItems.class)).getShowIds();
		} catch (IOException | URISyntaxException e) {
			throw new DaoException("Error while querying TVDB url", e);
		}
	}

	public TvShow getShowInformation(Long id, String lang) throws DaoException {
		return TVDBConverter.convertFromTVDB(getShowData(id, lang).getShowInfo());
	}

	private ShowData getShowData(Long id, String lang) throws DaoException {
		LOGGER.debug("[TVDB] Getting show information for tvdb id {} and lang {}", id, lang);
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder();
			builder.setScheme("http").setHost(TVDB_URL).setPath(SEARCH_QUERY + API_KEY + "/series/" + id + "/all/" + lang + ".zip");
			HttpGet httpget = new HttpGet(builder.build());
			LOGGER.debug("[TVDB] Calling url {}", httpget.getURI().toString());
			CloseableHttpResponse response = httpclient.execute(httpget);
			LOGGER.debug("[TVDB] Url response code = {}", response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() >= 300) {
				throw new HttpResponseException(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
			}
			try (InputStream inputStream = response.getEntity().getContent()) {
				LOGGER.debug("[TVDB] Processing archive result");
				Path zipFile = Files.createTempFile(null, null);
				Files.copy(inputStream, zipFile, StandardCopyOption.REPLACE_EXISTING);
				FileSystem zipFileSytem = FileSystems.newFileSystem(zipFile, null);
				Path showFile = zipFileSytem.getPath(lang + ".xml");
				LOGGER.debug("[TVDB] Show file retrieved in {}", showFile);
				try (InputStream in = Files.newInputStream(showFile)) {
					LOGGER.debug("[TVDB] Unmarshalling show file");
					return JAXB.unmarshal(in, ShowData.class);
				}
			}
		} catch (IOException | URISyntaxException e) {
			throw new DaoException("Error while querying TVDB url", e);
		}
	}

	@Override
	public List<Episode> getShowEpisodes(Long id, String lang) throws DaoException {
		return getShowData(id, lang).getEpisodes().stream().map(e -> TVDBConverter.convertFromTVDB(id, e)).collect(Collectors.toList());
	}
}
