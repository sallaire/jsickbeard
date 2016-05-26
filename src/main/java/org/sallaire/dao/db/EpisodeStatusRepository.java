package org.sallaire.dao.db;

import java.util.Collection;

import org.sallaire.dao.db.entity.EpisodeStatus;
import org.sallaire.dto.user.Status;
import org.springframework.data.repository.CrudRepository;

public interface EpisodeStatusRepository extends CrudRepository<EpisodeStatus, Long> {

	Collection<EpisodeStatus> findByStatus(Status status);

	EpisodeStatus findByEpisodeIdAndShowConfigurationFollowersId(Long episodeId, Long userId);

	Collection<EpisodeStatus> findByStatusAndShowConfigurationFollowersId(Status status, Long id);
}
