package org.sallaire.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.sallaire.dao.db.TvShowConfigurationRepository;
import org.sallaire.dao.db.TvShowRepository;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dao.db.entity.User;
import org.sallaire.dto.admin.AdminConfig;
import org.sallaire.dto.admin.AdminShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

	@Autowired
	private TvShowRepository tvShowDao;

	@Autowired
	private TvShowConfigurationRepository configDao;

	public List<AdminShow> getAllShows() {
		List<AdminShow> results = new ArrayList<>();
		for (TvShow tvShow : tvShowDao.findAll()) {
			AdminShow result = new AdminShow(tvShow.getId(), tvShow.getName());
			result.setConfigurations(tvShow.getConfigurations().size());
			int i = 0;
			for (TvShowConfiguration showConfig : tvShow.getConfigurations()) {
				i += showConfig.getFollowers().size();
			}
			result.setUsers(i);
			results.add(result);
		}

		return results;
	}

	public List<AdminConfig> getAllConfigs(Long id) {
		List<AdminConfig> results = new ArrayList<>();
		for (TvShowConfiguration config : configDao.findByTvShowId(id)) {
			AdminConfig result = new AdminConfig(config.getId(), config.getQuality(), config.getAudioLang());
			result.setUsers(config.getFollowers().stream().map(User::getName).collect(Collectors.toList()));
			results.add(result);
		}
		return results;
	}

}
