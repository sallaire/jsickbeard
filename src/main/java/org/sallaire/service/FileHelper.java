package org.sallaire.service;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileHelper {
	public static class Finder extends SimpleFileVisitor<Path> {

		private String name;
		private boolean found = false;

		public Finder(String name) {
			this.name = name;
		}

		// Compares the glob pattern against
		// the file or directory name.
		boolean find(Path file) {
			Path name = file.getFileName();
			return name != null && this.name.equals(name.toString());
		}

		// Invoke the pattern matching
		// method on each file.
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			if (find(file)) {
				found = true;
				return FileVisitResult.TERMINATE;
			} else {
				return FileVisitResult.CONTINUE;
			}
		}

		public boolean isFound() {
			return found;
		}
	}
}
