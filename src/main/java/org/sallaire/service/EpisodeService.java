package org.sallaire.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.sallaire.dao.db.EpisodeStatusRepository;
import org.sallaire.dao.db.entity.EpisodeStatus;
import org.sallaire.dto.api.EpisodeDto;
import org.sallaire.dto.api.UpdateEpisodeStatusParam;
import org.sallaire.dto.user.Status;
import org.sallaire.dto.user.UserDto;
import org.sallaire.service.processor.WantedShowProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EpisodeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EpisodeService.class);

	@Autowired
	private EpisodeStatusRepository episodeStatusDao;

	@Autowired
	private WantedShowProcessor downloadProcessor;

	public void updateEpisodesStatus(UpdateEpisodeStatusParam params, UserDto user) {
		List<EpisodeStatus> toUpdateEpisodes = new ArrayList<>();
		for (Long episodeId : params.getIds()) {
			EpisodeStatus episode = episodeStatusDao.findByEpisodeIdAndShowConfigurationFollowersId(episodeId, user.getId());
			if (episode == null) {
				LOGGER.warn("Unable to find a followed episode for id {} and user {}", episodeId, user.getName());
			} else {
				episode.setStatus(params.getStatus());
				toUpdateEpisodes.add(episode);
			}
		}
		LOGGER.debug("Save {} episodes to update", toUpdateEpisodes.size());
		episodeStatusDao.save(toUpdateEpisodes);
		LOGGER.debug("Episodes saved", toUpdateEpisodes.size());
		if (params.getStatus() == Status.WANTED) {
			LOGGER.info("Episodes status set to {}, process search", Status.WANTED);
			downloadProcessor.process(toUpdateEpisodes.stream().map(EpisodeStatus::getId).collect(Collectors.toList()));
		}
	}

	public Collection<EpisodeDto> getEpisodesForStatus(UserDto user, Status status, int from, int length) {
		Collection<EpisodeStatus> episodes = episodeStatusDao.findByStatusAndShowConfigurationFollowersId(status, user.getId());
		if (CollectionUtils.isNotEmpty(episodes)) {
			Comparator<EpisodeStatus> comparator = null;
			if (status == Status.DOWNLOADED) {
				comparator = (EpisodeStatus e1, EpisodeStatus e2) -> e2.getDownloadDate().compareTo(e1.getDownloadDate());
			} else {
				comparator = (EpisodeStatus e1, EpisodeStatus e2) -> e1.getEpisode().getAirDate().compareTo(e2.getEpisode().getAirDate());
			}
			return episodes.stream() //
					.sorted(comparator) //
					.skip(from).limit(length) //
					.map(e -> TvShowConverter.convertEpisode(e)).collect(Collectors.toList());
		} else {
			return CollectionUtils.emptyCollection();
		}
	}

}
