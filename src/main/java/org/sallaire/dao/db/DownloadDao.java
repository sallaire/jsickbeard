package org.sallaire.dao.db;

import java.util.Collection;

import org.sallaire.dao.db.engine.IDBEngine;
import org.sallaire.dto.user.EpisodeKey;
import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.dto.user.TvShowConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DownloadDao {

	@Autowired
	private IDBEngine dbEngine;

	public TvShowConfiguration getShowConfiguration(Long id) {
		return dbEngine.get(IDBEngine.SHOW_CONFIGURATION, id);
	}

	public void saveShowConfiguration(Long showId, TvShowConfiguration configuration) {
		dbEngine.store(IDBEngine.SHOW_CONFIGURATION, showId, configuration);
	}

	public void removeShowConfiguration(Long showId) {
		dbEngine.remove(IDBEngine.SHOW_CONFIGURATION, showId);
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

	public void removeSnatchedEpisode(EpisodeKey episodeKey) {
		dbEngine.remove(IDBEngine.SNATCHED_EPISODE, episodeKey);
	}

	public void saveEpisodeStatus(EpisodeStatus episodeStatus) {
		dbEngine.store(IDBEngine.EPISODE_STATUS, episodeStatus.getEpisodeKey(), episodeStatus);
	}

	public EpisodeStatus getEpisodeStatus(EpisodeKey episodeKey) {
		return dbEngine.get(IDBEngine.EPISODE_STATUS, episodeKey);
	}

	public void removeEpisodeStatus(EpisodeKey episodeKey) {
		dbEngine.remove(IDBEngine.EPISODE_STATUS, episodeKey);
	}

	public Collection<EpisodeStatus> getEpisodeStatus() {
		return dbEngine.getValues(IDBEngine.EPISODE_STATUS);
	}

	public void dropSnatchedEpisodes() {
		dbEngine.drop(IDBEngine.SNATCHED_EPISODE);
	}

	public Collection<EpisodeStatus> getDownloadedEpisodes() {
		return dbEngine.getValues(IDBEngine.DOWNLOADED_EPISODE);
	}

	public void saveDownloadedEpisode(EpisodeStatus episodeStatus) {
		dbEngine.store(IDBEngine.DOWNLOADED_EPISODE, episodeStatus.getEpisodeKey(), episodeStatus);
	}
}
