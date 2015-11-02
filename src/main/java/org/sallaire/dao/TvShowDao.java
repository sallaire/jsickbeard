package org.sallaire.dao;

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

	public Map<Long, List<Episode>> getAllShowEpisodes() {
		return dbEngine.getAll(IDBEngine.EPISODE);
	}

	public Long getLastUpdate() {
		return dbEngine.get(IDBEngine.UPDATE_TIME, 0L);
	}

	public void saveLastUpdate(Long lastUpdate) {
		dbEngine.store(IDBEngine.UPDATE_TIME, 0L, lastUpdate);
	}
}