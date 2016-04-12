package org.sallaire.controller;

import java.util.Collection;
import java.util.List;

import org.sallaire.dto.metadata.Episode;
import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.dto.metadata.TvShow;
import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.service.DownloadService;
import org.sallaire.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TvShowController {

	@Autowired
	private ShowService showService;

	@Autowired
	private DownloadService downloadService;

	@RequestMapping(value = "/tvshow", method = RequestMethod.GET)
	public Collection<SearchResult> searchShow(@RequestParam("name") String name, @RequestParam("lang") String lang) {
		return showService.search(name, lang);
	}

	@RequestMapping(value = "/tvshow/{id}", method = RequestMethod.POST)
	public void addShow(@PathVariable("id") Long id, @RequestParam("location") String location, @RequestParam("status") String initalStatus, @RequestParam("quality") String quality, @RequestParam("audio") String audioLang, @RequestParam(value = "customName", required = false) List<String> customNames) {
		showService.add(id, location, initalStatus, quality, audioLang, customNames);
	}

	@RequestMapping(value = "/tvshow/{id}", method = RequestMethod.PUT)
	public void updateShow(@PathVariable("id") Long id, @RequestParam(value = "location", required = false) String location, @RequestParam(value = "quality", required = false) String quality, @RequestParam(value = "audio", required = false) String audioLang, @RequestParam(value = "customName", required = false) List<String> customNames) {
		showService.update(id, location, quality, audioLang, customNames);
	}

	@RequestMapping(value = "/tvshow/{id}", method = RequestMethod.GET)
	public TvShow getShow(@PathVariable("id") Long id) {
		return showService.getShow(id);
	}

	@RequestMapping(value = "/tvshow/{id}/episodes", method = RequestMethod.GET)
	public Collection<Episode> getEpisodes(@PathVariable("id") Long id) {
		return showService.getEpisodes(id);
	}

	@RequestMapping(value = "/tvshows", method = RequestMethod.GET)
	public Collection<TvShow> getShows() {
		return showService.getShows();
	}

	@RequestMapping(value = "/tvshow/{id}/episodes", method = RequestMethod.PUT)
	public void updateEpisodesStatus(@PathVariable("id") Long id, @RequestParam("id") List<Long> ids, @RequestParam("status") String status) {
		showService.updateEpisodesStatus(id, ids, status);
	}

	@RequestMapping(value = "/tvshow/{id}/episode/{epId}", method = RequestMethod.POST)
	public void searchEpisode(@PathVariable("id") Long id, @PathVariable("epId") Long episodeId) {
		showService.searchEpisode(id, episodeId);
	}

	@RequestMapping(value = "/tvshow/{id}/episode/{epId}", method = RequestMethod.DELETE)
	public void truncateDownloadedEpisode(@PathVariable("id") Long id, @PathVariable("epId") Long episodeId) {
		showService.truncateDownloadedEpisode(id, episodeId);
	}

	@RequestMapping(value = "/tvshow/{id}/episode/{season}/{episode}/status", method = RequestMethod.GET)
	public EpisodeStatus getEpisodeStatus(@PathVariable("id") Long id, @PathVariable("season") Integer season, @PathVariable("episode") Integer episode, @RequestParam("quality") String quality, @RequestParam("lang") String lang) {
		return downloadService.getEpisodeStatus(id, season, episode, quality, lang);
	}

	@RequestMapping(value = "/tvshow/episodes/status", method = RequestMethod.GET)
	public Collection<EpisodeStatus> getEpisodeStatus() {
		return downloadService.getEpisodeStatus();
	}
}
