package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dto.user.Quality;
import org.springframework.data.repository.CrudRepository;

public interface TvShowConfigurationRepository extends CrudRepository<TvShowConfiguration, Long> {

	TvShowConfiguration findByTvShowIdAndFollowersName(Long showId, String userName);

	TvShowConfiguration findByTvShowIdAndQualityAndAudioLang(Long showId, Quality quality, String audioLang);
}
