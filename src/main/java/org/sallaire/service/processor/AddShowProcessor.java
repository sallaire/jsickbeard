package org.sallaire.service.processor;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections4.CollectionUtils;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dao.metadata.TVDBConverter;
import org.sallaire.dao.metadata.TVDBDao;
import org.sallaire.dto.Episode;
import org.sallaire.dto.Episode.Status;
import org.sallaire.dto.TvShow;
import org.sallaire.dto.tvdb.EpisodeInfo;
import org.sallaire.dto.tvdb.ShowData;
import org.sallaire.dto.tvdb.ShowInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AddShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddShowProcessor.class);

	private final BlockingQueue<Entry<Long, Status>> queue = new LinkedBlockingQueue<>();

	@Autowired
	private TVDBDao tvdbDao;

	@Autowired
	private TvShowDao showDao;

	@Async
	public void startShowProcessor() {
		Entry<Long, Status> entry;
		Long showId = null;
		Status initialStatus = null;
		while (true) {
			try {
				entry = queue.take();

				showId = entry.getKey();
				initialStatus = entry.getValue();
				LOGGER.info("Adding show [{}] with initial status [{}]", showId, initialStatus);

				ShowData data = tvdbDao.getShowInformation(showId, "fr");
				LOGGER.debug("Show data retrieved from TVDB");

				processShowInfo(data.getShowInfo());

				processShowEpisodes(showId, data.getEpisodes(), initialStatus);

				LOGGER.info("Show [{} - {}] processed successfully", showId, data.getShowInfo().getName());
			} catch (DaoException e) {
				LOGGER.error("Unable to get show informations for id [{}], show will not be added in db", showId, e);
			} catch (InterruptedException e) {
				LOGGER.error("Error while retrieving show from queue", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	private void processShowEpisodes(Long showId, List<EpisodeInfo> episodeInfos, Status initialStatus) {
		LOGGER.debug("Processing {} episodes data", episodeInfos != null ? episodeInfos.size() : 0);
		List<Episode> dbEpisodes = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(episodeInfos)) {
			for (EpisodeInfo episodeInfo : episodeInfos) {
				Episode dbEpisode = TVDBConverter.convertFromTVDB(showId, episodeInfo);
				if (dbEpisode.getAirDate().isAfter(LocalDate.now())) {
					dbEpisode.setStatus(Status.UNAIRED);
				} else {
					dbEpisode.setStatus(initialStatus);
				}
				dbEpisodes.add(dbEpisode);
			}
		}

		LOGGER.debug("Storing show episode data to db");
		showDao.saveShowEpisodes(showId, dbEpisodes);
		LOGGER.debug("Show episode data stored to db");
	}

	private void processShowInfo(ShowInfo showInfo) {
		LOGGER.debug("Processing show generic data");
		TvShow tvShow = TVDBConverter.convertFromTVDB(showInfo);
		LOGGER.debug("Storing show generic data to db");
		showDao.saveShow(tvShow);
		LOGGER.debug("Show generic data stored to db");
	}

	public void addShow(Long id, Status initialStatus) {
		queue.add(new AbstractMap.SimpleEntry<Long, Status>(id, initialStatus));
	}

}
