package org.sallaire.service.util;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.FilenameUtils;
import org.sallaire.dto.user.EpisodeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHelper {
    public static class Finder extends SimpleFileVisitor<Path> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

	private String name;
	private EpisodeKey episodeKey;
	private RegexFilterConfiguration regexFilter;
	private boolean found = false;

	public Finder(RegexFilterConfiguration regexFilter, String name, EpisodeKey episodeKey) {
	    this.episodeKey = episodeKey;
	    this.name = name;
	    this.regexFilter = regexFilter;
	}

	// Compares the glob pattern against
	// the file or directory name.
	boolean find(Path file) {
	    String name = FilenameUtils.removeExtension(file.getFileName().toString());
	    return regexFilter.matchEpisode(name, this.name, episodeKey.getSeason(), episodeKey.getNumber(), episodeKey.getQuality(),
		    episodeKey.getLang());
	}

	// Invoke the pattern matching
	// method on each file.
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
	    LOGGER.debug("try [{}] file", file);
	    if (find(file)) {
		LOGGER.trace("file found !");
		found = true;
		return FileVisitResult.TERMINATE;
	    } else {
		LOGGER.trace("file not found, continue");
		return FileVisitResult.CONTINUE;
	    }
	}

	public boolean isFound() {
	    return found;
	}
    }
}
