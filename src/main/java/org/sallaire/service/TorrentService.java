package org.sallaire.service;

import java.io.IOException;
import java.util.List;

import org.sallaire.client.IClient;
import org.sallaire.dao.TvShowDao;
import org.sallaire.dto.Episode;
import org.sallaire.dto.Episode.Status;
import org.sallaire.dto.TvShow;
import org.sallaire.dto.TvShowConfiguration;
import org.sallaire.provider.IProvider;
import org.sallaire.provider.Torrent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TorrentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TorrentService.class);

	@Autowired
	private IClient client;

	@Autowired
	private List<IProvider> providers;

	@Autowired
	private TvShowDao showDao;

	public void searchAndGetEpisode(Episode episode) {
		TvShowConfiguration config = showDao.getShowConfiguration(episode.getShowId());
		TvShow show = showDao.getShow(episode.getShowId());
		Torrent torrent = null;
		for (IProvider provider : providers) {
			try {
				torrent = provider.findEpisode(show.getName(), config.getAudioLang(), episode.getSeason(), episode.getEpisode(), config.getQuality());
				if (torrent != null) {
					LOGGER.info("Episode [{}] found with provider [{}]", episode, provider.getClass().getSimpleName());
					break;
				}
			} catch (IOException e) {
				LOGGER.error("Error while searching episode [{}] with provider [{}]", episode, provider.getClass().getSimpleName(), e);
			}
		}

		if (torrent != null) {
			LOGGER.info("Sendind torrent to client [{}]", client.getClass().getSimpleName());
			try {
				client.addTorrent(torrent.getPath(), torrent.getName());

				// Update episode status to snatched
				episode.setStatus(Status.SNATCHED);
				List<Episode> episodes = showDao.getShowEpisodes(episode.getShowId());
				episodes.stream().filter(e -> e.getId().equals(episode.getShowId())).forEach(e -> e.setStatus(Status.SNATCHED));
				showDao.saveShowEpisodes(episode.getShowId(), episodes);
			} catch (IOException e) {
				LOGGER.error("Unable to send torrent to client", e);
			}
		}
	}
}
