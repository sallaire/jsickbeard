package org.sallaire.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.DownloadDao;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dao.metadata.IMetaDataDao;
import org.sallaire.dto.metadata.Episode;
import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.dto.metadata.TvShow;
import org.sallaire.dto.user.EpisodeKey;
import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.dto.user.Quality;
import org.sallaire.dto.user.Status;
import org.sallaire.dto.user.TvShowConfiguration;
import org.sallaire.service.processor.AddShowProcessor;
import org.sallaire.service.processor.WantedShowProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShowService.class);

	@Autowired
	private IMetaDataDao metaDataDao;

	@Autowired
	private TvShowDao showDao;

	@Autowired
	private DownloadDao downloadDao;

	@Autowired
	private AddShowProcessor showProcessor;

	@Autowired
	private WantedShowProcessor wantedShowProcessor;

	public void add(Long id, String location, String initalStatus, String quality, String audioLang, List<String> customNames) {
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
		configuration.setLocation(location);
		configuration.setCustomNames(customNames);
		Status convertedInitialStatus = Status.SKIPPED;
		try {
			convertedInitialStatus = Status.valueOf(initalStatus);
		} catch (IllegalArgumentException e) {
			LOGGER.warn("Check of inital status {} fails, default status {} will be set", initalStatus, Status.SKIPPED, e);
		}
		LOGGER.debug("Store show configuration", id);
		downloadDao.saveShowConfiguration(id, configuration);
		LOGGER.debug("Show configuration stored", id);

		LOGGER.info("Adding show to AddShow queue");
		showProcessor.addShow(id, convertedInitialStatus);
	}

	public void update(Long id, String location, String quality, String audioLang, List<String> customNames) {
		LOGGER.debug("Process show configuration for show {}", id);
		TvShowConfiguration configuration = downloadDao.getShowConfiguration(id);
		if (configuration != null) {
			if (quality != null) {
				try {
					configuration.setQuality(Quality.valueOf(quality));
				} catch (IllegalArgumentException e) {
					LOGGER.warn("Check of quality {} fails, no quality will be set", quality, e);
				}
			}
			if (audioLang != null) {
				configuration.setAudioLang(audioLang);
			}
			if (location != null) {
				configuration.setLocation(location);
			}
			configuration.setCustomNames(customNames);
			LOGGER.debug("Update show configuration", id);
			downloadDao.saveShowConfiguration(id, configuration);
			LOGGER.debug("Show configuration updated", id);
		}
	}

	public List<SearchResult> search(String name, String lang) {
		try {
			return metaDataDao.searchForShows(name, lang);
		} catch (DaoException e) {
			LOGGER.error("Unable to find results for show {}", name, e);
		}
		return null;
	}

	public TvShow getShow(Long id) {
		return showDao.getShow(id);
	}

	public Collection<Episode> getEpisodes(Long showId) {
		return showDao.getShowEpisodes(showId);
	}

	public Collection<TvShow> getShows() {
		return showDao.getShows();
	}

	public void updateEpisodesStatus(Long showId, List<Long> ids, String status) {
		try {
			final Status convertedStatus = Status.valueOf(status);
			List<EpisodeStatus> episodesToSearch = new ArrayList<>();
			TvShowConfiguration showConfig = downloadDao.getShowConfiguration(showId);

			Collection<Episode> episodes = showDao.getShowEpisodes(showId);
			episodes.stream().filter(e -> ids.contains(e.getId())).forEach(e -> {
				EpisodeKey epKey = new EpisodeKey(showConfig, e);
				EpisodeStatus epStatus = downloadDao.getEpisodeStatus(epKey);
				epStatus.setStatus(convertedStatus);
				if (convertedStatus == Status.WANTED) {
					episodesToSearch.add(epStatus);
				}
				downloadDao.saveEpisodeStatus(epStatus);
			});
			if (!episodesToSearch.isEmpty()) {
				wantedShowProcessor.process(episodesToSearch);
			}
		} catch (IllegalArgumentException e) {
			LOGGER.warn("Check of inital status {} fails, no change will be made", status, e);
		}
	}

	public void searchEpisode(Long showId, Long episodeId) {
		TvShowConfiguration showConfig = downloadDao.getShowConfiguration(showId);
		Collection<Episode> episodes = showDao.getShowEpisodes(showId);
		Optional<Episode> ep = episodes.stream().filter(e -> e.getId().equals(episodeId)).findFirst();
		if (ep.isPresent()) {
			EpisodeKey episodeKey = new EpisodeKey(showConfig, ep.get());
			EpisodeStatus epStatus = downloadDao.getEpisodeStatus(episodeKey);
			if (epStatus.getStatus() != Status.UNAIRED) {
				LOGGER.debug("Processing search for episode {}", ep.get());
				wantedShowProcessor.process(epStatus);
			} else {
				LOGGER.warn("Episode {} is unaired, it will not be searched", ep);
			}
		} else {
			LOGGER.warn("unable to find an episode for show id {} and episode id {}", showId, episodeId);
		}
	}

	public void truncateDownloadedEpisode(Long showId, Long episodeId) {
		TvShowConfiguration showConfig = downloadDao.getShowConfiguration(showId);
		Collection<Episode> episodes = showDao.getShowEpisodes(showId);
		Optional<Episode> ep = episodes.stream().filter(e -> e.getId().equals(episodeId)).findFirst();
		if (ep.isPresent()) {
			EpisodeKey episodeKey = new EpisodeKey(showConfig, ep.get());
			EpisodeStatus epStatus = downloadDao.getEpisodeStatus(episodeKey);
			epStatus.setFileNames(new ArrayList<>());
			LOGGER.debug("Saving episode {} with empty file names", ep.get());
			downloadDao.saveEpisodeStatus(epStatus);
			LOGGER.debug("Episode {} modified", ep.get());
		} else {
			LOGGER.warn("unable to find an episode for show id {} and episode id {}", showId, episodeId);
		}
	}

	public List<Episode> getUpcomingEpisodes(int from, int length) {
		Collection<Episode> episodes = new ArrayList<>();
		showDao.getAllShowEpisodes().values().forEach(e -> episodes.addAll(e));
		LocalDate now = LocalDate.now();
		return episodes.stream() //
				.filter(e -> !e.getAirDate().isBefore(now)) //
				.sorted((e1, e2) -> e1.getAirDate().compareTo(e2.getAirDate())) //
				.skip(from).limit(length) //
				.collect(Collectors.toList());
	}
}
