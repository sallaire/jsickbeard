package org.sallaire.service.client;

import java.io.IOException;

import org.sallaire.dto.ClientConfiguration;
import org.sallaire.dto.Episode;
import org.sallaire.dto.TvShowConfiguration;
import org.sallaire.service.provider.Torrent;

public interface IClient {
	void addTorrent(Torrent torrent, TvShowConfiguration showConfiguration, Episode episode) throws IOException;

	String getId();

	void configurationChanged(ClientConfiguration parameters);
}
