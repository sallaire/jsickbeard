package org.sallaire.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.sallaire.JackBeardConstants;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.EpisodeRepository;
import org.sallaire.dao.db.TvShowRepository;
import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dao.db.entity.EpisodeStatus;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dao.metadata.IMetaDataDao;
import org.sallaire.dto.api.FullShow;
import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.dto.user.Status;
import org.sallaire.dto.user.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TvShowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TvShowService.class);

	@Autowired
	private TvShowRepository tvShowDao;

	@Autowired
	private IMetaDataDao metaDataDao;

	@Autowired
	private EpisodeRepository episodeDao;

	public TvShow createIfNotExist(Long showId) {
		TvShow tvShow = tvShowDao.findOne(showId);
		if (tvShow == null) {
			try {
				LOGGER.info("Adding show [{}] with initial status [{}]", showId);
				LOGGER.debug("Processing show generic data");
				TvShow newShow = metaDataDao.getShowInformation(showId, JackBeardConstants.LANG);
				newShow.setConfigurations(new HashSet<>());
				tvShowDao.save(newShow);
				LOGGER.debug("Show generic data stored to db");
				LOGGER.debug("Storing episodes generic data to db");
				List<Episode> episodes = metaDataDao.getShowEpisodes(newShow, "fr");
				episodes.forEach(e -> e.setTvShow(newShow));
				newShow.setEpisodes(episodes);
				LOGGER.debug("Episodes generic data stored to db");
				tvShowDao.save(newShow);
				LOGGER.info("Show [{}] processed successfully", showId);
				return newShow;
			} catch (DaoException e) {
				LOGGER.error("Unable to get show informations for id [{}], show will not be added in db", showId, e);
			}
			return null;
		} else {
			return tvShow;
		}
	}

	public void processEpisodesStatus(TvShowConfiguration tvShowConfiguration) {
		TvShow tvShow = tvShowConfiguration.getTvShow();
		LOGGER.debug("Processing {} episodes user data", tvShow.getEpisodes().size());
		tvShow.getEpisodes().forEach(e -> {
			EpisodeStatus epStatus = new EpisodeStatus();
			epStatus.setEpisode(e);
			e.getStatus().add(epStatus);
			epStatus.setShowConfiguration(tvShowConfiguration);
			if (e.getAirDate().isAfter(LocalDate.now())) {
				epStatus.setStatus(Status.UNAIRED);
			} else {
				epStatus.setStatus(Status.SKIPPED);
			}
		});
		episodeDao.save(tvShow.getEpisodes());
	}

	public List<SearchResult> search(String name, String lang) {
		try {
			return metaDataDao.searchForShows(name, lang);
		} catch (DaoException e) {
			LOGGER.error("Unable to find results for show {}", name, e);
		}
		return null;
	}

	public void updateCustomNames(Long id, List<String> customNames) {
		TvShow tvShow = tvShowDao.findOne(id);
		if (tvShow != null) {
			tvShow.setCustomNames(customNames);
			tvShowDao.save(tvShow);
		}
	}

	public Collection<FullShow> getShowsForUser(UserDto user) {
		Collection<TvShow> tvShows = tvShowDao.findByConfigurationsFollowersId(user.getId());
		return tvShows.stream().map(s -> new FullShow(s, null)).collect(Collectors.toList());
	}
}
