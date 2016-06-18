package org.sallaire.dao.metadata;

import java.util.List;

import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.entity.Episode;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dto.metadata.SearchResult;

public interface IMetaDataDao {
	List<SearchResult> searchForShows(String name, String lang) throws DaoException;

	List<Long> getShowsToUpdate(Long fromTime) throws DaoException;

	TvShow getShowInformation(Long id, String lang) throws DaoException;

	List<Episode> getShowEpisodes(TvShow tvShow, String lang) throws DaoException;

	boolean hasShowUpdates(Long id) throws DaoException;
}
