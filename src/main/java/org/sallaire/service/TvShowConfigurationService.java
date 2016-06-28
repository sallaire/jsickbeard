package org.sallaire.service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.sallaire.JackBeardConstants;
import org.sallaire.dao.db.EpisodeRepository;
import org.sallaire.dao.db.TvShowConfigurationRepository;
import org.sallaire.dao.db.UserRepository;
import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dao.db.entity.User;
import org.sallaire.dto.api.EpisodeDto;
import org.sallaire.dto.api.ShowDto;
import org.sallaire.dto.api.TvShowConfigurationParam;
import org.sallaire.dto.user.Quality;
import org.sallaire.dto.user.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TvShowConfigurationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TvShowConfigurationService.class);

	@Autowired
	private TvShowConfigurationRepository tvShowConfigDao;

	@Autowired
	private EpisodeRepository episodeDao;

	@Autowired
	private UserRepository userDao;

	@Autowired
	private TvShowService tvShowService;

	public void unfollow(Long showId, UserDto user) {
		TvShowConfiguration currentUserConfig = tvShowConfigDao.findByTvShowIdAndFollowersName(showId, user.getName());
		if (currentUserConfig != null) {
			User currentUser = userDao.findOne(user.getId());
			currentUser.getConfigurations().remove(currentUserConfig);
			userDao.save(currentUser);
		}
	}

	public void upsertConfiguration(Long showId, TvShowConfigurationParam configParam, UserDto user) {
		LOGGER.debug("Process show configuration for show {}", showId);
		Quality wantedQuality = configParam.getQuality();
		if (wantedQuality == null) {
			LOGGER.warn("No quality configured, default quality {} will be set", Quality.SD);
			wantedQuality = Quality.SD;
		}

		TvShow tvShow = tvShowService.createIfNotExist(showId);
		User currentUser = userDao.findOne(user.getId());

		TvShowConfiguration currentUserConfig = tvShowConfigDao.findByTvShowIdAndFollowersName(showId, currentUser.getName());
		if (currentUserConfig != null && currentUserConfig.getQuality() == wantedQuality && currentUserConfig.getAudioLang().equals(configParam.getAudio())) {
			LOGGER.debug("No change in user configuration for show {}", showId);
		} else {
			TvShowConfiguration wantedConfig = tvShowConfigDao.findByTvShowIdAndQualityAndAudioLang(showId, wantedQuality, configParam.getAudio());
			if (wantedConfig == null) {
				LOGGER.debug("No configuration existing for show {}, quality {} and audio lang {}, creating one", showId, wantedQuality, configParam.getAudio());
				wantedConfig = addConfiguration(tvShow, currentUser, wantedQuality, configParam.getAudio());
			} else {
				LOGGER.debug("Configuration already exists for show {}, quality {} and audio lang {}, using it", showId, wantedQuality, configParam.getAudio());
			}
			if (currentUserConfig != null) {
				currentUser.getConfigurations().remove(currentUserConfig);
			}
			tvShowService.processEpisodesStatus(wantedConfig);
			currentUser.addConfiguration(wantedConfig);
			tvShowConfigDao.save(wantedConfig);
		}
	}

	private TvShowConfiguration addConfiguration(TvShow tvShow, User user, Quality quality, String audioLang) {
		TvShowConfiguration newConfig = new TvShowConfiguration();
		Path location = JackBeardConstants.DOWNLAD_DIRECTORY.resolve(StringUtils.stripAccents(tvShow.getName()).replaceAll("[^\\w\\s-]", ""));
		newConfig.setLocation(location.toString());
		newConfig.setQuality(quality);
		newConfig.setAudioLang(audioLang);
		newConfig.setTvShow(tvShow);
		newConfig.addFollower(user);
		return tvShowConfigDao.save(newConfig);
	}

	public ShowDto getShow(Long showId, UserDto user, List<String> fields) {
		TvShowConfiguration tvShowConfig = tvShowConfigDao.findByTvShowIdAndFollowersName(showId, user.getName());
		if (tvShowConfig == null) {
			return null;
		} else {
			return TvShowConverter.convertFromShowConfiguration(tvShowConfig, fields);
		}
	}

	public List<EpisodeDto> getSeason(UserDto user, Long showId, Integer season) {
		TvShowConfiguration tvShowConfig = tvShowConfigDao.findByTvShowIdAndFollowersName(showId, user.getName());
		if (tvShowConfig == null) {
			return null;
		} else {
			List<Episode> seasonEpisode = episodeDao.findByTvShowIdAndSeasonOrderByEpisode(showId, season);
			return seasonEpisode.stream().map(e -> TvShowConverter.convertEpisode(e, tvShowConfig)).collect(Collectors.toList());
		}
	}

	public EpisodeDto getEpisode(UserDto user, Long showId, Integer season, Integer episode) {
		TvShowConfiguration tvShowConfig = tvShowConfigDao.findByTvShowIdAndFollowersName(showId, user.getName());
		if (tvShowConfig == null) {
			return null;
		} else {
			return TvShowConverter.convertEpisode(episodeDao.findByTvShowIdAndSeasonAndEpisode(showId, season, episode), tvShowConfig);
		}
	}

}
