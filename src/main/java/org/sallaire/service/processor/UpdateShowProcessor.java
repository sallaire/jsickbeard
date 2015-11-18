package org.sallaire.service.processor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dao.db.UserDao;
import org.sallaire.dao.metadata.IMetaDataDao;
import org.sallaire.dto.metadata.Episode;
import org.sallaire.dto.metadata.TvShow;
import org.sallaire.dto.user.EpisodeKey;
import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.dto.user.Status;
import org.sallaire.dto.user.TvShowConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateShowProcessor.class);

	@Autowired
	private TvShowDao showDao;

	@Autowired
	private IMetaDataDao metaDataDao;

	@Autowired
	private UserDao userDao;

	@Scheduled(cron = "0 0 2 * * *")
	public void updateShow() {
		Long lastUpdate = showDao.getLastUpdate();
		LOGGER.info("Starting update show processor, last update = {}", lastUpdate);
		List<Long> updateItems = new ArrayList<>();

		if (lastUpdate == null) {
			lastUpdate = Instant.now().getEpochSecond();
		}
		try {
			Long newUpdateTime = Instant.now().getEpochSecond();
			updateItems = metaDataDao.getShowsToUpdate(lastUpdate);
			showDao.saveLastUpdate(newUpdateTime);
		} catch (DaoException e) {
			LOGGER.error("Unable to get update lists from tvdb API", e);
		}

		Collection<TvShow> shows = showDao.getShows();
		LOGGER.debug("{} show to process", shows.size());

		Long showId = null;
		for (TvShow show : shows) {
			showId = show.getId();
			LOGGER.debug("processing show {}", showId);

			LOGGER.debug("Retrieve show configuration");
			TvShowConfiguration showConfig = userDao.getShowConfiguration(showId);
			LOGGER.debug("Show configuration retrieved");

			// Check if the show has been updated
			if (updateItems.contains(showId)) {
				LOGGER.debug("show {} has to be refreshed, ", showId);
				updateShow(showConfig);
			}

			// Now check if one or several episodes has to be set to wanted status
			Collection<Episode> episodes = showDao.getShowEpisodes(showId);
			final LocalDate now = LocalDate.now();
			LOGGER.debug("Checking if episodes has been aired and set them to wanted");
			episodes.stream().filter(e -> e.getAirDate().isBefore(now)).forEach(e -> {
				EpisodeKey epKey = new EpisodeKey(showConfig, e);
				EpisodeStatus epStatus = userDao.getEpisodeStatus(epKey);
				if (epStatus.getStatus() == Status.UNAIRED) {
					LOGGER.debug("Epsiode S{}E{} has been aired ({}) and is set to wanted", e.getSeason(), e.getEpisode(), e.getAirDate());
					epStatus.setStatus(Status.WANTED);
					userDao.saveEpisodeStatus(epStatus);
					userDao.saveWantedEpisode(epStatus);
				}
			});
		}
		LOGGER.info("Update show processor done");
	}

	private void updateShow(TvShowConfiguration showConfig) {
		try {

			LOGGER.debug("Retrieve show informations");
			TvShow tvShow = metaDataDao.getShowInformation(showConfig.getId(), "fr");
			showDao.saveShow(metaDataDao.getShowInformation(showConfig.getId(), "fr"));
			LOGGER.debug("Show informations saved");

			LOGGER.debug("Retrieve show episodes");
			List<Episode> updatedEpisodes = metaDataDao.getShowEpisodes(showConfig.getId(), "fr");
			showDao.saveShowEpisodes(showConfig.getId(), updatedEpisodes);
			LOGGER.debug("Process {} episodes for show {}", updatedEpisodes.size(), tvShow.getName());

			updatedEpisodes.stream().forEach(e -> {
				EpisodeKey epKey = new EpisodeKey(showConfig, e);
				if (userDao.getEpisodeStatus(epKey) == null) {
					LOGGER.debug("New episode {}-{} found, add it to status episode", e.getSeason(), e.getEpisode());
					EpisodeStatus epStatus = new EpisodeStatus(epKey, Status.UNAIRED);
					userDao.saveEpisodeStatus(epStatus);
				}
			});
		} catch (DaoException e) {
			LOGGER.error("Unable to get show [{}] informations", showConfig.getId(), e);
		}
	}
}
