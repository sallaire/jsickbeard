package org.sallaire.service;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHelper {
	public static class Finder extends SimpleFileVisitor<Path> {

		private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

		private String name;
		private boolean found = false;

		public Finder(String name) {
			this.name = name;
		}

		// Compares the glob pattern against
		// the file or directory name.
		boolean find(Path file) {
			String name = FilenameUtils.removeExtension(file.getFileName().toString());
			LOGGER.trace("Test [{}] against [{}]", name, this.name);
			return this.name.equals(name);
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
