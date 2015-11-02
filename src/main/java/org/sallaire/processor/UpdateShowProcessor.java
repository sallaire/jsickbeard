package org.sallaire.processor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sallaire.converter.TVDBConverter;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.TvShowDao;
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

	@Scheduled(cron = "0 2 * * * *")
	public void updateShow() {
		Long lastUpdate = showDao.getLastUpdate();
		UpdateItems updateItems = null;

		if (lastUpdate == null) {
			lastUpdate = Instant.now().getEpochSecond();
		}
		try {
			updateItems = tvDbDao.getShowsToUpdate(Instant.now().getEpochSecond());
			showDao.saveLastUpdate(updateItems.getTime());
		} catch (DaoException e) {
			LOGGER.error("Unable to get update lists from tvdb API", e);
		}

		Map<Long, List<Episode>> epidodesByShow = showDao.getAllShowEpisodes();

		Long showId = null;
		for (Entry<Long, List<Episode>> entry : epidodesByShow.entrySet()) {
			showId = entry.getKey();
			List<Episode> currentEpisodes = entry.getValue();

			// Check if the show has been updated
			if (updateItems != null && updateItems.getShowIds() != null && updateItems.getShowIds().contains(showId)) {
				currentEpisodes = updateShow(showId, currentEpisodes);
			}

			// Now check if one or several episodes has to be set to wanted status
			final LocalDate now = LocalDate.now();
			currentEpisodes.stream().filter(e -> e.getAirDate().isBefore(now) && e.getStatus() == Status.UNAIRED).forEach(e -> e.setStatus(Status.WANTED));
			// TODO ajouter les épisodes recherchés à la liste de traitement
			showDao.saveShowEpisodes(showId, entry.getValue());
		}

	}

	private List<Episode> updateShow(final Long id, List<Episode> currentEpisodes) {
		try {
			ShowData showData = tvDbDao.getShowInformation(id, "fr");
			showDao.saveShow(TVDBConverter.convertFromTVDB(showData.getShowInfo()));

			List<Episode> updatedEpisodes = showData.getEpisodes().stream().map(e -> TVDBConverter.convertFromTVDB(id, e)).collect(Collectors.toList());
			Map<Long, Episode> currentEpisodesById = currentEpisodes.stream().collect(Collectors.toMap(Episode::getId, Function.identity()));
			updatedEpisodes.stream().forEach(e -> {
				if (currentEpisodesById.containsKey(e.getId())) {
					Episode currentEpisode = currentEpisodesById.get(e.getId());
					e.setDownloadDate(currentEpisode.getDownloadDate());
					e.setStatus(currentEpisode.getStatus());
					e.setFileName(currentEpisode.getFileName());
				} else {
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
