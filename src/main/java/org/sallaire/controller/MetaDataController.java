package org.sallaire.controller;

import java.util.Collection;

import org.sallaire.dto.metadata.Episode;
import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.dto.metadata.TvShow;
import org.sallaire.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetaDataController {

	@Autowired
	private ShowService showService;

	@RequestMapping(value = "/metadata/tvshow/{id}", method = RequestMethod.POST)
	public void updateShowMetadata(@PathVariable("id") Long showId) {
		showService.updateShowMetadata(showId);
	}

	@RequestMapping(value = "/metadata/tvshow", method = RequestMethod.GET)
	public Collection<SearchResult> searchShow(@RequestParam("name") String name, @RequestParam("lang") String lang) {
		return showService.search(name, lang);
	}

	@RequestMapping(value = "/metadata/tvshow/{id}", method = RequestMethod.GET)
	public TvShow getShow(@PathVariable("id") Long id) {
		return showService.getShow(id);
	}

	@RequestMapping(value = "/metadata/tvshow/{id}/episodes", method = RequestMethod.GET)
	public Collection<Episode> getEpisodes(@PathVariable("id") Long id) {
		return showService.getEpisodes(id);
	}

}
