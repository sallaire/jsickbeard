package org.sallaire.client;

import java.io.IOException;
import java.nio.file.Path;

import org.sallaire.dto.ClientConfiguration;

public interface IClient {
	void addTorrent(Path torrentPath, String torrentName) throws IOException;

	String getId();

	void configurationChanged(ClientConfiguration parameters);
}
