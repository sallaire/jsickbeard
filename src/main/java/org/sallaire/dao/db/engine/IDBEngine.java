package org.sallaire.dao.db.engine;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.sallaire.SickBeardConstants;

public interface IDBEngine {

	static final Path DB_LOCATION = Paths.get(SickBeardConstants.APPLICATION_DIRECTORY.toString(), "sickbeard.db");

	static final String SHOW_CONFIGURATION = "showConfiguration";
	static final String SHOW = "show";
	static final String EPISODE = "episode";

	<T> void store(String collection, Long id, T value);

	<T> T get(String collection, Long id);

	public static IDBEngine getDbEngine() {
		return new MapDB();
	}
}
