package org.sallaire.dao.metadata;

import java.util.List;

import org.sallaire.dao.DaoException;
import org.sallaire.dto.metadata.Episode;
import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.dto.metadata.TvShow;

public interface IMetaDataDao {
	List<SearchResult> searchForShows(String name, String lang) throws DaoException;

	List<Long> getShowsToUpdate(Long fromTime) throws DaoException;

	TvShow getShowInformation(Long id, String lang) throws DaoException;

	List<Episode> getShowEpisodes(Long id, String lang) throws DaoException;
}
