package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.TvShow;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface TvShowRepository extends GraphRepository<TvShow> {

	@Query("MATCH (show:TvShow {sourceId:{0}}) RETURN show")
	TvShow getTvShowFromSourceId(Long showId);

}
