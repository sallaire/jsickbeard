package org.sallaire.dao.db;

import java.util.List;

import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dto.user.Quality;
import org.springframework.data.repository.CrudRepository;

public interface TvShowConfigurationRepository extends CrudRepository<TvShowConfiguration, Long> {

	List<TvShowConfiguration> findByTvShowId(Long showId);

	TvShowConfiguration findByTvShowIdAndFollowersName(Long showId, String userName);

	TvShowConfiguration findByTvShowIdAndQualityAndAudioLang(Long showId, Quality quality, String audioLang);
}
