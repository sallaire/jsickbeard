package org.sallaire.service.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dto.Episode;
import org.sallaire.dto.TvShowConfiguration;
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
	private TvShowDao showDao;

	@Scheduled(cron = "15 * * * * *")
	public void updateShow() {
		// Collection<Episode> episodes = showDao.getWantedEpisodes();
		// for (Episode episode : episodes) {
		// LOGGER.debug("Search for snatched episode {}", episode);
		// try {
		// if (searchFile(episode)) {
		// LOGGER.debug("Episode found, remove it from list of snatched episodes", episode);
		// episode.setDownloadDate(LocalDate.now());
		// episode.setStatus(Status.DOWNLOADED);
		// showDao.saveShowEpisode(episode);
		// showDao.removeSnatchedEpisode(episode.getId());
		// }
		// } catch (IOException e) {
		// LOGGER.error("Error while checking downloaded file for episode {}", episode);
		// }
		// }
	}

	private boolean searchFile(Episode episode) throws IOException {
		TvShowConfiguration showConfig = showDao.getShowConfiguration(episode.getShowId());
		String location = showConfig.getLocation();
		boolean filesFound = true;
		for (String fileName : episode.getFileNames()) {
			Finder finder = new Finder(fileName);
			Files.walkFileTree(Paths.get(location), finder);
			filesFound &= finder.isFound();
		}
		return filesFound;
	}
}
