package org.sallaire.service.provider;

import java.nio.file.Path;

public class Torrent {
	private String name;
	private Path path;

	public Torrent(String name, Path path) {
		super();
		this.name = name;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
}
