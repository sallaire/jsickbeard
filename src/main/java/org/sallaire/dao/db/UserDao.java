package org.sallaire.dao.db;

import java.util.Collection;

import org.sallaire.dao.db.engine.IDBEngine;
import org.sallaire.dto.user.EpisodeKey;
import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.dto.user.TvShowConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	@Autowired
	private IDBEngine dbEngine;

	public TvShowConfiguration getShowConfiguration(Long id) {
		return dbEngine.get(IDBEngine.SHOW_CONFIGURATION, id);
	}

	public void saveShowConfiguration(Long showId, TvShowConfiguration configuration) {
		dbEngine.store(IDBEngine.SHOW_CONFIGURATION, showId, configuration);
	}

	public Collection<EpisodeStatus> getWantedEpisodes() {
		return dbEngine.getValues(IDBEngine.WANTED_EPISODE);
	}

	public void saveWantedEpisode(EpisodeStatus episodeStatus) {
		dbEngine.store(IDBEngine.WANTED_EPISODE, episodeStatus.getEpisodeKey(), episodeStatus);
	}

	public void removeWantedEpisode(EpisodeStatus episodeStatus) {
		dbEngine.remove(IDBEngine.WANTED_EPISODE, episodeStatus.getEpisodeKey());
	}

	public Collection<EpisodeStatus> getSnatchedEpisodes() {
		return dbEngine.getValues(IDBEngine.SNATCHED_EPISODE);
	}

	public void saveSnatchedEpisode(EpisodeStatus episodeStatus) {
		dbEngine.store(IDBEngine.SNATCHED_EPISODE, episodeStatus.getEpisodeKey(), episodeStatus);
	}

	public void removeSnatchedEpisode(EpisodeStatus episodeStatus) {
		dbEngine.remove(IDBEngine.SNATCHED_EPISODE, episodeStatus.getEpisodeKey());
	}

	public void saveEpisodeStatus(EpisodeStatus episodeStatus) {
		dbEngine.store(IDBEngine.EPISODE_STATUS, episodeStatus.getEpisodeKey(), episodeStatus);
	}

	public EpisodeStatus getEpisodeStatus(EpisodeKey episodeKey) {
		return dbEngine.get(IDBEngine.EPISODE_STATUS, episodeKey);
	}
}
