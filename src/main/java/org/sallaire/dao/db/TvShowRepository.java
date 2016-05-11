package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface TvShowRepository extends GraphRepository<TvShow> {

	@Query("MATCH (show:TvShow {sourceId:{0}}) RETURN show")
	TvShow getTvShowFromSourceId(Long showId);

	@Query("MATCH (show:TvShow {sourceId:{showId}})-[r:FOLLOWED_BY]-(user) WHERE user.name={userName} RETURN show, r, user")
	TvShowConfiguration getUserConfiguration(@Param("showId") Long showId, @Param("userName") String userName);
}
