package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.EpisodeStatus;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface EpisodeStatusRepository extends GraphRepository<EpisodeStatus> {

}
