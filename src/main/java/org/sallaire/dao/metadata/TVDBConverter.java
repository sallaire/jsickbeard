package org.sallaire.dao.metadata;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.sallaire.dto.Episode;
import org.sallaire.dto.TvShow;
import org.sallaire.dto.tvdb.EpisodeInfo;
import org.sallaire.dto.tvdb.ShowInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TVDBConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(TVDBConverter.class);

	private static final DateTimeFormatter TIME_FORMAT_EN = DateTimeFormatter.ofPattern("hh:mm a");
	private static final DateTimeFormatter TIME_FORMAT_FR = DateTimeFormatter.ofPattern("HH:mm");

	public static TvShow convertFromTVDB(ShowInfo showInfo) {
		TvShow tvShow = new TvShow();
		tvShow.setId(showInfo.getId());
		tvShow.setImdbId(showInfo.getImdbId());
		tvShow.setName(showInfo.getName());
		tvShow.setDescription(showInfo.getDescription());
		tvShow.setNetwork(showInfo.getNetwork());
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

	public static Episode convertFromTVDB(Long showId, EpisodeInfo episodeInfo) {
		Episode dbEpisode = new Episode();
		dbEpisode.setId(episodeInfo.getId());
		dbEpisode.setImdbId(episodeInfo.getImdbId());
		dbEpisode.setShowId(showId);
		dbEpisode.setName(episodeInfo.getName());
		dbEpisode.setSeason(episodeInfo.getSeasonNumber());
		dbEpisode.setEpisode(episodeInfo.getEpisodeNumber());
		dbEpisode.setLastUpdated(episodeInfo.getLastUpdated());
		dbEpisode.setDescription(episodeInfo.getDescription());
		dbEpisode.setLang(episodeInfo.getLang());
		if (episodeInfo.getFirstAired() != null) {
			try {
				dbEpisode.setAirDate(LocalDate.parse(episodeInfo.getFirstAired()));
			} catch (DateTimeParseException e) {
				LOGGER.warn("Unable to parse episode air date [{}] for episode [{}-{}]", episodeInfo.getFirstAired(), episodeInfo.getSeasonNumber(), episodeInfo.getEpisodeNumber(), e);
			}
		}
		return dbEpisode;
	}
}