package org.sallaire.service.processor;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.sallaire.dao.db.EpisodeStatusRepository;
import org.sallaire.dao.db.entity.EpisodeStatus;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dto.user.Status;
import org.sallaire.service.DownloadService;
import org.sallaire.service.client.IClient;
import org.sallaire.service.provider.IProvider;
import org.sallaire.service.provider.Torrent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class WantedShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(WantedShowProcessor.class);

	@Autowired
	private EpisodeStatusRepository episodeStatusDao;

	@Autowired
	private DownloadService downloadService;

	@Autowired
	private SnatchedShowProcessor snatchedShowProcessor;

	@Scheduled(cron = "0 0 * * * *")
	public void process() {
		Collection<EpisodeStatus> episodes = episodeStatusDao.findByStatus(Status.WANTED);
		LOGGER.info("Starting wanted show processor with {} wanted episodes", episodes.size());
		findEpisodes(episodes);
		LOGGER.info("Wanted show processor done");
	}

	@Async
	public void process(List<Long> episodes) {
		findEpisodes(episodeStatusDao.findAll(episodes));
	}

	public boolean process(EpisodeStatus episode) {
		return findEpisodes(Arrays.asList(episode)).get(0);
	}

	private synchronized List<Boolean> findEpisodes(Iterable<EpisodeStatus> episodes) {
		List<Boolean> results = new ArrayList<>();
		for (EpisodeStatus episode : episodes) {
			LOGGER.debug("Try to retrieve episode {} {}-{}", episode.getEpisode().getTvShow().getName(), episode.getEpisode().getSeason(), episode.getEpisode().getEpisode());
			boolean found = searchAndGetEpisode(episode);
			if (found) {
				LOGGER.debug("Episode found");
			}
			results.add(found);
		}
		return results;
	}

	private boolean searchAndGetEpisode(EpisodeStatus episode) {
		TvShowConfiguration config = episode.getShowConfiguration();
		TvShow show = config.getTvShow();
		Torrent torrent = null;
		String providerUsed = null;
		for (IProvider provider : downloadService.getActiveProviders()) {
			try {
				Collection<String> namesToSearch = null;
				if (show.getCustomNames().isEmpty()) {
					namesToSearch = Arrays.asList(show.getOriginalName());
				} else {
					namesToSearch = new ArrayList<>(show.getCustomNames());
				}
				torrent = provider.findEpisode(namesToSearch, config.getAudioLang(), episode.getEpisode().getSeason(), episode.getEpisode().getEpisode(), config.getQuality(), episode.getDownloadedFiles());
				if (torrent != null) {
					LOGGER.info("Episode [{} {}-{}] found with provider [{}]", episode.getEpisode().getTvShow().getName(), episode.getEpisode().getSeason(), episode.getEpisode().getEpisode(), provider.getId());
					providerUsed = provider.getId();
					break;
				}
			} catch (IOException e) {
				LOGGER.error("Error while searching episode [{}] with provider [{}]", episode, provider.getId(), e);
			}
		}

		if (torrent != null) {
			IClient client = downloadService.getActiveClient();
			if (client != null) {
				LOGGER.info("Sending torrent to client [{}]", client.getId());
				try {
					final Torrent torrentToDownload = torrent;
					client.addTorrent(torrentToDownload, Paths.get(config.getLocation()), episode.getEpisode());

					// Update episode status to snatched
					episode.setStatus(Status.SNATCHED);
					episode.getDownloadedFiles().add(torrentToDownload.getName());
					episode.setDownloadDate(LocalDateTime.now());
					episode.setProvider(providerUsed);
					episodeStatusDao.save(episode);
					snatchedShowProcessor.episodeSnatched(episode);
					return true;
				} catch (IOException e) {
					LOGGER.error("Unable to send torrent to client", e);
				}
			} else {
				LOGGER.error("No torrent client defined to download search results");
			}
		}
		return false;
	}

}
