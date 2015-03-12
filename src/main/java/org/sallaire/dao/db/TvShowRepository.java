package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.TvShow;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvShowRepository extends PagingAndSortingRepository<TvShow, Long> {

}
