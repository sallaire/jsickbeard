package org.sallaire.service.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.sallaire.dao.db.EpisodeStatusRepository;
import org.sallaire.dao.db.entity.EpisodeStatus;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dto.user.Status;
import org.sallaire.service.util.FileHelper.Finder;
import org.sallaire.service.util.RegexFilterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class SnatchedShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SnatchedShowProcessor.class);

	@Autowired
	private RegexFilterConfiguration regexFilter;

	@Autowired
	private EpisodeStatusRepository episodeStatusDao;

	@Scheduled(cron = "0 15 * * * *")
	public void updateShow() {
		Collection<EpisodeStatus> episodes = episodeStatusDao.findByStatus(Status.SNATCHED);
		LOGGER.info("Starting snatched show processor with {} snatched episodes", episodes.size());
		for (EpisodeStatus episode : episodes) {
			LOGGER.debug("Search for snatched episode {}", episode);
			try {
				if (searchFile(episode)) {
					LOGGER.debug("Episode found, remove it from list of snatched episodes", episode);
					episode.setDownloadDate(LocalDateTime.now());
					episode.setStatus(Status.DOWNLOADED);
					episodeStatusDao.save(episode);
				}
			} catch (IOException e) {
				LOGGER.error("Error while checking downloaded file for episode {}", episode);
			}
		}
		LOGGER.info("Snatched show processor done");
	}

	private boolean searchFile(EpisodeStatus episode) throws IOException {
		if (episode.getDownloadedFiles() != null) {
			TvShowConfiguration showConfig = episode.getShowConfiguration();
			TvShow tvShow = showConfig.getTvShow();
			String location = showConfig.getLocation();
			boolean filesFound = true;
			LOGGER.debug("Try to find file in directory [{}]", location);
			Finder finder = null;
			if (CollectionUtils.isNotEmpty(tvShow.getCustomNames())) {
				finder = new Finder(regexFilter, episode.getEpisode().getSeason(), episode.getEpisode().getEpisode(), showConfig.getAudioLang(), showConfig.getQuality(), tvShow.getCustomNames().toArray(new String[tvShow.getCustomNames().size()]));
			} else {
				finder = new Finder(regexFilter, episode.getEpisode().getSeason(), episode.getEpisode().getEpisode(), showConfig.getAudioLang(), showConfig.getQuality(), tvShow.getOriginalName());
			}
			Files.walkFileTree(Paths.get(location), finder);
			filesFound &= finder.isFound();
			return filesFound;
		} else {
			LOGGER.warn("Episode {} is set to SNATCHED, but no file is associated", episode);
			return false;
		}
	}
}
