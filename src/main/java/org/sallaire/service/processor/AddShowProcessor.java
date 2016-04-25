package org.sallaire.service.processor;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.sallaire.JackBeardConstants;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.DownloadDao;
import org.sallaire.dao.db.TvShowDao;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AddShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddShowProcessor.class);

	private final BlockingQueue<Entry<Long, Status>> queue = new LinkedBlockingQueue<>();

	@Autowired
	private IMetaDataDao metaDataDao;

	@Autowired
	private TvShowDao showDao;

	@Autowired
	private DownloadDao downloadDao;

	@Async
	public void startShowProcessor() {
		Entry<Long, Status> entry;
		Long showId = null;
		Status initialStatus = null;
		while (true) {
			try {
				entry = queue.take();

				showId = entry.getKey();
				initialStatus = entry.getValue();
				processShow(showId, initialStatus);

			} catch (InterruptedException e) {
				LOGGER.error("Error while retrieving show from queue", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	private void processShow(Long showId, Status initialStatus) {
		try {
			LOGGER.debug("Retrieving show configuration");
			TvShowConfiguration showConfiguration = downloadDao.getShowConfiguration(showId);
			if (showConfiguration != null) {
				LOGGER.info("Adding show [{}] with initial status [{}]", showId, initialStatus);
				Collection<Episode> episodes = showDao.getShowEpisodes(showId);
				if (episodes == null) {
					LOGGER.debug("Processing show generic data");
					TvShow tvShow = metaDataDao.getShowInformation(showId, JackBeardConstants.LANG);
					LOGGER.debug("Storing show generic data to db");
					showDao.saveShow(tvShow);
					LOGGER.debug("Show generic data stored to db");

					LOGGER.debug("Storing show episode data to db");
					episodes = metaDataDao.getShowEpisodes(showId, "fr");
					showDao.saveShowEpisodes(showId, episodes);
					LOGGER.debug("Show episode data stored to db");

				} else {
					LOGGER.debug("Show generic data are already in db, skip to episodes status");

				}
				LOGGER.debug("Processing episodes status");
				processEpisodesStatus(showConfiguration, episodes, initialStatus);
				LOGGER.debug("Episodes status processed");

				LOGGER.info("Show [{}] processed successfully", showId);
			} else {
				LOGGER.warn("No show configuration found for show {}, it will not be added", showId);
			}
		} catch (DaoException e) {
			LOGGER.error("Unable to get show informations for id [{}], show will not be added in db", showId, e);
		}
	}

	private void processEpisodesStatus(TvShowConfiguration showConfig, Collection<Episode> episodes, Status initialStatus) {
		LOGGER.debug("Processing {} episodes user data", episodes != null ? episodes.size() : 0);
		episodes.stream().forEach(e -> {
			EpisodeStatus epStatus = new EpisodeStatus();
			EpisodeKey epKey = new EpisodeKey();
			epStatus.setEpisodeKey(epKey);
			epKey.setLang(showConfig.getAudioLang());
			epKey.setQuality(showConfig.getQuality());
			epKey.setShowId(showConfig.getId());
			epKey.setSeason(e.getSeason());
			epKey.setNumber(e.getEpisode());
			if (e.getAirDate().isAfter(LocalDate.now())) {
				epStatus.setStatus(Status.UNAIRED);
			} else {
				epStatus.setStatus(initialStatus);
			}
			LOGGER.debug("Adding Episode status {} for episode S{}E{} and show {}", epStatus.getStatus(), epKey.getSeason(), epKey.getNumber(), showConfig.getId());
			downloadDao.saveEpisodeStatus(epStatus);
		});

	}

	public void addShow(Long id, Status initialStatus) {
		queue.add(new AbstractMap.SimpleEntry<Long, Status>(id, initialStatus));
	}

}
