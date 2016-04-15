package org.sallaire.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dto.api.FullEpisode;
import org.sallaire.dto.metadata.Episode;
import org.sallaire.dto.metadata.TvShow;
import org.sallaire.dto.user.EpisodeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EpisodeConverter {

	@Autowired
	private TvShowDao showDao;

	public Collection<FullEpisode> convertEpisodesStatus(Collection<EpisodeStatus> episodesStatus) {
		List<FullEpisode> fullEpisodes = new ArrayList<>();
		Map<Long, TvShow> showCache = new HashMap<>();
		for (EpisodeStatus episodeStatus : episodesStatus) {
			long showId = episodeStatus.getEpisodeKey().getShowId();
			if (!showCache.containsKey(showId)) {
				showCache.put(showId, showDao.getShow(showId));
			}
			TvShow show = showCache.get(showId);
			Episode ep = showDao.getShowEpisodes(showId) //
					.stream() //
					.filter(e -> e.getEpisode().equals(episodeStatus.getEpisodeKey().getNumber()) && e.getSeason().equals(episodeStatus.getEpisodeKey().getSeason())) //
					.findFirst().get();
			FullEpisode fullEpisode = new FullEpisode(show, ep, episodeStatus);
			fullEpisodes.add(fullEpisode);
		}
		return fullEpisodes;
	}

	public Collection<FullEpisode> convertEpisodes(Collection<Episode> episodes) {
		List<FullEpisode> fullEpisodes = new ArrayList<>();
		Map<Long, TvShow> showCache = new HashMap<>();
		for (Episode episode : episodes) {
			long showId = episode.getShowId();
			if (!showCache.containsKey(showId)) {
				showCache.put(showId, showDao.getShow(showId));
			}
			TvShow show = showCache.get(showId);
			FullEpisode fullEpisode = new FullEpisode(show, episode, null);

			fullEpisodes.add(fullEpisode);
		}
		return fullEpisodes;
	}
}
