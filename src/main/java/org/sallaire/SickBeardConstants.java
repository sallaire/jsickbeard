package org.sallaire;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SickBeardConstants {
	public static final Path APPLICATION_DIRECTORY = System.getProperty("sickbeard.home") != null ? Paths.get(System.getProperty("sickbeard.home")) : Paths.get(System.getProperty("user.home"), ".sickbeard");
}
