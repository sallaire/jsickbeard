package org.sallaire.service.client;

import java.io.IOException;

import org.sallaire.dto.configuration.ClientConfiguration;
import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.dto.user.TvShowConfiguration;
import org.sallaire.service.provider.Torrent;

public interface IClient {
	void addTorrent(Torrent torrent, TvShowConfiguration showConfiguration, EpisodeStatus episode) throws IOException;

	String getId();

	void configurationChanged(ClientConfiguration parameters);
}
