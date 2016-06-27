package org.sallaire.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
import org.sallaire.dto.api.ShowDto;
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
		} else if (tvShow.getConfigurations().stream().allMatch(c -> c.getFollowers().isEmpty())) {
			updateShow(tvShow);
		}
		return tvShow;
	}

	public void updateShow(TvShow tvShow) {
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
					LOGGER.debug("Add new episode {}-{}", e.getSeason(), e.getEpisode());
					tvShow.addEpisode(e);
					e.setTvShow(tvShow);
				}
			});
			tvShowDao.save(tvShow);
			LOGGER.debug("Show informations saved");
		} catch (DaoException e) {
			LOGGER.error("Unable to get show [{}] informations", tvShow.getId(), e);
		}
	}

	public void processEpisodesStatus(TvShowConfiguration tvShowConfiguration) {
		TvShow tvShow = tvShowConfiguration.getTvShow();
		LOGGER.debug("Processing {} episodes user data", tvShow.getEpisodes().size());
		tvShow.getEpisodes().forEach(e -> {
			if (!e.getStatus().stream().anyMatch(s -> tvShowConfiguration.equals(s.getShowConfiguration()))) {
				EpisodeStatus epStatus = new EpisodeStatus();
				epStatus.setEpisode(e);
				e.getStatus().add(epStatus);
				epStatus.setShowConfiguration(tvShowConfiguration);
				if (e.getAirDate().isAfter(LocalDate.now())) {
					epStatus.setStatus(Status.UNAIRED);
				} else {
					epStatus.setStatus(Status.SKIPPED);
				}
			}
		});
		episodeDao.save(tvShow.getEpisodes());

	}

	public List<SearchResult> search(UserDto user, String name, String lang) {
		try {
			return metaDataDao.searchForShows(name, lang);
		} catch (DaoException e) {
			LOGGER.error("Unable to find results for show {}", name, e);
		}
		return null;
	}

	public List<ShowDto> find(UserDto user, String name, String lang) {
		try {
			List<SearchResult> results = metaDataDao.searchForShows(name, lang);
			Collection<String> userShows = tvShowDao.findByConfigurationsFollowersId(user.getId()).stream().map(TvShow::getName).collect(Collectors.toList());
			return results.stream().map(sr -> new ShowDto(sr, userShows.contains(sr.getName()))).collect(Collectors.toList());
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

	public Collection<ShowDto> getShowsForUser(UserDto user) {
		Collection<TvShow> tvShows = tvShowDao.findByConfigurationsFollowersId(user.getId());
		return tvShows.stream().map(s -> new ShowDto(s, null)).collect(Collectors.toList());
	}

	public Collection<TvShow> getActiveShows() {
		List<TvShow> results = new ArrayList<>();
		tvShowDao.findAll().forEach(s -> {
			if (s.getConfigurations().stream().anyMatch(c -> !c.getFollowers().isEmpty())) {
				results.add(s);
			}
		});
		return results;
	}
}
