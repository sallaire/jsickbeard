package org.sallaire.service.processor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.sallaire.dao.db.UserDao;
import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.service.DownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WantedShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(WantedShowProcessor.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private DownloadService torrentService;

	@Scheduled(cron = "0 0 * * * *")
	public void process() {
		Collection<EpisodeStatus> episodes = userDao.getWantedEpisodes();
		LOGGER.info("Starting wanted show processor with {} wanted episodes", episodes.size());
		findEpisodes(episodes);
		LOGGER.info("Wanted show processor done");
	}

	@Async
	public void process(List<EpisodeStatus> episodes) {
		findEpisodes(episodes);
	}

	public void process(EpisodeStatus episode) {
		findEpisodes(Arrays.asList(episode));
		// TODO renvoyer un boolean pour savoir si l'épisode a été trouvé ?
	}

	private synchronized void findEpisodes(Collection<EpisodeStatus> episodes) {
		for (EpisodeStatus episode : episodes) {
			LOGGER.debug("Try to retrieve episode {}", episode);
			if (torrentService.searchAndGetEpisode(episode)) {
				LOGGER.debug("Episode {} found", episode);
				userDao.removeWantedEpisode(episode);
			} else {
				userDao.saveWantedEpisode(episode);
			}
		}
	}

}
