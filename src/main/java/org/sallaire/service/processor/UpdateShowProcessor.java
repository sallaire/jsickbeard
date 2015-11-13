package org.sallaire.service.processor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dao.metadata.TVDBConverter;
import org.sallaire.dao.metadata.TVDBDao;
import org.sallaire.dto.Episode;
import org.sallaire.dto.Episode.Status;
import org.sallaire.dto.tvdb.ShowData;
import org.sallaire.dto.tvdb.UpdateItems;
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
	private TVDBDao tvDbDao;

	@Scheduled(cron = "0 0 2 * * *")
	public void updateShow() {
		Long lastUpdate = showDao.getLastUpdate();
		LOGGER.info("Starting update show processor, last update = {}", lastUpdate);
		UpdateItems updateItems = null;

		if (lastUpdate == null) {
			lastUpdate = Instant.now().getEpochSecond();
		}
		try {
			updateItems = tvDbDao.getShowsToUpdate(lastUpdate);
			showDao.saveLastUpdate(updateItems.getTime());
		} catch (DaoException e) {
			LOGGER.error("Unable to get update lists from tvdb API", e);
		}

		Map<Long, List<Episode>> epidodesByShow = showDao.getAllShowEpisodes();
		LOGGER.debug("{} show to process", epidodesByShow.size());

		Long showId = null;
		for (Entry<Long, List<Episode>> entry : epidodesByShow.entrySet()) {
			showId = entry.getKey();
			List<Episode> currentEpisodes = entry.getValue();
			LOGGER.debug("processing show {} with {} episodes", showId, currentEpisodes.size());

			// Check if the show has been updated
			if (updateItems != null && updateItems.getShowIds() != null && updateItems.getShowIds().contains(showId)) {
				LOGGER.debug("show {} has to be refreshed, ", showId);
				currentEpisodes = updateShow(showId, currentEpisodes);
			}

			// Now check if one or several episodes has to be set to wanted status
			final LocalDate now = LocalDate.now();
			LOGGER.debug("Checking if episodes has been aired and set hem to wanted");
			currentEpisodes.stream().filter(e -> e.getAirDate().isBefore(now) && e.getStatus() == Status.UNAIRED).forEach(e -> {
				LOGGER.debug("Epsiode S{}E{} has been aired ({}) and is set to wanted", e.getSeason(), e.getEpisode(), e.getAirDate());
				e.setStatus(Status.WANTED);
				showDao.saveWantedEpisode(e);
			});
			showDao.saveShowEpisodes(showId, entry.getValue());
		}
		LOGGER.info("Update show processor done");
	}

	private List<Episode> updateShow(final Long id, List<Episode> currentEpisodes) {
		try {
			LOGGER.debug("Retrieve show informations");
			ShowData showData = tvDbDao.getShowInformation(id, "fr");
			showDao.saveShow(TVDBConverter.convertFromTVDB(showData.getShowInfo()));

			LOGGER.debug("Process {} episodes for show {}", showData.getEpisodes().size(), showData.getShowInfo().getName());
			List<Episode> updatedEpisodes = showData.getEpisodes().stream().map(e -> TVDBConverter.convertFromTVDB(id, e)).collect(Collectors.toList());
			Map<Long, Episode> currentEpisodesById = currentEpisodes.stream().collect(Collectors.toMap(Episode::getId, Function.identity()));
			updatedEpisodes.stream().forEach(e -> {
				if (currentEpisodesById.containsKey(e.getId())) {
					Episode currentEpisode = currentEpisodesById.get(e.getId());
					e.setDownloadDate(currentEpisode.getDownloadDate());
					e.setStatus(currentEpisode.getStatus());
					e.setFileNames(currentEpisode.getFileNames());
				} else {
					LOGGER.debug("new epsiode found (S{}E{}), set status to unaired", e.getSeason(), e.getEpisode());
					e.setStatus(Status.UNAIRED);
				}
			});

			// Don't update episode now, it will be done after status wanted check
			return updatedEpisodes;
		} catch (DaoException e) {
			LOGGER.error("Unbale to get show [{}] informations", id, e);
		}
		return currentEpisodes;
	}
}
