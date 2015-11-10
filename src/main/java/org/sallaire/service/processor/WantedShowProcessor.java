package org.sallaire.service.processor;

import java.util.Collection;

import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dto.Episode;
import org.sallaire.service.TorrentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WantedShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(WantedShowProcessor.class);

	@Autowired
	private TvShowDao showDao;

	@Autowired
	private TorrentService torrentService;

	@Scheduled(cron = "0 * * * * *")
	public void updateShow() {
		Collection<Episode> episodes = showDao.getWantedEpisodes();
		for (Episode episode : episodes) {
			LOGGER.debug("Try to retrieve episode {}", episode);
			if (torrentService.searchAndGetEpisode(episode)) {
				LOGGER.debug("Episode found, remove it from list of wanted episodes", episode);
				showDao.removeWantedEpisode(episode.getId());
			}
		}
	}

}
