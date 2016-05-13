package org.sallaire.service.processor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.tuple.Triple;
import org.sallaire.JackBeardConstants;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.EpisodeRepository;
import org.sallaire.dao.db.EpisodeStatusRepository;
import org.sallaire.dao.db.TvShowRepository;
import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dao.db.entity.EpisodeStatus;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dao.metadata.IMetaDataDao;
import org.sallaire.dto.user.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AddShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddShowProcessor.class);

	private final BlockingQueue<Triple<Long, TvShowConfiguration, Status>> queue = new LinkedBlockingQueue<>();

	@Autowired
	private TvShowRepository tvShowDao;

	@Autowired
	private EpisodeRepository episodeDao;

	@Autowired
	private EpisodeStatusRepository episodeStatusDao;

	@Autowired
	private IMetaDataDao metaDataDao;

	@Async
	public void startShowProcessor() {
		Triple<Long, TvShowConfiguration, Status> entry;
		Long showId = null;
		TvShowConfiguration showConfiguration = null;
		Status initialStatus = null;
		while (true) {
			try {
				entry = queue.take();

				showId = entry.getLeft();
				showConfiguration = entry.getMiddle();
				initialStatus = entry.getRight();
				if (initialStatus == null) {
					LOGGER.warn("Initial status is not set for show {} and configuration {}, it will be set to {} by default", showId, showConfiguration.toString(), Status.SKIPPED);
					initialStatus = Status.SKIPPED;
				}
				processShow(showId, showConfiguration, initialStatus);

			} catch (InterruptedException e) {
				LOGGER.error("Error while retrieving show from queue", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	private void processShow(Long showId, TvShowConfiguration showConfiguration, Status initialStatus) {
		try {
			LOGGER.info("Adding show [{}] with initial status [{}]", showId, initialStatus);
			LOGGER.debug("Processing show generic data");
			TvShow tvShow = metaDataDao.getShowInformation(showId, JackBeardConstants.LANG);
			tvShow.setConfigurations(new HashSet<>());
			tvShow.getConfigurations().add(showConfiguration);
			LOGGER.debug("Storing show generic data to db");
			tvShowDao.save(tvShow);
			LOGGER.debug("Show generic data stored to db");

			LOGGER.debug("Storing show episode data to db");
			List<Episode> episodes = metaDataDao.getShowEpisodes(tvShow, "fr");
			episodeDao.save(episodes);
			LOGGER.debug("Show episode data stored to db");

			LOGGER.debug("Processing episodes status");
			processEpisodesStatus(tvShow.getId(), showConfiguration, initialStatus);
			LOGGER.debug("Episodes status processed");

			LOGGER.info("Show [{}] processed successfully", showId);
		} catch (DaoException e) {
			LOGGER.error("Unable to get show informations for id [{}], show will not be added in db", showConfiguration.getTvShow().getId(), e);
		}

	}

	public void processEpisodesStatus(Long showDbId, TvShowConfiguration tvShowConfiguration, Status initialStatus) {
		TvShow tvShow = tvShowDao.findOne(showDbId);
		LOGGER.debug("Processing {} episodes user data", tvShow.getEpisodes().size());
		List<EpisodeStatus> statuses = new ArrayList<>();
		tvShow.getEpisodes().forEach(e -> {
			EpisodeStatus epStatus = new EpisodeStatus();
			epStatus.setEpisode(e);
			epStatus.setShowConfiguration(tvShowConfiguration);
			if (e.getAirDate().isAfter(LocalDate.now())) {
				epStatus.setStatus(Status.UNAIRED);
			} else {
				epStatus.setStatus(initialStatus);
			}
			statuses.add(epStatus);
		});
		episodeStatusDao.save(statuses);
	}

	public void addShow(Long showId, TvShowConfiguration tvShowConfiguration, Status initialStatus) {
		queue.add(Triple.of(showId, tvShowConfiguration, initialStatus));
	}

}
