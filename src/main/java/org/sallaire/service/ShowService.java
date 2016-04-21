package org.sallaire.service;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.sallaire.JackBeardConstants;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.DownloadDao;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dao.metadata.IMetaDataDao;
import org.sallaire.dto.api.FullEpisode;
import org.sallaire.dto.api.FullShow;
import org.sallaire.dto.api.TvShowConfigurationParam;
import org.sallaire.dto.api.UpdateEpisodeStatusParam;
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

	public void upsertShow(Long id, TvShowConfigurationParam showConfig) {
		LOGGER.debug("Process show configuration for show {}", id);
		boolean existingShow = true;
		TvShowConfiguration configuration = downloadDao.getShowConfiguration(id);
		Status initialStatus = showConfig.getStatus();

		if (configuration == null) {
			LOGGER.debug("New show added to followed shows : {}", id);
			existingShow = false;
			configuration = new TvShowConfiguration();
			configuration.setId(id);
			if (showConfig.getStatus() == null) {
				initialStatus = Status.SKIPPED;
				LOGGER.warn("Initial status is not configured, default status {} will be set", Status.SKIPPED);
			}
		}
		if (showConfig.getQuality() == null) {
			LOGGER.warn("No quality configured, default quality {} will be set", Quality.SD);
			configuration.setQuality(Quality.SD);
		} else {
			configuration.setQuality(showConfig.getQuality());
		}
		configuration.setAudioLang(showConfig.getAudio());
		if (StringUtils.isEmpty(showConfig.getLocation())) {
			Path defaultLocation = JackBeardConstants.DOWNLAD_DIRECTORY.resolve(StringUtils.stripAccents(showConfig.getName()).replaceAll("[^\\w\\s-]", ""));
			LOGGER.debug("No location is set, using default one [{}]", defaultLocation);
			configuration.setLocation(defaultLocation.toString());
		} else {
			configuration.setLocation(showConfig.getLocation());
		}
		configuration.setCustomNames(showConfig.getCustomNames());

		LOGGER.debug("Store show configuration", id);
		downloadDao.saveShowConfiguration(id, configuration);
		LOGGER.debug("Show configuration stored", id);

		if (!existingShow) {
			LOGGER.info("Adding show to AddShow queue");
			showProcessor.addShow(id, initialStatus);
		} else {
			LOGGER.info("Show {} is already followed, convert existing episodes status", id);
			downloadDao.getEpisodeStatus() //
					.stream() //
					.filter(e -> e.getEpisodeKey().getShowId().equals(id)) //
					.forEach(e -> {
						downloadDao.removeEpisodeStatus(e.getEpisodeKey());
						e.getEpisodeKey().setQuality(showConfig.getQuality());
						e.getEpisodeKey().setLang(showConfig.getAudio());
						downloadDao.saveEpisodeStatus(e);
					});
		}
	}

	public TvShowConfiguration getTvShowConfiguration(Long id) {
		return downloadDao.getShowConfiguration(id);
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
		try {
			return metaDataDao.getShowInformation(id, "fr");
		} catch (DaoException e) {
			LOGGER.error("Unable to find results for show {}", id, e);
		}
		return null;
	}

	public void unFollowShow(Long showId) {
		TvShowConfiguration showConfig = downloadDao.getShowConfiguration(showId);
		Collection<Episode> episodes = showDao.getShowEpisodes(showId);
		// Remove episodes status
		if (episodes != null) {
			for (Episode episode : episodes) {
				downloadDao.removeEpisodeStatus(new EpisodeKey(showConfig, episode));
			}
		}
		// Remove episodes
		showDao.removeShowEpisodes(showId);
		// Remove show
		showDao.removeShow(showId);
		// Remove showconfig
		downloadDao.removeShowConfiguration(showId);
	}

	public FullShow getFullShow(Long showId) {
		FullShow result = null;
		TvShow show = showDao.getShow(showId);
		Collection<Episode> episodes = showDao.getShowEpisodes(showId);
		TvShowConfiguration showConfig = downloadDao.getShowConfiguration(showId);
		if (show != null && showConfig != null && episodes != null) {
			Collection<FullEpisode> fullEpisodes = new ArrayList<>();

			episodes.stream().forEach(e -> {
				fullEpisodes.add(new FullEpisode(show, e, downloadDao.getEpisodeStatus(new EpisodeKey(showConfig, e))));
			});
			result = new FullShow(show, showConfig, fullEpisodes);
		}
		return result;
	}

	public Collection<Episode> getEpisodes(Long showId) {
		return showDao.getShowEpisodes(showId);
	}

	public Collection<TvShow> getShows() {
		return showDao.getShows();
	}

	public void updateEpisodesStatus(Long showId, UpdateEpisodeStatusParam params) {
		List<EpisodeStatus> episodesToSearch = new ArrayList<>();
		TvShowConfiguration showConfig = downloadDao.getShowConfiguration(showId);

		Collection<Episode> episodes = showDao.getShowEpisodes(showId);
		episodes.stream().filter(e -> params.getIds().contains(e.getId())).forEach(e -> {
			EpisodeKey epKey = new EpisodeKey(showConfig, e);
			EpisodeStatus epStatus = downloadDao.getEpisodeStatus(epKey);
			epStatus.setStatus(params.getStatus());
			if (params.getStatus() == Status.WANTED) {
				episodesToSearch.add(epStatus);
			}
			downloadDao.saveEpisodeStatus(epStatus);
		});
		if (!episodesToSearch.isEmpty()) {
			wantedShowProcessor.process(episodesToSearch);
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

	public void updateShowMetadata(Long showId) {
		LOGGER.debug("Processing show generic data");
		TvShow tvShow;
		try {
			tvShow = metaDataDao.getShowInformation(showId, "fr");
			LOGGER.debug("Storing show generic data to db");
			showDao.saveShow(tvShow);
			LOGGER.debug("Show generic data stored to db");

			LOGGER.debug("Storing show episode data to db");
			List<Episode> episodes;
			episodes = metaDataDao.getShowEpisodes(showId, "fr");
			showDao.saveShowEpisodes(showId, episodes);
			LOGGER.debug("Show episode data stored to db");
		} catch (DaoException e) {
			LOGGER.error("Error while retrieving show [{}] information from metadata API", showId, e);
		}
	}
}
