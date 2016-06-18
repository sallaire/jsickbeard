package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.Episode;
import org.springframework.data.repository.CrudRepository;

public interface EpisodeRepository extends CrudRepository<Episode, Long> {

}
