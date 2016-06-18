package org.sallaire.service.processor;

import java.time.LocalDate;
import java.util.Collection;

import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.EpisodeStatusRepository;
import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dao.metadata.IMetaDataDao;
import org.sallaire.dto.user.Status;
import org.sallaire.service.TvShowService;
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
	private TvShowService showService;

	@Autowired
	private EpisodeStatusRepository statusDao;

	@Autowired
	private IMetaDataDao metaDataDao;

	@Scheduled(cron = "0 0 2 * * *")
	public void updateShow() {
		LOGGER.info("Starting update show processor");
		Collection<TvShow> shows = showService.getActiveShows();
		LOGGER.debug("{} show to process", shows.size());

		Long showId = null;
		for (TvShow tvShow : shows) {
			showId = tvShow.getId();
			LOGGER.debug("processing show {}", showId);

			// Check if the show has been updated
			try {
				if (metaDataDao.hasShowUpdates(showId)) {
					LOGGER.debug("show {} has to be refreshed, ", showId);
					showService.updateShow(tvShow);
					for (TvShowConfiguration tvShowConfiguration : tvShow.getConfigurations()) {
						showService.processEpisodesStatus(tvShowConfiguration);
					}
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

}
