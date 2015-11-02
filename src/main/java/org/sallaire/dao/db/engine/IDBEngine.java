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
	static final String UPDATE_TIME = "updateTime";

	<T> void store(String collection, Long id, T value);

	<T> T get(String collection, Long id);

	<T> Collection<T> getValues(String collection);

	<T> Map<Long, T> getAll(String collection);

	public static IDBEngine getDbEngine() {
		return new MapDB();
	}
}
