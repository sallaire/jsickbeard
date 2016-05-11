// package org.sallaire.service.processor;
//
// import java.time.LocalDate;
// import java.util.Collection;
// import java.util.List;
//
// import org.sallaire.JackBeardConstants;
// import org.sallaire.dao.DaoException;
// import org.sallaire.dao.db.DownloadDao;
// import org.sallaire.dao.db.TvShowDao;
// import org.sallaire.dao.metadata.IMetaDataDao;
// import org.sallaire.dto.metadata.Episode;
// import org.sallaire.dto.metadata.TvShow;
// import org.sallaire.dto.user.EpisodeKey;
// import org.sallaire.dto.user.EpisodeStatus;
// import org.sallaire.dto.user.Status;
// import org.sallaire.dto.user.TvShowConfiguration;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// @Component
// public class UpdateShowProcessor {
//
// private static final Logger LOGGER = LoggerFactory.getLogger(UpdateShowProcessor.class);
//
// @Autowired
// private TvShowDao showDao;
//
// @Autowired
// private IMetaDataDao metaDataDao;
//
// @Autowired
// private DownloadDao downloadDao;
//
// @Scheduled(cron = "0 0 2 * * *")
// public void updateShow() {
// LOGGER.info("Starting update show processor");
// Collection<TvShow> shows = showDao.getShows();
// LOGGER.debug("{} show to process", shows.size());
//
// Long showId = null;
// for (TvShow show : shows) {
// showId = show.getId();
// LOGGER.debug("processing show {}", showId);
//
// LOGGER.debug("Retrieve show configuration");
// TvShowConfiguration showConfig = downloadDao.getShowConfiguration(showId);
// LOGGER.debug("Show configuration retrieved");
//
// // Check if the show has been updated
// try {
// if (metaDataDao.hasShowUpdates(showId)) {
// LOGGER.debug("show {} has to be refreshed, ", showId);
// updateShow(showConfig);
// }
// } catch (DaoException e) {
// LOGGER.error("Unable to get show [{}] update informations, it will be ignored", showId, e);
// }
//
// // Now check if one or several episodes has to be set to wanted
// // status
// Collection<Episode> episodes = showDao.getShowEpisodes(showId);
// final LocalDate now = LocalDate.now();
// LOGGER.debug("Checking if episodes has been aired and set them to wanted");
// episodes.stream().filter(e -> e.getAirDate().isBefore(now)).forEach(e -> {
// EpisodeKey epKey = new EpisodeKey(showConfig, e);
// EpisodeStatus epStatus = downloadDao.getEpisodeStatus(epKey);
// if (epStatus.getStatus() == Status.UNAIRED) {
// LOGGER.debug("Epsiode S{}E{} has been aired ({}) and is set to wanted", e.getSeason(), e.getEpisode(), e.getAirDate());
// epStatus.setStatus(Status.WANTED);
// downloadDao.saveEpisodeStatus(epStatus);
// downloadDao.saveWantedEpisode(epStatus);
// }
// });
// }
// LOGGER.info("Update show processor done");
// }
//
// private void updateShow(TvShowConfiguration showConfig) {
// try {
//
// LOGGER.debug("Retrieve show informations");
// TvShow tvShow = metaDataDao.getShowInformation(showConfig.getId(), JackBeardConstants.LANG);
// showDao.saveShow(tvShow);
// LOGGER.debug("Show informations saved");
//
// LOGGER.debug("Retrieve show episodes");
// List<Episode> updatedEpisodes = metaDataDao.getShowEpisodes(showConfig.getId(), JackBeardConstants.LANG);
// showDao.saveShowEpisodes(showConfig.getId(), updatedEpisodes);
// LOGGER.debug("Process {} episodes for show {}", updatedEpisodes.size(), tvShow.getName());
//
// updatedEpisodes.stream().forEach(e -> {
// EpisodeKey epKey = new EpisodeKey(showConfig, e);
// if (downloadDao.getEpisodeStatus(epKey) == null) {
// LOGGER.debug("New episode {}-{} found, add it to status episode", e.getSeason(), e.getEpisode());
// EpisodeStatus epStatus = new EpisodeStatus(epKey, Status.UNAIRED);
// downloadDao.saveEpisodeStatus(epStatus);
// }
// });
// } catch (DaoException e) {
// LOGGER.error("Unable to get show [{}] informations", showConfig.getId(), e);
// }
// }
// }
