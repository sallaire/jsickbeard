package org.sallaire.service;

import org.sallaire.dao.db.TvShowConfigurationRepository;
import org.sallaire.dao.db.TvShowRepository;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dao.db.entity.User;
import org.sallaire.dto.api.TvShowConfigurationParam;
import org.sallaire.dto.user.Quality;
import org.sallaire.service.processor.AddShowProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TvShowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TvShowService.class);

	@Autowired
	private TvShowRepository tvShowDao;

	@Autowired
	private TvShowConfigurationRepository tvShowConfigDao;

	@Autowired
	private AddShowProcessor addShowProcessor;

	public void upsertShow(Long showId, TvShowConfigurationParam configParam, User currentUser) {
		LOGGER.debug("Process show configuration for show {}", showId);
		Quality wantedQuality = configParam.getQuality();
		if (wantedQuality == null) {
			LOGGER.warn("No quality configured, default quality {} will be set", Quality.SD);
			wantedQuality = Quality.SD;
		}

		TvShow tvShow = tvShowDao.getTvShowFromSourceId(showId);

		if (tvShow == null) {
			LOGGER.debug("New show added to followed shows : {}", showId);
			TvShowConfiguration showConfig = addConfiguration(tvShow, currentUser, wantedQuality, configParam.getAudio());
			addShowProcessor.addShow(showId, showConfig, configParam.getStatus());
		} else {

			TvShowConfiguration showConfig = tvShowConfigDao.getUserConfiguration(showId, currentUser.getName());
			if (showConfig != null && showConfig.getQuality() == wantedQuality && showConfig.getAudioLang().equals(configParam.getAudio())) {
				LOGGER.debug("No change in user configuration for show {}", showId);
			} else {
				TvShowConfiguration wantedConfig = tvShowConfigDao.getConfiguration(showId, wantedQuality, configParam.getAudio());
				if (wantedConfig == null) {
					LOGGER.debug("No configuration existing for show {}, quality {} and audio lang {}, creating one", showId, wantedQuality, configParam.getAudio());
					wantedConfig = addConfiguration(tvShow, currentUser, wantedQuality, configParam.getAudio());
					addShowProcessor.processEpisodesStatus(tvShow.getId(), wantedConfig, configParam.getStatus());
				} else {
					LOGGER.debug("Configuration already exists for show {}, quality {} and audio lang {}, using it", showId, wantedQuality, configParam.getAudio());
					wantedConfig.addFollower(currentUser);
					tvShowConfigDao.save(wantedConfig);
				}
			}
		}
	}

	private TvShowConfiguration addConfiguration(TvShow tvShow, User user, Quality quality, String audioLang) {
		TvShowConfiguration newConfig = new TvShowConfiguration();
		newConfig.setQuality(quality);
		newConfig.setAudioLang(audioLang);
		newConfig.setTvShow(tvShow);
		newConfig.addFollower(user);
		return tvShowConfigDao.save(newConfig);
	}
}
