package org.sallaire.service;

import org.sallaire.controller.conf.CurrentUser;
import org.sallaire.dao.db.TvShowConfigurationRepository;
import org.sallaire.dao.db.TvShowRepository;
import org.sallaire.dao.db.entity.User;
import org.sallaire.dto.api.TvShowConfigurationParam;
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

	public void upsertShow(Long showId, TvShowConfigurationParam configParam, @CurrentUser User currentUser) {
		// LOGGER.debug("Process show configuration for show {}", showId);
		// TvShow tvShow = tvShowDao.getTvShowFromSourceId(showId);
		// if (tvShow == null) {
		// LOGGER.debug("New show added to followed shows : {}", showId);
		// // AddShowProcessor
		// } else {
		// TvShowConfiguration showConfig = tvShowDao.getUserConfiguration(showId, currentUser.getName());
		// if (showConfig == null) {
		// showConfig = new TvShowConfiguration();
		// showConfig.setUser(currentUser);
		// showConfig.setTvShow(tvShow);
		// }
		// if (configParam.getQuality() == null) {
		// LOGGER.warn("No quality configured, default quality {} will be set", Quality.SD);
		// showConfig.setQuality(Quality.SD);
		// } else {
		// showConfig.setQuality(configParam.getQuality());
		// }
		// showConfig.setAudioLang(configParam.getAudio());
		// tvShowConfigDao.save(showConfig);
		//
		// }

	}

}
