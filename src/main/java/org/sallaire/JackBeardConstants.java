package org.sallaire;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JackBeardConstants {
	public static final Path APPLICATION_DIRECTORY = System.getProperty("jackbeard.home") != null ? Paths.get(System.getProperty("jackbeard.home")) : Paths.get(System.getProperty("user.home"), ".jackbeard");
	public static final Path DOWNLAD_DIRECTORY = Paths.get(System.getProperty("user.home"), "content", "series");
}
