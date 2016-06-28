package org.sallaire.dao.db;

import java.util.List;

import org.sallaire.dao.db.entity.Episode;
import org.springframework.data.repository.CrudRepository;

public interface EpisodeRepository extends CrudRepository<Episode, Long> {

	List<Episode> findByTvShowIdAndSeasonOrderByEpisode(Long showId, Integer season);

	Episode findByTvShowIdAndSeasonAndEpisode(Long showId, Integer season, Integer episode);
}
