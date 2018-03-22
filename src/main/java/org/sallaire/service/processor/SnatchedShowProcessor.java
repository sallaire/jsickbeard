package org.sallaire.service.processor;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.sallaire.dao.db.EpisodeStatusRepository;
import org.sallaire.dao.db.entity.EpisodeStatus;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.TvShowConfiguration;
import org.sallaire.dto.user.Status;
import org.sallaire.service.util.FileHelper.Finder;
import org.sallaire.service.util.RegexFilterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class SnatchedShowProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SnatchedShowProcessor.class);

	@Autowired
	private RegexFilterConfiguration regexFilter;

	@Autowired
	private EpisodeStatusRepository episodeStatusDao;

//	@Scheduled(cron = "0 15 * * * *")
	public void updateShow() {
		Collection<EpisodeStatus> episodes = episodeStatusDao.findByStatus(Status.SNATCHED);
		LOGGER.info("Starting snatched show processor with {} snatched episodes", episodes.size());
		for (EpisodeStatus episode : episodes) {
			LOGGER.debug("Search for snatched episode {}", episode);
			try {
				if (searchFile(episode)) {
					LOGGER.debug("Episode {} found, remove it from list of snatched episodes", episode.getEpisode());
					episode.setDownloadDate(LocalDateTime.now());
					episode.setStatus(Status.DOWNLOADED);
					episodeStatusDao.save(episode);
				}
			} catch (IOException e) {
				LOGGER.error("Error while checking downloaded file for episode {}", episode);
			}
		}
		LOGGER.info("Snatched show processor done");
	}

	@Async
	public void episodeSnatched(EpisodeStatus episode) {
		Path location = Paths.get(episode.getShowConfiguration().getLocation());
		try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
			Files.walkFileTree(location, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
					return FileVisitResult.CONTINUE;
				}
			});

			WatchKey key = null;
			boolean overflowDetected = false;
			do {
				// wait for key to be signaled
				try {
					key = watcher.poll(2L, TimeUnit.HOURS);

					for (WatchEvent<?> event : key.pollEvents()) {
						WatchEvent.Kind<?> kind = event.kind();

						// This key is registered only for ENTRY_CREATE events,
						// but an OVERFLOW event can occur regardless if events are lost or discarded.
						if (kind == StandardWatchEventKinds.OVERFLOW) {
							LOGGER.error("The Maximum watchService events has been reached !");
							overflowDetected = true;
						} else {
							@SuppressWarnings("unchecked")
							WatchEvent<Path> ev = (WatchEvent<Path>) event;
							if (ev.context() != null) {
								List<String> names = new ArrayList<>();
								if (!episode.getEpisode().getTvShow().getCustomNames().isEmpty()) {
									names.addAll(episode.getEpisode().getTvShow().getCustomNames());
								} else {
									names.add(episode.getEpisode().getTvShow().getOriginalName());
								}

								for (String name : names) {
									if (regexFilter.matchEpisode(ev.context().getFileName().toString(), name, episode.getEpisode().getSeason(), episode.getEpisode().getEpisode(), episode.getShowConfiguration().getQuality(), episode.getShowConfiguration().getAudioLang())) {
										LOGGER.info("File {} match episode {}, set it as downloaded", ev.context().getFileName(), episode.getEpisode());
										episode.setStatus(Status.DOWNLOADED);
										episode.setDownloadDate(LocalDateTime.now());
										episodeStatusDao.save(episode);
										return;
									} else {
										LOGGER.debug("File {} does not match episode {}", ev.context().getFileName(), episode.getEpisode());
									}
								}
							}
						}
					}

					// Check if an overflow event was detected
					if (overflowDetected) {
						// We need to threat current files into input folder, because watcher service don't catch them
						if (searchFile(episode)) {
							LOGGER.debug("Episode {} found, set it as downloaded", episode.getEpisode());
							episode.setStatus(Status.DOWNLOADED);
							episode.setDownloadDate(LocalDateTime.now());
							episodeStatusDao.save(episode);
							return;
						}
					}

					// Reset the key -- this step is critical if you want to receive further watch events.
					// If the key is no longer valid, the directory is inaccessible so exit the loop.
					boolean valid = key.reset();
					if (!valid) {
						LOGGER.error("Unable to watch folder, it may be inaccessible");
						return;
					}
				} catch (InterruptedException x) {
					LOGGER.error("Unable to watch folder, it may be inaccessible", x);
					return;
				} catch (ClosedWatchServiceException e) {
					LOGGER.error("WatchService was closed, ending service", e);
					return;
				}
			} while (key != null);

			// If we reach this point, no files has been found since 2 hours, there must have been an error during torrent download/copy
			LOGGER.error("Unable to find file corresponding to episode {} in directory {}", episode.getEpisode(), location);
			episode.setStatus(Status.ERROR);
			episodeStatusDao.save(episode);
		} catch (IOException e) {
			LOGGER.error("Unable to watch folder {}", location, e);
		}
	}

	private boolean searchFile(EpisodeStatus episode) throws IOException {
		if (episode.getDownloadedFiles() != null) {
			TvShowConfiguration showConfig = episode.getShowConfiguration();
			TvShow tvShow = showConfig.getTvShow();
			String location = showConfig.getLocation();
			boolean filesFound = true;
			LOGGER.debug("Try to find file in directory [{}]", location);
			Finder finder = null;
			if (CollectionUtils.isNotEmpty(tvShow.getCustomNames())) {
				finder = new Finder(regexFilter, episode.getEpisode().getSeason(), episode.getEpisode().getEpisode(), showConfig.getAudioLang(), showConfig.getQuality(), tvShow.getCustomNames().toArray(new String[tvShow.getCustomNames().size()]));
			} else {
				finder = new Finder(regexFilter, episode.getEpisode().getSeason(), episode.getEpisode().getEpisode(), showConfig.getAudioLang(), showConfig.getQuality(), tvShow.getOriginalName());
			}
			Files.walkFileTree(Paths.get(location), finder);
			filesFound &= finder.isFound();
			return filesFound;
		} else {
			LOGGER.warn("Episode {} is set to SNATCHED, but no file is associated", episode);
			return false;
		}
	}
}
