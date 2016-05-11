package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface TvShowConfigurationRepository extends GraphRepository<TvShowConfiguration> {

	@Query("MATCH (show:TvShow {sourceId:1399})-[r:FOLLOWED_BY]-(user) WHERE user.name='admin' RETURN show, r, user")
	TvShowConfiguration getUserConfiguration(@Param("showId") Long showId, @Param("userName") String userName);
}
