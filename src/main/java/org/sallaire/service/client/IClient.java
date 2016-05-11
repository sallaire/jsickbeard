package org.sallaire.service.client;

import java.io.IOException;
import java.nio.file.Path;

import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dto.configuration.ClientConfiguration;
import org.sallaire.service.provider.Torrent;

public interface IClient {
	void addTorrent(Torrent torrent, Path location, Episode episode) throws IOException;

	String getId();

	void configurationChanged(ClientConfiguration parameters);
}
