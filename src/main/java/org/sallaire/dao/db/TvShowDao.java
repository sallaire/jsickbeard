package org.sallaire.dao.db;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.sallaire.dao.db.engine.IDBEngine;
import org.sallaire.dto.Episode;
import org.sallaire.dto.TvShow;
import org.sallaire.dto.TvShowConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TvShowDao {
	@Autowired
	private IDBEngine dbEngine;

	public TvShowConfiguration getShowConfiguration(Long id) {
		return dbEngine.get(IDBEngine.SHOW_CONFIGURATION, id);
	}

	public void saveShowConfiguration(Long showId, TvShowConfiguration configuration) {
		dbEngine.store(IDBEngine.SHOW_CONFIGURATION, showId, configuration);
	}

	public TvShow getShow(Long id) {
		return dbEngine.get(IDBEngine.SHOW, id);
	}

	public void saveShow(TvShow tvShow) {
		dbEngine.store(IDBEngine.SHOW, tvShow.getId(), tvShow);
	}

	public List<Episode> getShowEpisodes(Long id) {
		return dbEngine.get(IDBEngine.EPISODE, id);
	}

	public void saveShowEpisodes(Long showId, List<Episode> episodes) {
		dbEngine.store(IDBEngine.EPISODE, showId, episodes);
	}

	public void saveShowEpisode(Episode episode) {
		List<Episode> episodes = getShowEpisodes(episode.getShowId());
		episodes.stream().filter(e -> e.getId().equals(episode.getShowId())).forEach(e -> e = episode);
		dbEngine.store(IDBEngine.EPISODE, episode.getShowId(), episodes);
	}

	public Map<Long, List<Episode>> getAllShowEpisodes() {
		return dbEngine.getAll(IDBEngine.EPISODE);
	}

	public Long getLastUpdate() {
		return dbEngine.get(IDBEngine.UPDATE_TIME, 0L);
	}

	public void saveLastUpdate(Long lastUpdate) {
		dbEngine.store(IDBEngine.UPDATE_TIME, 0L, lastUpdate);
	}

	public Collection<Episode> getWantedEpisodes() {
		return dbEngine.getValues(IDBEngine.WANTED_EPISODE);
	}

	public void saveWantedEpisode(Episode episode) {
		dbEngine.store(IDBEngine.WANTED_EPISODE, episode.getId(), episode);
	}

	public void removeWantedEpisode(long id) {
		dbEngine.remove(IDBEngine.WANTED_EPISODE, id);
	}

	public Collection<Episode> getSnatchedEpisodes() {
		return dbEngine.getValues(IDBEngine.SNATCHED_EPISODE);
	}

	public void saveSnatchedEpisode(Episode episode) {
		dbEngine.store(IDBEngine.SNATCHED_EPISODE, episode.getId(), episode);
	}

	public void removeSnatchedEpisode(long id) {
		dbEngine.remove(IDBEngine.SNATCHED_EPISODE, id);
	}
}