package org.sallaire.dao.db;

import java.util.Collection;

import org.sallaire.dao.db.entity.TvShow;
import org.springframework.data.repository.CrudRepository;

public interface TvShowRepository extends CrudRepository<TvShow, Long> {
	Collection<TvShow> findByConfigurationsFollowersId(Long id);
}
