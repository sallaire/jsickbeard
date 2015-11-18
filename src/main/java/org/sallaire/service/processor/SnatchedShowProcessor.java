package org.sallaire.service.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;

import org.sallaire.dao.db.UserDao;
import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.dto.user.Status;
import org.sallaire.dto.user.TvShowConfiguration;
import org.sallaire.service.FileHelper.Finder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SnatchedShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SnatchedShowProcessor.class);

	@Autowired
	private UserDao userDao;

	@Scheduled(cron = "0 15 * * * *")
	public void updateShow() {
		Collection<EpisodeStatus> episodes = userDao.getSnatchedEpisodes();
		LOGGER.info("Starting snatched show processor with {} snatched episodes", episodes.size());
		for (EpisodeStatus episode : episodes) {
			LOGGER.debug("Search for snatched episode {}", episode);
			try {
				if (searchFile(episode)) {
					LOGGER.debug("Episode found, remove it from list of snatched episodes", episode);
					episode.setDownloadDate(LocalDate.now());
					episode.setStatus(Status.DOWNLOADED);
					userDao.saveEpisodeStatus(episode);
					userDao.removeSnatchedEpisode(episode);
				}
			} catch (IOException e) {
				LOGGER.error("Error while checking downloaded file for episode {}", episode);
			}
		}
		LOGGER.info("Snatched show processor done");
	}

	private boolean searchFile(EpisodeStatus episode) throws IOException {
		if (episode.getFileNames() != null) {
			TvShowConfiguration showConfig = userDao.getShowConfiguration(episode.getEpisodeKey().getShowId());
			String location = showConfig.getLocation();
			boolean filesFound = true;
			for (String fileName : episode.getFileNames()) {
				Finder finder = new Finder(fileName);
				Files.walkFileTree(Paths.get(location), finder);
				filesFound &= finder.isFound();
			}
			return filesFound;
		} else {
			LOGGER.warn("Episode {} is set to SNATCHED, but no file is associated", episode);
			return false;
		}
	}
}
