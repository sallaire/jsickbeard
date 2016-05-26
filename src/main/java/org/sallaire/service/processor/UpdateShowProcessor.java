package org.sallaire.service.processor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sallaire.JackBeardConstants;
import org.sallaire.dao.DaoException;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UpdateShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateShowProcessor.class);

	@Autowired
	private TvShowRepository showDao;

	@Autowired
	private EpisodeStatusRepository statusDao;

	@Autowired
	private IMetaDataDao metaDataDao;

	@Scheduled(cron = "0 0 2 * * *")
	public void updateShow() {
		LOGGER.info("Starting update show processor");
		Iterable<TvShow> shows = showDao.findAll();
		LOGGER.debug("{} show to process", showDao.count());

		Long showId = null;
		for (TvShow tvShow : shows) {
			showId = tvShow.getId();
			LOGGER.debug("processing show {}", showId);

			// Check if the show has been updated
			try {
				if (metaDataDao.hasShowUpdates(showId)) {
					LOGGER.debug("show {} has to be refreshed, ", showId);
					updateShow(tvShow);
				}
			} catch (DaoException e) {
				LOGGER.error("Unable to get show [{}] update informations, it will be ignored", showId, e);
			}

			// Now check if one or several episodes has to be set to wanted
			// status
			Collection<Episode> episodes = tvShow.getEpisodes();
			final LocalDate now = LocalDate.now();
			LOGGER.debug("Checking if episodes has been aired and set them to wanted");
			episodes.stream().filter(e -> e.getAirDate().isBefore(now)).forEach(e -> {
				e.getStatus().forEach(s -> {
					if (s.getStatus() == Status.UNAIRED) {
						LOGGER.debug("Epsiode S{}E{} has been aired ({}) and is set to wanted", e.getSeason(), e.getEpisode(), e.getAirDate());
						s.setStatus(Status.WANTED);
						statusDao.save(s);
					}
				});
			});
		}
		LOGGER.info("Update show processor done");
	}

	private void updateShow(TvShow tvShow) {
		try {

			LOGGER.debug("Retrieve show informations");
			TvShow updatedTvShow = metaDataDao.getShowInformation(tvShow.getId(), JackBeardConstants.LANG);
			tvShow.fromShow(updatedTvShow);

			LOGGER.debug("Retrieve show episodes");
			List<Episode> updatedEpisodes = metaDataDao.getShowEpisodes(tvShow, JackBeardConstants.LANG);
			LOGGER.debug("Process {} episodes for show {}", updatedEpisodes.size(), tvShow.getName());
			Map<Long, Episode> episodeById = tvShow.getEpisodes().stream().collect(Collectors.toMap(Episode::getId, Function.identity()));
			updatedEpisodes.stream().forEach(e -> {
				if (episodeById.containsKey(e.getId())) {
					LOGGER.debug("Update existing episode {}-{}", e.getSeason(), e.getEpisode());
					episodeById.get(e.getId()).fromEpisode(e);
				} else {
					LOGGER.debug("New episode {}-{} found, add it to status episode", e.getSeason(), e.getEpisode());
					for (TvShowConfiguration config : tvShow.getConfigurations()) {
						EpisodeStatus episodeStatus = new EpisodeStatus();
						episodeStatus.setEpisode(e);
						episodeStatus.setShowConfiguration(config);
						episodeStatus.setStatus(Status.UNAIRED);
						e.getStatus().add(episodeStatus);
					}
				}
			});
			showDao.save(tvShow);
			LOGGER.debug("Show informations saved");
		} catch (DaoException e) {
			LOGGER.error("Unable to get show [{}] informations", tvShow.getId(), e);
		}
	}
}
