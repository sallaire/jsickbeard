package org.sallaire.service;

import java.util.List;

import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dao.metadata.TVDBDao;
import org.sallaire.dto.Episode.Status;
import org.sallaire.dto.TvShowConfiguration;
import org.sallaire.dto.TvShowConfiguration.Quality;
import org.sallaire.dto.tvdb.ISearchResult;
import org.sallaire.service.processor.AddShowProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShowService.class);

	@Autowired
	private TVDBDao tvdbDao;

	@Autowired
	private TvShowDao showDao;

	@Autowired
	AddShowProcessor showProcessor;

	public void add(Long id, String location, String initalStatus, String quality, String audioLang) {
		LOGGER.debug("Process show configuration for show {}", id);
		TvShowConfiguration configuration = new TvShowConfiguration();
		configuration.setId(id);
		Quality convertedQuality = Quality.SD;
		try {
			convertedQuality = Quality.valueOf(quality);
		} catch (IllegalArgumentException e) {
			LOGGER.warn("Check of quality {} fails, default quality {} will be set", quality, Quality.SD, e);
		}
		configuration.setQuality(convertedQuality);
		configuration.setAudioLang(audioLang);
		Status convertedInitialStatus = Status.SKIPPED;
		try {
			convertedInitialStatus = Status.valueOf(initalStatus);
		} catch (IllegalArgumentException e) {
			LOGGER.warn("Check of inital status {} fails, default status {} will be set", initalStatus, Status.SKIPPED, e);
		}
		LOGGER.debug("Store show configuration", id);
		showDao.saveShowConfiguration(id, configuration);
		LOGGER.debug("Show configuration stored", id);

		LOGGER.info("Adding show to AddShow queue");
		showProcessor.addShow(id, convertedInitialStatus);
	}

	public List<? extends ISearchResult> search(String name, String lang) {
		try {
			return tvdbDao.searchForShows(name, lang);
		} catch (DaoException e) {
			LOGGER.error("Unable to search show from tvdb", e);
			return null;
		}
	}
}
