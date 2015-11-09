package org.sallaire.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.sallaire.client.IClient;
import org.sallaire.dao.db.ConfigurationDao;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dto.ClientConfiguration;
import org.sallaire.dto.Episode;
import org.sallaire.dto.Episode.Status;
import org.sallaire.dto.ProviderConfiguration;
import org.sallaire.dto.TvShow;
import org.sallaire.dto.TvShowConfiguration;
import org.sallaire.provider.IProvider;
import org.sallaire.provider.Torrent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TorrentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TorrentService.class);

	@Autowired
	private List<IClient> clients;

	@Autowired
	private List<IProvider> providers;

	@Autowired
	private TvShowDao showDao;

	@Autowired
	private ConfigurationDao configurationDao;

	private Set<String> activatedProviders;

	private IClient client;

	@PostConstruct
	private void init() {
		// Sort and check activated providers
		activatedProviders = new HashSet<>();
		Map<String, Integer> providersOrder = new HashMap<>();
		for (IProvider provider : providers) {
			ProviderConfiguration config = configurationDao.getProvider(provider.getId());
			if (config != null) {
				providersOrder.put(provider.getId(), config.getOrder());
				if (config.isActivated()) {
					activatedProviders.add(provider.getId());
				}
			} else {
				providersOrder.put(provider.getId(), -1);
			}
		}
		sortProviders(providersOrder);

		// Initialize client
		ClientConfiguration config = configurationDao.getClientConfiguration();
		if (config != null) {
			clients.stream().filter(c -> c.getId().equals(config.getClient())).forEach(c -> client = c);
			client.configurationChanged(config);
		}

	}

	private void sortProviders(Map<String, Integer> providersOrder) {
		providers.sort((p1, p2) -> Integer.compare(providersOrder.get(p1.getId()), providersOrder.get(p2.getId())));
	}

	public void searchAndGetEpisode(Episode episode) {
		TvShowConfiguration config = showDao.getShowConfiguration(episode.getShowId());
		TvShow show = showDao.getShow(episode.getShowId());
		Torrent torrent = null;
		for (IProvider provider : providers) {
			if (activatedProviders.contains(provider.getId())) {
				try {
					torrent = provider.findEpisode(show.getName(), config.getAudioLang(), episode.getSeason(), episode.getEpisode(), config.getQuality());
					if (torrent != null) {
						LOGGER.info("Episode [{}] found with provider [{}]", episode, provider.getId());
						break;
					}
				} catch (IOException e) {
					LOGGER.error("Error while searching episode [{}] with provider [{}]", episode, provider.getId(), e);
				}
			}
		}

		if (torrent != null) {
			if (client != null) {
				LOGGER.info("Sendind torrent to client [{}]", client.getId());
				try {
					client.addTorrent(torrent, config, episode);

					// Update episode status to snatched
					episode.setStatus(Status.SNATCHED);
					List<Episode> episodes = showDao.getShowEpisodes(episode.getShowId());
					episodes.stream().filter(e -> e.getId().equals(episode.getShowId())).forEach(e -> e.setStatus(Status.SNATCHED));
					showDao.saveShowEpisodes(episode.getShowId(), episodes);
				} catch (IOException e) {
					LOGGER.error("Unable to send torrent to client", e);
				}
			} else {
				LOGGER.error("No torrent client defined to download search results");
			}
		}
	}

	public Map<String, String> getProviderConfiguration(String id) {
		ProviderConfiguration config = configurationDao.getProvider(id);
		if (config != null) {
			return config.getParameters();
		} else {
			return new HashMap<>();
		}
	}

	public ClientConfiguration getClientConfiguration() {
		return configurationDao.getClientConfiguration();
	}

	public void saveProvider(String id, Map<String, String> parameters) {
		ProviderConfiguration config = configurationDao.getProvider(id);
		if (config != null) {
			config.setParameters(parameters);
		} else {
			config = new ProviderConfiguration();
			config.setActivated(false);
			config.setOrder(-1);
			config.setParameters(parameters);
		}
		configurationDao.saveProvider(id, config);
		providers.stream().filter(p -> p.getId().equals(id)).forEach(p -> p.configurationChanged(parameters));
	}

	public void saveProviders(LinkedHashMap<String, Boolean> parameters) {
		int i = 0;
		Map<String, Integer> providersOrder = new HashMap<>();
		for (Entry<String, Boolean> entry : parameters.entrySet()) {
			String id = entry.getKey();
			ProviderConfiguration conf = configurationDao.getProvider(id);
			if (conf == null) {
				conf = new ProviderConfiguration();
			}
			conf.setActivated(entry.getValue());
			conf.setOrder(i++);
			configurationDao.saveProvider(entry.getKey(), conf);
			if (conf.isActivated()) {
				activatedProviders.add(id);
			} else {
				activatedProviders.remove(id);
			}
			providersOrder.put(id, conf.getOrder());
		}
		sortProviders(providersOrder);
	}

	public void saveClient(ClientConfiguration config) {
		configurationDao.saveClientConfiguration(config);
		clients.stream().filter(c -> c.getId().equals(config.getClient())).forEach(c -> client = c);
		client.configurationChanged(config);
	}
}
