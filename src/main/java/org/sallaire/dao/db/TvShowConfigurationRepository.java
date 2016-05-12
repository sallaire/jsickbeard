package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dto.user.Quality;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface TvShowConfigurationRepository extends GraphRepository<TvShowConfiguration> {

	@Query("MATCH (user:User { name:{userName}})--conf-[:CONFIGURED_BY]-show WHERE show.sourceId={showId} RETURN  user,conf,show")
	TvShowConfiguration getUserConfiguration(@Param("showId") Long showId, @Param("userName") String userName);

	@Query("MATCH (showConfig:TvShowConfiguration)-[r:FOLLOWED_BY]-(user) WHERE showConfig.tvShow.sourceId={showId} AND showConfig.quality={quality} AND showConfig.audioLang={audioLang} RETURN showConfig, user")
	TvShowConfiguration getConfiguration(@Param("showId") Long showId, @Param("quality") Quality quality, @Param("audioLang") String audioLang);
}
