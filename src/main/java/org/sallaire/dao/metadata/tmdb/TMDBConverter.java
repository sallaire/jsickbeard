package org.sallaire.dao.metadata.tmdb;

import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.sallaire.dto.metadata.Episode;
import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.dto.metadata.TvShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TMDBConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TMDBConverter.class);

    public static List<SearchResult> convertSearchResults(String imageUrl, List<TvSeries> searchResults) {
	return searchResults.stream().map(s -> {
	    SearchResult r = new SearchResult();
	    r.setId((long) s.getId());
	    r.setFirstAired(s.getFirstAirDate());
	    r.setName(s.getName());
	    r.setOverview(s.getOverview());
	    r.setCountries(s.getOriginCountry());
	    if (s.getPosterPath() != null) {
		r.setImage(imageUrl + s.getPosterPath());
	    }
	    return r;
	}).collect(Collectors.toList());
    }

    public static TvShow convertFromTvSeries(TvSeries tvSeries, TvSeries defaultLang) {
	TvShow result = new TvShow();
	result.setId(new Long(tvSeries.getId()));
	result.setDescription(tvSeries.getOverview());
	result.setImdbId(tvSeries.getExternalIds().getImdbId());
	if (tvSeries.getFirstAirDate() != null) {
	    try {
		LocalDate firstAirDate = LocalDate.parse(tvSeries.getFirstAirDate());
		result.setFirstAired(firstAirDate);
		result.setAirDay(firstAirDate.getDayOfWeek());
	    } catch (DateTimeParseException e) {
		LOGGER.warn("Unable to parse first air date [{}]", tvSeries.getFirstAirDate(), e);
	    }
	}
	result.setName(tvSeries.getName());
	result.setNetwork(tvSeries.getNetworks().stream().map(n -> n.getName()).collect(Collectors.toList()));
	result.setRuntime(tvSeries.getEpisodeRuntime().stream().findFirst().orElse(null));
	// TODO genre
	result.setPoster(tvSeries.getPosterPath());
	result.setBanner(tvSeries.getBackdropPath());
	result.setStatus(tvSeries.getStatus());
	if (StringUtils.isEmpty(result.getName())) {
	    result.setName(defaultLang.getName());
	}
	if (StringUtils.isEmpty(result.getDescription())) {
	    result.setDescription(defaultLang.getOverview());
	}
	return result;
    }

    public static List<Episode> convertFromTvSeason(Long showId, TvSeason tvSeason, TvSeason defaultLang) {
	return tvSeason.getEpisodes().stream().map(e -> {
	    TvEpisode defaultLangEpisode = defaultLang.getEpisodes().stream().filter(d -> e.getId() == d.getId()).findFirst().get();
	    Episode episode = new Episode();
	    episode.setAirDate(LocalDate.parse(e.getAirDate()));
	    episode.setSeason(tvSeason.getSeasonNumber());
	    episode.setEpisode(e.getEpisodeNumber());
	    episode.setDescription(e.getOverview());
	    episode.setId(new Long(e.getId()));
	    episode.setName(e.getName());
	    episode.setShowId(showId);
	    if (e.getExternalIds() != null) {
		episode.setImdbId(e.getExternalIds().getImdbId());
	    }
	    if (StringUtils.isEmpty(episode.getName())) {
		episode.setName(defaultLangEpisode.getName());
	    }
	    if (StringUtils.isEmpty(episode.getDescription())) {
		episode.setDescription(defaultLangEpisode.getOverview());
	    }
	    return episode;
	}).collect(Collectors.toList());
    }
}