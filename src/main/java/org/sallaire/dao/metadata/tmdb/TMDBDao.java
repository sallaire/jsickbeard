package org.sallaire.dao.metadata.tmdb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.metadata.IMetaDataDao;
import org.sallaire.dto.metadata.Episode;
import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.dto.metadata.TvShow;
import org.springframework.stereotype.Repository;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV.TvMethod;
import info.movito.themoviedbapi.TvResultsPage;
import info.movito.themoviedbapi.model.changes.ChangesItems;
import info.movito.themoviedbapi.model.changes.ChangesResultsPage;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

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
		TvSeries enTvSeries = new TmdbApi(API_KEY).getTvSeries().getSeries(id.intValue(), "en", TvMethod.external_ids, TvMethod.images);
		return TMDBConverter.convertFromTvSeries(tvSeries, enTvSeries);
	}

	@Override
	public List<Episode> getShowEpisodes(Long id, String lang) throws DaoException {
		List<Episode> episodes = new ArrayList<>();
		TvSeries tvSeries = new TmdbApi(API_KEY).getTvSeries().getSeries(id.intValue(), lang);
		for (int i = 1; i <= tvSeries.getNumberOfSeasons(); i++) {
			TvSeason tvSeason = new TmdbApi(API_KEY).getTvSeasons().getSeason(id.intValue(), i, lang);
			TvSeason defaultLangTvSeason = new TmdbApi(API_KEY).getTvSeasons().getSeason(id.intValue(), i, "en");
			episodes.addAll(TMDBConverter.convertFromTvSeason(id, tvSeason, defaultLangTvSeason));
		}
		return episodes;
	}

	@Override
	public boolean hasShowUpdates(Long id) throws DaoException {
		ChangesItems changes = new TmdbApi(API_KEY).getChanges().getTvSerieChangesList(id.intValue());
		return CollectionUtils.isNotEmpty(changes.getChangedItems());
	}

}
