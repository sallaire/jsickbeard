package info.movito.themoviedbapi;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import info.movito.themoviedbapi.model.changes.ChangesItems;
import info.movito.themoviedbapi.model.changes.ChangesResultsPage;
import info.movito.themoviedbapi.tools.ApiUrl;
import info.movito.themoviedbapi.tools.MovieDbException;

public class TmdbChanges extends AbstractTmdbApi {

	public static final String TMDB_METHOD_TV = "tv";
	public static final String TMDB_METHOD_CHANGES = "changes";
	public static final String TMDB_PARAM_START_DATE = "start_date";
	public static final String TMDB_PARAM_END_DATE = "end_date";

	TmdbChanges(TmdbApi tmdbApi) {
		super(tmdbApi);
	}

	public void getMovieChangesList(int page, String startDate, String endDate) {
		throw new MovieDbException("Not implemented yet");
	}

	public void getPersonChangesList(int page, String startDate, String endDate) {
		throw new MovieDbException("Not implemented yet");
	}

	public ChangesItems getTvSerieChangesList(int serieId) {
		ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_TV, serieId, TMDB_METHOD_CHANGES);
		return mapJsonResult(apiUrl, ChangesItems.class);
	}

	public ChangesResultsPage getTvSeriesChangesList(int page, String startDate, String endDate) {
		ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_TV, TMDB_METHOD_CHANGES);
		apiUrl.addPage(page);
		if (isNotBlank(startDate)) {
			apiUrl.addParam(TMDB_PARAM_START_DATE, startDate);
		}
		if (isNotBlank(endDate)) {
			apiUrl.addParam(TMDB_PARAM_END_DATE, startDate);
		}
		return mapJsonResult(apiUrl, ChangesResultsPage.class);
	}
}
