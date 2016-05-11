package org.sallaire.dao.metadata.tvdb;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.dto.tvdb.EpisodeInfo;
import org.sallaire.dto.tvdb.ShowInfo;
import org.sallaire.dto.tvdb.TVDBSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TVDBConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(TVDBConverter.class);

	private static final DateTimeFormatter TIME_FORMAT_EN = DateTimeFormatter.ofPattern("hh:mm a");
	private static final DateTimeFormatter TIME_FORMAT_FR = DateTimeFormatter.ofPattern("HH:mm");

	public static TvShow convertFromTVDB(ShowInfo showInfo) {
		TvShow tvShow = new TvShow();
		tvShow.setSourceId(showInfo.getId());
		tvShow.setImdbId(showInfo.getImdbId());
		tvShow.setName(showInfo.getName());
		tvShow.setDescription(showInfo.getDescription());
		tvShow.setNetwork(Arrays.asList(showInfo.getNetwork()));
		tvShow.setGenre(showInfo.getGenre());
		if (showInfo.getAirDay() != null) {
			try {
				tvShow.setAirDay(DayOfWeek.valueOf(showInfo.getAirDay().toUpperCase()));
			} catch (IllegalArgumentException e) {
				LOGGER.warn("Unable to parse air day [{}]", showInfo.getAirDay(), e);
			}
		}
		tvShow.setRuntime(showInfo.getRuntime());
		tvShow.setBanner(showInfo.getBanner());
		tvShow.setPoster(showInfo.getPoster());
		tvShow.setFanart(showInfo.getFanart());
		tvShow.setLastUpdated(showInfo.getLastUpdated());
		tvShow.setStatus(showInfo.getStatus());
		if (showInfo.getFirstAired() != null) {
			try {
				tvShow.setFirstAired(LocalDate.parse(showInfo.getFirstAired()));
			} catch (DateTimeParseException e) {
				LOGGER.warn("Unable to parse first air date [{}]", showInfo.getFirstAired(), e);
			}
		}
		if (showInfo.getAirTime() != null) {
			try {
				tvShow.setAirTime(LocalTime.parse(showInfo.getAirTime(), TIME_FORMAT_EN));
			} catch (DateTimeParseException e) {
				LOGGER.debug("Unable to parse air time with EN format, try with FR");
				try {
					tvShow.setAirTime(LocalTime.parse(showInfo.getAirTime(), TIME_FORMAT_FR));
				} catch (DateTimeParseException e1) {
					LOGGER.warn("Unable to parse first air time [{}]", showInfo.getAirTime(), e1);
				}
			}
		}

		return tvShow;
	}

	public static Episode convertFromTVDB(TvShow tvShow, EpisodeInfo episodeInfo) {
		Episode dbEpisode = new Episode();
		dbEpisode.setSourceId(episodeInfo.getId());
		dbEpisode.setImdbId(episodeInfo.getImdbId());
		dbEpisode.setTvShow(tvShow);
		dbEpisode.setName(episodeInfo.getName());
		dbEpisode.setSeason(episodeInfo.getSeasonNumber());
		dbEpisode.setEpisode(episodeInfo.getEpisodeNumber());
		dbEpisode.setDescription(episodeInfo.getDescription());
		if (episodeInfo.getFirstAired() != null) {
			try {
				dbEpisode.setAirDate(LocalDate.parse(episodeInfo.getFirstAired()));
			} catch (DateTimeParseException e) {
				LOGGER.warn("Unable to parse episode air date [{}] for episode [{}-{}]", episodeInfo.getFirstAired(), episodeInfo.getSeasonNumber(), episodeInfo.getEpisodeNumber(), e);
			}
		}
		return dbEpisode;
	}

	public static List<SearchResult> convertSearchResults(List<TVDBSearchResult> searchResults) {
		return searchResults.stream().map(s -> {
			SearchResult r = new SearchResult();
			r.setId(s.getId());
			r.setFirstAired(s.getFirstAired());
			r.setName(s.getName());
			r.setOverview(s.getOverview());
			return r;
		}).collect(Collectors.toList());
	}
}
