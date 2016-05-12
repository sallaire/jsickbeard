package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.Episode;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface EpisodeRepository extends GraphRepository<Episode> {

}
