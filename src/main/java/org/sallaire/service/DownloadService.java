// package org.sallaire.service;
//
// import java.util.Collection;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Map.Entry;
// import java.util.Set;
// import java.util.stream.Collectors;
//
// import javax.annotation.PostConstruct;
//
// import org.sallaire.dao.db.ConfigurationDao;
// import org.sallaire.dao.db.DownloadDao;
// import org.sallaire.dto.configuration.ClientConfiguration;
// import org.sallaire.dto.configuration.ProviderConfiguration;
// import org.sallaire.dto.user.EpisodeKey;
// import org.sallaire.dto.user.EpisodeStatus;
// import org.sallaire.dto.user.Quality;
// import org.sallaire.service.client.IClient;
// import org.sallaire.service.provider.IProvider;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// @Service
// public class DownloadService {
//
// private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);
//
// @Autowired
// private List<IClient> clients;
//
// @Autowired
// private List<IProvider> providers;
//
// @Autowired
// private DownloadDao downloadDao;
//
// @Autowired
// private ConfigurationDao configurationDao;
//
// private Set<String> activatedProviders;
//
// private IClient client;
//
// @PostConstruct
// private void init() {
// // Sort and check activated providers
// activatedProviders = new HashSet<>();
// Map<String, Integer> providersOrder = new HashMap<>();
// for (IProvider provider : providers) {
// ProviderConfiguration config = configurationDao.getProvider(provider.getId());
// if (config != null) {
// // Init provider with its parameters
// provider.configurationChanged(config.getParameters());
// providersOrder.put(provider.getId(), config.getOrder());
// if (config.isActivated()) {
// activatedProviders.add(provider.getId());
// }
// } else {
// providersOrder.put(provider.getId(), -1);
// }
// }
// sortProviders(providersOrder);
//
// // Initialize client
// ClientConfiguration config = configurationDao.getClientConfiguration();
// if (config != null) {
// clients.stream().filter(c -> c.getId().equals(config.getClient())).forEach(c -> client = c);
// client.configurationChanged(config);
// }
//
// }
//
// private void sortProviders(Map<String, Integer> providersOrder) {
// providers.sort((p1, p2) -> Integer.compare(providersOrder.get(p1.getId()), providersOrder.get(p2.getId())));
// }
//
// public Map<String, String> getProviderConfiguration(String id) {
// LOGGER.info("Get {} provider information", id);
// ProviderConfiguration config = configurationDao.getProvider(id);
// if (config != null) {
// LOGGER.info("Provider configuration found");
// return config.getParameters();
// } else {
// LOGGER.info("Provider configuration not found");
// return new HashMap<>();
// }
// }
//
// public ClientConfiguration getClientConfiguration() {
// return configurationDao.getClientConfiguration();
// }
//
// public void saveProvider(String id, Map<String, String> parameters) {
// LOGGER.info("Saving provider {} with params {}", id, parameters);
// ProviderConfiguration config = configurationDao.getProvider(id);
// if (config != null) {
// config.setParameters(parameters);
// } else {
// config = new ProviderConfiguration();
// config.setActivated(false);
// config.setOrder(-1);
// config.setParameters(parameters);
// }
// LOGGER.debug("Save provider configuration to DB");
// configurationDao.saveProvider(id, config);
// LOGGER.debug("Provider configuration saved to DB");
//
// LOGGER.debug("Update provider configuration in provider singleton");
// providers.stream().filter(p -> p.getId().equals(id)).forEach(p -> p.configurationChanged(parameters));
// LOGGER.debug("Provider singleton updated");
// LOGGER.info("Provider {} saved", id);
// }
//
// public void saveProviders(LinkedHashMap<String, Boolean> parameters) {
// LOGGER.info("Save providers configuration");
// int i = 0;
// Map<String, Integer> providersOrder = new HashMap<>();
// for (Entry<String, Boolean> entry : parameters.entrySet()) {
// String id = entry.getKey();
// LOGGER.debug("Retrieve provider {} configuration in DB", id);
// ProviderConfiguration conf = configurationDao.getProvider(id);
// if (conf == null) {
// LOGGER.debug("Provider {} configuration doesn't exist yet, create it", id);
// conf = new ProviderConfiguration();
// }
// conf.setActivated(entry.getValue());
// conf.setOrder(i++);
// LOGGER.debug("Provider {} is now in position {} and is activated : {}", id, conf.getOrder(), conf.isActivated());
// configurationDao.saveProvider(entry.getKey(), conf);
// LOGGER.debug("Provider saved in DB");
// if (conf.isActivated()) {
// activatedProviders.add(id);
// } else {
// activatedProviders.remove(id);
// }
// providersOrder.put(id, conf.getOrder());
// }
// LOGGER.debug("Sort providers singletons");
// sortProviders(providersOrder);
// LOGGER.info("Providers configuration saved");
// }
//
// public void saveClient(ClientConfiguration config) {
// LOGGER.info("Saving client configuration {}", config.getClient());
// LOGGER.debug("Save client configuration to DB");
// configurationDao.saveClientConfiguration(config);
// LOGGER.debug("Client configuration saved to DB");
// LOGGER.debug("Update client singleton");
// clients.stream().filter(c -> c.getId().equals(config.getClient())).forEach(c -> client = c);
// LOGGER.debug("Client singleton updated with {}", client.getId());
// LOGGER.debug("Update client configuration in singleton");
// client.configurationChanged(config);
// LOGGER.debug("Client configuration updated in singleton");
// LOGGER.info("Client configuration {} saved", config.getClient());
// }
//
// public Collection<EpisodeStatus> getSnatchedEpisodes() {
// LOGGER.info("Get all snatched episodes");
// return downloadDao.getSnatchedEpisodes();
// }
//
// public Collection<EpisodeStatus> getWantedEpisodes() {
// LOGGER.info("Get all wanted episodes");
// return downloadDao.getWantedEpisodes();
// }
//
// public Collection<EpisodeStatus> getDownloadedEpisodes(int from, int length) {
// return downloadDao.getDownloadedEpisodes().stream() //
// .sorted((e1, e2) -> e2.getDownloadDate().compareTo(e1.getDownloadDate())) //
// .skip(from).limit(length) //
// .collect(Collectors.toList());
// }
//
// public void deleteSnatchedEpisodes() {
// downloadDao.dropSnatchedEpisodes();
// }
//
// public void deleteSnatchedEpisode(Long showId, Integer season, Integer number, String quality, String lang) {
// try {
// Quality convertedQuality = Quality.valueOf(quality);
// EpisodeKey episodeKey = new EpisodeKey(showId, season, number, convertedQuality, lang);
// downloadDao.removeSnatchedEpisode(episodeKey);
// } catch (IllegalArgumentException e) {
// LOGGER.error("Unable to delete snatched episode, quality [{}] could not be resolved", quality, e);
// }
// }
//
// public EpisodeStatus getEpisodeStatus(Long showId, Integer season, Integer episode, String quality, String lang) {
// return downloadDao.getEpisodeStatus(new EpisodeKey(showId, season, episode, Quality.valueOf(quality), lang));
// }
//
// public Collection<EpisodeStatus> getEpisodeStatus() {
// return downloadDao.getEpisodeStatus();
// }
//
// public IClient getActiveClient() {
// return client;
// }
//
// public List<IProvider> getActiveProviders() {
// return providers.stream().filter(p -> activatedProviders.contains(p.getId())).collect(Collectors.toList());
// }
// }
