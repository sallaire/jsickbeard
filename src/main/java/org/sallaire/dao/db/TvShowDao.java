package org.sallaire.dao.db;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.sallaire.dao.db.engine.IDBEngine;
import org.sallaire.dto.metadata.Episode;
import org.sallaire.dto.metadata.TvShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TvShowDao {
	@Autowired
	private IDBEngine dbEngine;

	public TvShow getShow(Long id) {
		return dbEngine.get(IDBEngine.SHOW, id);
	}

	public void saveShow(TvShow tvShow) {
		dbEngine.store(IDBEngine.SHOW, tvShow.getId(), tvShow);
	}

	public Collection<TvShow> getShows() {
		return dbEngine.getValues(IDBEngine.SHOW);
	}

	public Collection<Episode> getShowEpisodes(Long id) {
		return dbEngine.get(IDBEngine.EPISODE, id);
	}

	public void saveShowEpisodes(Long showId, Collection<Episode> episodes) {
		dbEngine.store(IDBEngine.EPISODE, showId, episodes);
	}

	public void saveShowEpisode(Episode episode) {
		Collection<Episode> episodes = getShowEpisodes(episode.getShowId());
		episodes.remove(episode);
		episodes.add(episode);
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

}
