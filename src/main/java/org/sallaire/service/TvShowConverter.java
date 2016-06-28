package org.sallaire.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dao.db.entity.EpisodeStatus;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dto.api.EpisodeDto;
import org.sallaire.dto.api.ShowDto;

public class TvShowConverter {

	public static ShowDto convertFromShowConfiguration(TvShowConfiguration tvShowConfig, List<String> fields) {
		ShowDto result = new ShowDto();
		if (fields == null || fields.contains("config")) {
			result.setQuality(tvShowConfig.getQuality());
			result.setAudioLang(tvShowConfig.getAudioLang());
		}
		if (fields == null || fields.contains("tvshow")) {
			result.setId(tvShowConfig.getTvShow().getId());
			result.setImdbId(tvShowConfig.getTvShow().getImdbId());
			result.setName(tvShowConfig.getTvShow().getName());
			result.setOriginalName(tvShowConfig.getTvShow().getOriginalName());
			result.setOriginalLang(tvShowConfig.getTvShow().getOriginalLang());
			result.setDescription(tvShowConfig.getTvShow().getDescription());
			result.setNetwork(new ArrayList<>(tvShowConfig.getTvShow().getNetwork()));
			result.setGenre(tvShowConfig.getTvShow().getGenre());
			result.setRuntime(tvShowConfig.getTvShow().getRuntime());
			result.setAirDay(tvShowConfig.getTvShow().getAirDay());
			result.setAirTime(tvShowConfig.getTvShow().getAirTime());
			result.setStatus(tvShowConfig.getTvShow().getStatus());
			result.setFirstAired(tvShowConfig.getTvShow().getFirstAired());
			result.setBanner(tvShowConfig.getTvShow().getBanner());
			result.setFanart(tvShowConfig.getTvShow().getFanart());
			result.setPoster(tvShowConfig.getTvShow().getPoster());
			result.setCustomNames(tvShowConfig.getTvShow().getCustomNames());
			result.setNbSeasons(tvShowConfig.getTvShow().getNbSeasons());
			result.setNbEpisodes(tvShowConfig.getTvShow().getNbEpisodes());
		}
		if (fields == null || fields.contains("episodes")) {

			for (Episode episode : tvShowConfig.getTvShow().getEpisodes()) {
				if (result.getEpisodes().get(episode.getSeason()) == null) {
					result.getEpisodes().put(episode.getSeason(), new ArrayList<>());
				}
				result.getEpisodes().get(episode.getSeason()).add(convertEpisode(episode, tvShowConfig));
			}

			for (List<EpisodeDto> episodes : result.getEpisodes().values()) {
				episodes.sort(Comparator.comparing(EpisodeDto::getNumber));
			}
		}

		return result;
	}

	public static EpisodeDto convertEpisode(EpisodeStatus episodeStatus) {
		EpisodeDto result = new EpisodeDto();
		Episode episode = episodeStatus.getEpisode();
		result.setAirDate(episode.getAirDate());
		result.setDescription(episode.getDescription());
		result.setEpisodeId(episode.getId());
		result.setName(episode.getName());
		result.setSeason(episode.getSeason());
		result.setNumber(episode.getEpisode());
		result.setShowId(episode.getTvShow().getId());
		result.setShowName(episode.getTvShow().getName());

		result.setDownloadDate(episodeStatus.getDownloadDate());
		result.setFileNames(episodeStatus.getDownloadedFiles());
		result.setStatus(episodeStatus.getStatus());
		result.setProvider(episodeStatus.getProvider());

		return result;
	}

	public static EpisodeDto convertEpisode(Episode episode, TvShowConfiguration tvShowConfig) {
		EpisodeStatus episodeStatus = episode.getStatus().stream().filter(s -> s.getShowConfiguration().getId().equals(tvShowConfig.getId())).findFirst().get();
		return convertEpisode(episodeStatus);
	}

}
