package org.sallaire.service;

import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.sallaire.JackBeardConstants;
import org.sallaire.dao.db.TvShowConfigurationRepository;
import org.sallaire.dao.db.TvShowRepository;
import org.sallaire.dao.db.UserRepository;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dao.db.entity.User;
import org.sallaire.dto.api.FullShow;
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
	private TvShowRepository tvShowDao;

	@Autowired
	private TvShowConfigurationRepository tvShowConfigDao;

	@Autowired
	private UserRepository userDao;

	@Autowired
	private TvShowService tvShowService;

	public void upsertConfiguration(Long showId, TvShowConfigurationParam configParam, UserDto user) {
		LOGGER.debug("Process show configuration for show {}", showId);
		Quality wantedQuality = configParam.getQuality();
		if (wantedQuality == null) {
			LOGGER.warn("No quality configured, default quality {} will be set", Quality.SD);
			wantedQuality = Quality.SD;
		}

		TvShow tvShow = tvShowDao.findOne(showId);
		User currentUser = userDao.findOne(user.getId());
		TvShowConfiguration wantedConfig = null;
		if (tvShow == null) {
			LOGGER.debug("New show added to followed shows : {}", showId);
			tvShow = tvShowService.createShow(showId);
			wantedConfig = addConfiguration(tvShow, currentUser, wantedQuality, configParam.getAudio());
			tvShowService.processEpisodesStatus(wantedConfig);
		} else {

			TvShowConfiguration currentUserConfig = tvShowConfigDao.findByTvShowIdAndFollowersName(showId, currentUser.getName());
			if (currentUserConfig != null && currentUserConfig.getQuality() == wantedQuality && currentUserConfig.getAudioLang().equals(configParam.getAudio())) {
				LOGGER.debug("No change in user configuration for show {}", showId);
				return;
			} else {
				wantedConfig = tvShowConfigDao.findByTvShowIdAndQualityAndAudioLang(showId, wantedQuality, configParam.getAudio());
				if (wantedConfig == null) {
					LOGGER.debug("No configuration existing for show {}, quality {} and audio lang {}, creating one", showId, wantedQuality, configParam.getAudio());
					wantedConfig = addConfiguration(tvShow, currentUser, wantedQuality, configParam.getAudio());
					tvShowService.processEpisodesStatus(wantedConfig);
				} else {
					LOGGER.debug("Configuration already exists for show {}, quality {} and audio lang {}, using it", showId, wantedQuality, configParam.getAudio());
				}
				if (currentUserConfig != null) {
					currentUser.getConfigurations().remove(currentUserConfig);
				}
			}
		}
		currentUser.addConfiguration(wantedConfig);
		tvShowConfigDao.save(wantedConfig);
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

	public FullShow getFullShow(Long showId, UserDto user, List<String> fields) {
		TvShowConfiguration tvShowConfig = tvShowConfigDao.findByTvShowIdAndFollowersName(showId, user.getName());
		if (tvShowConfig == null) {
			return null;
		} else {
			return TvShowConverter.convertFromShowConfiguration(tvShowConfig, fields);
		}
	}

}
