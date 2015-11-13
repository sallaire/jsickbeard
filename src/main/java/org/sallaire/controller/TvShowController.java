package org.sallaire.controller;

import java.util.Collection;
import java.util.List;

import org.sallaire.dto.Episode;
import org.sallaire.dto.TvShow;
import org.sallaire.dto.tvdb.ISearchResult;
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

	@RequestMapping(value = "/tvshow", method = RequestMethod.GET)
	public Collection<? extends ISearchResult> searchShow(@RequestParam("name") String name, @RequestParam("lang") String lang) {
		return showService.search(name, lang);
	}

	@RequestMapping(value = "/tvshow/{id}", method = RequestMethod.POST)
	public void addShow(@PathVariable("id") Long id, @RequestParam("location") String location, @RequestParam("status") String initalStatus, @RequestParam("quality") String quality, @RequestParam("audio") String audioLang) {
		showService.add(id, location, initalStatus, quality, audioLang);
	}

	@RequestMapping(value = "/tvshow/{id}", method = RequestMethod.GET)
	public TvShow getShow(@PathVariable("id") Long id) {
		return showService.getShow(id);
	}

	@RequestMapping(value = "/tvshow/{id}/episodes", method = RequestMethod.GET)
	public List<Episode> getEpisodes(@PathVariable("id") Long id) {
		return showService.getEpisodes(id);
	}

	@RequestMapping(value = "/tvshows", method = RequestMethod.GET)
	public Collection<TvShow> getShows() {
		return showService.getShows();
	}

}
