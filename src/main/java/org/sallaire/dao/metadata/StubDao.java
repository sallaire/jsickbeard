package org.sallaire.dao.metadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.metadata.tmdb.TMDBConverter;
import org.sallaire.dto.metadata.SearchResult;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.movito.themoviedbapi.TvResultsPage;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

//@Repository
public class StubDao implements IMetaDataDao {

	@Override
	public List<SearchResult> searchForShows(String name, String lang) throws DaoException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Path path = Paths.get("C:\\workspace", "JackBeard", "stub", name.toLowerCase() + "-search-data.json");
			if (!Files.exists(path)) {
				path = Paths.get("C:\\workspace", "JackBeard", "stub", "search-data.json");
			}
			TvResultsPage resultPage = mapper.readValue(path.toFile(), TvResultsPage.class);
			List<TvSeries> rawResults = resultPage.getResults();
			Collections.sort(rawResults, (r1, r2) -> Double.compare(r2.getPopularity(), r1.getPopularity()));
			return TMDBConverter.convertSearchResults("http://image.tmdb.org/t/p/w1280/", rawResults);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Long> getShowsToUpdate(Long fromTime) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TvShow getShowInformation(Long id, String lang) throws DaoException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			TvSeries tvSeries = mapper.readValue(Paths.get("C:\\workspace", "JackBeard", "stub", id + "-detail-data.json").toFile(), TvSeries.class);
			tvSeries.setId(id.intValue());
			return TMDBConverter.convertFromTvSeries(tvSeries, tvSeries);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Episode> getShowEpisodes(TvShow tvShow, String lang) throws DaoException {
		ObjectMapper mapper = new ObjectMapper();
		List<Episode> episodes = new ArrayList<>();
		TvSeries tvSeries;
		try {
			tvSeries = mapper.readValue(Paths.get("C:\\workspace", "JackBeard", "stub", tvShow.getId() + "-detail-data.json").toFile(), TvSeries.class);
			for (int i = 1; i <= tvSeries.getNumberOfSeasons(); i++) {
				TvSeason tvSeason = mapper.readValue(Paths.get("C:\\workspace", "JackBeard", "stub", tvShow.getId() + "-" + i + "-season-fr-data.json").toFile(), TvSeason.class);
				TvSeason defaultLangTvSeason = null;
				if (!lang.equals(tvSeries.getOriginalLanguage())) {
					defaultLangTvSeason = mapper.readValue(Paths.get("C:\\workspace", "JackBeard", "stub", tvShow.getId() + "-" + i + "-season-en-data.json").toFile(), TvSeason.class);
				}
				episodes.addAll(TMDBConverter.convertFromTvSeason(tvShow, tvSeason, defaultLangTvSeason));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return episodes;
	}

	@Override
	public boolean hasShowUpdates(Long id) throws DaoException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getImageUrl(String imageName) {
		// TODO Auto-generated method stub
		return "http://image.tmdb.org/t/p/w1280/" + imageName;
	}

}
