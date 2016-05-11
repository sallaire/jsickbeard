package org.sallaire.dao.metadata.tmdb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.metadata.IMetaDataDao;
import org.sallaire.dto.metadata.SearchResult;
import org.springframework.stereotype.Repository;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV.TvMethod;
import info.movito.themoviedbapi.TvResultsPage;
import info.movito.themoviedbapi.model.changes.ChangesItems;
import info.movito.themoviedbapi.model.changes.ChangesResultsPage;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;
import info.movito.themoviedbapi.tools.MovieDbException;

@Repository
public class TMDBDao implements IMetaDataDao {
	private static final String API_KEY = "d9685252c19bdeab3c302887363d4a23";

	private String imagesUrl;

	private String getImagesUrl() {
		if (imagesUrl == null) {
			imagesUrl = new TmdbApi(API_KEY).getConfiguration().getBaseUrl() + "w342";
		}
		return imagesUrl;
	}

	public List<SearchResult> searchForShows(String name, String lang) {
		TvResultsPage resultPage = new TmdbApi(API_KEY).getSearch().searchTv(name, lang, 0);
		List<TvSeries> rawResults = resultPage.getResults();
		Collections.sort(rawResults, (r1, r2) -> Double.compare(r2.getPopularity(), r1.getPopularity()));
		return TMDBConverter.convertSearchResults(getImagesUrl(), rawResults);
	}

	@Override
	public List<Long> getShowsToUpdate(Long fromTime) throws DaoException {
		List<Long> results = new ArrayList<>();
		LocalDate localDate = LocalDate.ofEpochDay(fromTime);
		Integer nbPages = 1;
		for (int i = 1; i <= nbPages; i++) {
			ChangesResultsPage resultsPage = new TmdbApi(API_KEY).getChanges().getTvSeriesChangesList(0, localDate.toString(), null);
			nbPages = resultsPage.getTotalPages();
			results.addAll(resultsPage.getResults().stream().map(r -> new Long(r.getId())).collect(Collectors.toList()));
		}

		return results;
	}

	@Override
	public TvShow getShowInformation(Long id, String lang) throws DaoException {
		TvSeries tvSeries = new TmdbApi(API_KEY).getTvSeries().getSeries(id.intValue(), lang, TvMethod.external_ids, TvMethod.images);
		TvSeries originalTvSeries = null;
		if (!lang.equals(tvSeries.getOriginalLanguage())) {
			originalTvSeries = new TmdbApi(API_KEY).getTvSeries().getSeries(id.intValue(), tvSeries.getOriginalLanguage(), TvMethod.external_ids, TvMethod.images);
		}
		return TMDBConverter.convertFromTvSeries(tvSeries, originalTvSeries);
	}

	@Override
	public List<Episode> getShowEpisodes(TvShow tvShow, String lang) throws DaoException {
		List<Episode> episodes = new ArrayList<>();
		TvSeries tvSeries = new TmdbApi(API_KEY).getTvSeries().getSeries(tvShow.getSourceId().intValue(), lang);
		for (int i = 1; i <= tvSeries.getNumberOfSeasons(); i++) {
			TvSeason tvSeason = new TmdbApi(API_KEY).getTvSeasons().getSeason(tvShow.getSourceId().intValue(), i, lang);
			TvSeason defaultLangTvSeason = null;
			if (!lang.equals(tvSeries.getOriginalLanguage())) {
				defaultLangTvSeason = new TmdbApi(API_KEY).getTvSeasons().getSeason(tvShow.getSourceId().intValue(), i, tvSeries.getOriginalLanguage());
			}
			episodes.addAll(TMDBConverter.convertFromTvSeason(tvShow, tvSeason, defaultLangTvSeason));
		}
		return episodes;
	}

	@Override
	public boolean hasShowUpdates(Long id) throws DaoException {
		try {
			ChangesItems changes = new TmdbApi(API_KEY).getChanges().getTvSerieChangesList(id.intValue());
			return CollectionUtils.isNotEmpty(changes.getChangedItems());
		} catch (MovieDbException e) {
			throw new DaoException("Unable to get show changes", e);
		}
	}

}
