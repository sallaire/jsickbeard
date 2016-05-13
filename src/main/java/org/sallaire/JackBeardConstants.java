package org.sallaire;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JackBeardConstants {

	public static final Path DOWNLAD_DIRECTORY = Paths.get(System.getProperty("user.home"), "content", "series");

	public static final String LANG = System.getProperty("jackbeard.lang") != null ? System.getProperty("jackbeard.lang") : "fr";

}
