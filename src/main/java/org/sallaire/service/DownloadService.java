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

import org.sallaire.dao.db.ConfigurationDao;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dao.db.UserDao;
import org.sallaire.dto.configuration.ClientConfiguration;
import org.sallaire.dto.configuration.ProviderConfiguration;
import org.sallaire.dto.metadata.TvShow;
import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.dto.user.Status;
import org.sallaire.dto.user.TvShowConfiguration;
import org.sallaire.service.client.IClient;
import org.sallaire.service.provider.IProvider;
import org.sallaire.service.provider.Torrent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

	@Autowired
	private List<IClient> clients;

	@Autowired
	private List<IProvider> providers;

	@Autowired
	private TvShowDao showDao;

	@Autowired
	private UserDao userDao;

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
				// Init provider with its parameters
				provider.configurationChanged(config.getParameters());
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

	public boolean searchAndGetEpisode(EpisodeStatus episode) {
		TvShowConfiguration config = userDao.getShowConfiguration(episode.getEpisodeKey().getShowId());
		TvShow show = showDao.getShow(episode.getEpisodeKey().getShowId());
		Torrent torrent = null;
		for (IProvider provider : providers) {
			if (activatedProviders.contains(provider.getId())) {
				try {
					torrent = provider.findEpisode(show.getName(), episode.getEpisodeKey().getLang(), episode.getEpisodeKey().getSeason(), episode.getEpisodeKey().getNumber(), episode.getEpisodeKey().getQuality(), episode.getFileNames());
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
					final Torrent torrentToDownload = torrent;
					client.addTorrent(torrentToDownload, config, episode);

					// Update episode status to snatched
					episode.setStatus(Status.SNATCHED);
					userDao.saveEpisodeStatus(episode);
					userDao.saveSnatchedEpisode(episode);
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

	public Map<String, String> getProviderConfiguration(String id) {
		LOGGER.info("Get {} provider information", id);
		ProviderConfiguration config = configurationDao.getProvider(id);
		if (config != null) {
			LOGGER.info("Provider configuration found");
			return config.getParameters();
		} else {
			LOGGER.info("Provider configuration not found");
			return new HashMap<>();
		}
	}

	public ClientConfiguration getClientConfiguration() {
		return configurationDao.getClientConfiguration();
	}

	public void saveProvider(String id, Map<String, String> parameters) {
		LOGGER.info("Saving provider {} with params {}", id, parameters);
		ProviderConfiguration config = configurationDao.getProvider(id);
		if (config != null) {
			config.setParameters(parameters);
		} else {
			config = new ProviderConfiguration();
			config.setActivated(false);
			config.setOrder(-1);
			config.setParameters(parameters);
		}
		LOGGER.debug("Save provider configuration to DB");
		configurationDao.saveProvider(id, config);
		LOGGER.debug("Provider configuration saved to DB");

		LOGGER.debug("Update provider configuration in provider singleton");
		providers.stream().filter(p -> p.getId().equals(id)).forEach(p -> p.configurationChanged(parameters));
		LOGGER.debug("Provider singleton updated");
		LOGGER.info("Provider {} saved", id);
	}

	public void saveProviders(LinkedHashMap<String, Boolean> parameters) {
		LOGGER.info("Save providers configuration");
		int i = 0;
		Map<String, Integer> providersOrder = new HashMap<>();
		for (Entry<String, Boolean> entry : parameters.entrySet()) {
			String id = entry.getKey();
			LOGGER.debug("Retrieve provider {} configuration in DB", id);
			ProviderConfiguration conf = configurationDao.getProvider(id);
			if (conf == null) {
				LOGGER.debug("Provider {} configuration doesn't exist yet, create it", id);
				conf = new ProviderConfiguration();
			}
			conf.setActivated(entry.getValue());
			conf.setOrder(i++);
			LOGGER.debug("Provider {} is now in position {} and is activated : {}", id, conf.getOrder(), conf.isActivated());
			configurationDao.saveProvider(entry.getKey(), conf);
			LOGGER.debug("Provider saved in DB");
			if (conf.isActivated()) {
				activatedProviders.add(id);
			} else {
				activatedProviders.remove(id);
			}
			providersOrder.put(id, conf.getOrder());
		}
		LOGGER.debug("Sort providers singletons");
		sortProviders(providersOrder);
		LOGGER.info("Providers configuration saved");
	}

	public void saveClient(ClientConfiguration config) {
		LOGGER.info("Saving client configuration {}", config.getClient());
		LOGGER.debug("Save client configuration to DB");
		configurationDao.saveClientConfiguration(config);
		LOGGER.debug("Client configuration saved to DB");
		LOGGER.debug("Update client singleton");
		clients.stream().filter(c -> c.getId().equals(config.getClient())).forEach(c -> client = c);
		LOGGER.debug("Client singleton updated with {}", client.getId());
		LOGGER.debug("Update client configuration in singleton");
		client.configurationChanged(config);
		LOGGER.debug("Client configuration updated in singleton");
		LOGGER.info("Client configuration {} saved", config.getClient());
	}
}
