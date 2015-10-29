package org.sallaire.processor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections4.CollectionUtils;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.engine.IDBEngine;
import org.sallaire.dao.metadata.TVDBDao;
import org.sallaire.dto.Episode;
import org.sallaire.dto.Episode.Status;
import org.sallaire.dto.TvShow;
import org.sallaire.dto.tvdb.EpisodeInfo;
import org.sallaire.dto.tvdb.ShowData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AddShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddShowProcessor.class);

	private static final DateTimeFormatter TIME_FORMAT_EN = DateTimeFormatter.ofPattern("hh:mm a");
	private static final DateTimeFormatter TIME_FORMAT_FR = DateTimeFormatter.ofPattern("HH:mm");

	private final BlockingQueue<Entry<Long, Status>> queue = new LinkedBlockingQueue<>();

	@Autowired
	private TVDBDao tvdbDao;

	@Autowired
	private IDBEngine dbEngine;

	@Async
	public void startShowProcessor() {
		Entry<Long, Status> entry;
		Long showId = null;
		Status initialStatus = null;
		while (true) {
			// Récupérer l'id de la serie
			try {
				entry = queue.take();

				showId = entry.getKey();
				initialStatus = entry.getValue();
				LOGGER.info("Adding show [{}] with initial status [{}]", showId, initialStatus);

				// Récupérer le zip sur tvdb
				// Parser le fichier xml des épisodes
				ShowData data = tvdbDao.getShowInformation(showId, "fr");
				LOGGER.debug("Show data retrieved from TVDB");

				// Stocker les infos génériques en bas de données
				LOGGER.debug("Processing show generic data");
				TvShow tvShow = new TvShow();
				tvShow.setId(data.getShowInfo().getId());
				tvShow.setImdbId(data.getShowInfo().getImdbId());
				tvShow.setName(data.getShowInfo().getName());
				tvShow.setDescription(data.getShowInfo().getDescription());
				tvShow.setNetwork(data.getShowInfo().getNetwork());
				tvShow.setGenre(data.getShowInfo().getGenre());
				if (data.getShowInfo().getAirDay() != null) {
					try {
						tvShow.setAirDay(DayOfWeek.valueOf(data.getShowInfo().getAirDay().toUpperCase()));
					} catch (IllegalArgumentException e) {
						LOGGER.warn("Unable to parse air day [{}]", data.getShowInfo().getAirDay(), e);
					}
				}
				tvShow.setRuntime(data.getShowInfo().getRuntime());
				tvShow.setBanner(data.getShowInfo().getBanner());
				tvShow.setPoster(data.getShowInfo().getPoster());
				tvShow.setFanart(data.getShowInfo().getFanart());
				tvShow.setLastUpdated(data.getShowInfo().getLastUpdated());
				tvShow.setStatus(data.getShowInfo().getStatus());
				if (data.getShowInfo().getFirstAired() != null) {
					try {
						tvShow.setFirstAired(LocalDate.parse(data.getShowInfo().getFirstAired()));
					} catch (DateTimeParseException e) {
						LOGGER.warn("Unable to parse first air date [{}]", data.getShowInfo().getFirstAired(), e);
					}
					try {
						tvShow.setAirTime(LocalTime.parse(data.getShowInfo().getAirTime(), TIME_FORMAT_EN));
					} catch (DateTimeParseException e) {
						LOGGER.debug("Unable to parse air time with EN format, try with FR");
						try {
							tvShow.setAirTime(LocalTime.parse(data.getShowInfo().getAirTime(), TIME_FORMAT_FR));
						} catch (DateTimeParseException e1) {
							LOGGER.warn("Unable to parse first air time [{}]", data.getShowInfo().getAirTime(), e1);
						}
					}
				}

				LOGGER.debug("Storing show generic data to db");
				dbEngine.store(IDBEngine.SHOW, tvShow.getId(), tvShow);
				LOGGER.debug("Show generic data stored to db");

				// Stocker les épisodes en base de données
				LOGGER.debug("Processing {} episodes data", data.getEpisodes() != null ? data.getEpisodes().size() : 0);
				List<Episode> dbEpisodes = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(data.getEpisodes())) {
					for (EpisodeInfo episodeInfo : data.getEpisodes()) {
						Episode dbEpisode = new Episode();
						dbEpisode.setId(episodeInfo.getId());
						dbEpisode.setImdbId(episodeInfo.getImdbId());
						dbEpisode.setShowId(showId);
						dbEpisode.setName(episodeInfo.getName());
						dbEpisode.setSeason(episodeInfo.getSeasonNumber());
						dbEpisode.setEpisode(episodeInfo.getEpisodeNumber());
						dbEpisode.setLastUpdated(episodeInfo.getLastUpdated());
						dbEpisode.setDescription(episodeInfo.getDescription());
						dbEpisode.setLang(episodeInfo.getLang());
						if (episodeInfo.getFirstAired() != null) {
							try {
								dbEpisode.setAirDate(LocalDate.parse(episodeInfo.getFirstAired()));
							} catch (DateTimeParseException e) {
								LOGGER.warn("Unable to parse episode air date [{}] for episode [{}-{}]", data.getShowInfo().getFirstAired(), episodeInfo.getSeasonNumber(), episodeInfo.getEpisodeNumber(), e);
							}
						}
						dbEpisode.setStatus(initialStatus);

						dbEpisodes.add(dbEpisode);
					}
				}

				LOGGER.debug("Storing show episode data to db");
				dbEngine.store(IDBEngine.EPISODE, tvShow.getId(), dbEpisodes);
				LOGGER.debug("Show episode data stored to db");

				LOGGER.info("Show [{} - {}] processed successfully", tvShow.getId(), tvShow.getName());
			} catch (DaoException e) {
				LOGGER.error("Unable to get show informations for id [{}], show will not be added in db", showId, e);
			} catch (InterruptedException e) {
				LOGGER.error("Error while retrieving show from queue", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	public void addShow(Long id, Status initialStatus) {
		queue.add(new AbstractMap.SimpleEntry<Long, Status>(id, initialStatus));
	}

}
