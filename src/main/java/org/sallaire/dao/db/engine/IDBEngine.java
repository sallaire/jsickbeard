package org.sallaire.dao.db.engine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

import org.sallaire.SickBeardConstants;

public interface IDBEngine {

	static final Path DB_LOCATION = Paths.get(SickBeardConstants.APPLICATION_DIRECTORY.toString(), "sickbeard.db");

	static final String SHOW_CONFIGURATION = "showConfiguration";
	static final String SHOW = "show";
	static final String EPISODE = "episode";
	static final String EPISODE_STATUS = "episodeStatus";
	static final String WANTED_EPISODE = "wantedEpisode";
	static final String DOWNLOADED_EPISODE = "downloadedEpisode";
	static final String SNATCHED_EPISODE = "snatchedEpisode";
	static final String UPDATE_TIME = "updateTime";
	static final String PROVIDER_CONFIGURATION = "providerConfiguration";
	static final String CLIENT_CONFIGURATION = "clientConfiguration";
	static final String ACCOUNT = "account";

	<T> void store(String collection, Long id, T value);

	<T> void store(String collection, String id, T value);

	<K, V> void store(String collection, K id, V value);

	<T> T get(String collection, Long id);

	<T> T get(String collection, String id);

	<K, T> T get(String collection, K id);

	<T> Collection<T> getValues(String collection);

	<K, V> Map<K, V> getAll(String collection);

	<K> void remove(String collection, K id);

	void remove(String collection, Long id);

	void remove(String collection, String id);

	public void drop(String collection);

	public static IDBEngine getDbEngine() {
		return new MapDB();
	}
}
