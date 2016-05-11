// package org.sallaire.service.processor;
//
// import java.io.IOException;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collection;
// import java.util.List;
//
// import org.sallaire.dao.db.DownloadDao;
// import org.sallaire.dao.db.TvShowDao;
// import org.sallaire.dto.metadata.TvShow;
// import org.sallaire.dto.user.EpisodeStatus;
// import org.sallaire.dto.user.Status;
// import org.sallaire.dto.user.TvShowConfiguration;
// import org.sallaire.service.DownloadService;
// import org.sallaire.service.client.IClient;
// import org.sallaire.service.provider.IProvider;
// import org.sallaire.service.provider.Torrent;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// @Component
// public class WantedShowProcessor {
//
// private static final Logger LOGGER = LoggerFactory.getLogger(WantedShowProcessor.class);
//
// @Autowired
// private TvShowDao showDao;
//
// @Autowired
// private DownloadDao downloadDao;
//
// @Autowired
// private DownloadService downloadService;
//
// @Scheduled(cron = "0 0 * * * *")
// public void process() {
// Collection<EpisodeStatus> episodes = downloadDao.getWantedEpisodes();
// LOGGER.info("Starting wanted show processor with {} wanted episodes", episodes.size());
// findEpisodes(episodes);
// LOGGER.info("Wanted show processor done");
// }
//
// @Async
// public void process(List<EpisodeStatus> episodes) {
// findEpisodes(episodes);
// }
//
// public void process(EpisodeStatus episode) {
// findEpisodes(Arrays.asList(episode));
// // TODO renvoyer un boolean pour savoir si l'épisode a été trouvé ?
// }
//
// private synchronized void findEpisodes(Collection<EpisodeStatus> episodes) {
// for (EpisodeStatus episode : episodes) {
// LOGGER.debug("Try to retrieve episode {}", episode);
// if (searchAndGetEpisode(episode)) {
// LOGGER.debug("Episode {} found", episode);
// downloadDao.removeWantedEpisode(episode);
// } else {
// downloadDao.saveWantedEpisode(episode);
// }
// }
// }
//
// public boolean searchAndGetEpisode(EpisodeStatus episode) {
// TvShowConfiguration config = downloadDao.getShowConfiguration(episode.getEpisodeKey().getShowId());
// TvShow show = showDao.getShow(episode.getEpisodeKey().getShowId());
// Torrent torrent = null;
// for (IProvider provider : downloadService.getActiveProviders()) {
// try {
// Collection<String> namesToSearch = null;
// if (config.getCustomNames().isEmpty()) {
// namesToSearch = Arrays.asList(show.getOriginalName());
// } else {
// namesToSearch = new ArrayList<>(config.getCustomNames());
// }
// torrent = provider.findEpisode(namesToSearch, episode.getEpisodeKey().getLang(), episode.getEpisodeKey().getSeason(), episode.getEpisodeKey().getNumber(), episode.getEpisodeKey().getQuality(),
// episode.getFileNames());
// if (torrent != null) {
// LOGGER.info("Episode [{}] found with provider [{}]", episode, provider.getId());
// break;
// }
// } catch (IOException e) {
// LOGGER.error("Error while searching episode [{}] with provider [{}]", episode, provider.getId(), e);
// }
// }
//
// if (torrent != null) {
// IClient client = downloadService.getActiveClient();
// if (client != null) {
// LOGGER.info("Sending torrent to client [{}]", client.getId());
// try {
// final Torrent torrentToDownload = torrent;
// client.addTorrent(torrentToDownload, config, episode);
//
// // Update episode status to snatched
// episode.setStatus(Status.SNATCHED);
// episode.addFileName(torrentToDownload.getName());
// episode.setDownloadDate(LocalDateTime.now());
// downloadDao.saveEpisodeStatus(episode);
// downloadDao.saveSnatchedEpisode(episode);
// return true;
// } catch (IOException e) {
// LOGGER.error("Unable to send torrent to client", e);
// }
// } else {
// LOGGER.error("No torrent client defined to download search results");
// }
// }
// return false;
// }
//
// }
