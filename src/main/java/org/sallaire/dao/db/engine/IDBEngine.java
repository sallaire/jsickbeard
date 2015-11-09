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
	static final String WANTED_EPISODE = "wantedEpisode";
	static final String SNATCHED_EPISODE = "snatchedEpisode";
	static final String UPDATE_TIME = "updateTime";
	static final String PROVIDER_CONFIGURATION = "providerConfiguration";
	static final String CLIENT_CONFIGURATION = "clientConfiguration";

	<T> void store(String collection, Long id, T value);

	<T> void store(String collection, String id, T value);

	<T> T get(String collection, Long id);

	<T> T get(String collection, String id);

	<T> Collection<T> getValues(String collection);

	<T> Map<Long, T> getAll(String collection);

	void remove(String collection, Long id);

	public static IDBEngine getDbEngine() {
		return new MapDB();
	}
}
