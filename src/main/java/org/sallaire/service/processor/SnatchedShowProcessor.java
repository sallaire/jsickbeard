package org.sallaire.service.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;

import org.sallaire.dao.db.DownloadDao;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dto.metadata.TvShow;
import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.dto.user.Status;
import org.sallaire.dto.user.TvShowConfiguration;
import org.sallaire.service.util.FileHelper.Finder;
import org.sallaire.service.util.RegexFilterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SnatchedShowProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnatchedShowProcessor.class);

    @Autowired
    private DownloadDao downloadDao;

    @Autowired
    private TvShowDao tvShowDao;

    @Autowired
    private RegexFilterConfiguration regexFilter;

    @Scheduled(cron = "0 15 * * * *")
    public void updateShow() {
	Collection<EpisodeStatus> episodes = downloadDao.getSnatchedEpisodes();
	LOGGER.info("Starting snatched show processor with {} snatched episodes", episodes.size());
	for (EpisodeStatus episode : episodes) {
	    LOGGER.debug("Search for snatched episode {}", episode);
	    try {
		if (searchFile(episode)) {
		    LOGGER.debug("Episode found, remove it from list of snatched episodes", episode);
		    episode.setDownloadDate(LocalDate.now());
		    episode.setStatus(Status.DOWNLOADED);
		    downloadDao.saveEpisodeStatus(episode);
		    downloadDao.saveDownloadedEpisode(episode);
		    downloadDao.removeSnatchedEpisode(episode.getEpisodeKey());
		}
	    } catch (IOException e) {
		LOGGER.error("Error while checking downloaded file for episode {}", episode);
	    }
	}
	LOGGER.info("Snatched show processor done");
    }

    private boolean searchFile(EpisodeStatus episode) throws IOException {
	if (episode.getFileNames() != null) {
	    TvShowConfiguration showConfig = downloadDao.getShowConfiguration(episode.getEpisodeKey().getShowId());
	    TvShow tvShow = tvShowDao.getShow(episode.getEpisodeKey().getShowId());
	    String location = showConfig.getLocation();
	    boolean filesFound = true;
	    LOGGER.debug("Try to find file in directory [{}]", location);
	    Finder finder = new Finder(regexFilter, tvShow.getName(), episode.getEpisodeKey());
	    Files.walkFileTree(Paths.get(location), finder);
	    filesFound &= finder.isFound();
	    return filesFound;
	} else {
	    LOGGER.warn("Episode {} is set to SNATCHED, but no file is associated", episode);
	    return false;
	}
    }
}
