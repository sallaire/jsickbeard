package org.sallaire.controller;

import java.util.Collection;

import org.sallaire.dto.metadata.SearchResult;
import org.sallaire.service.TvShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetaDataController {

	@Autowired
	private TvShowService showService;

	// @RequestMapping(value = "/metadata/tvshow/{id}", method = RequestMethod.POST)
	// public void updateShowMetadata(@PathVariable("id") Long showId) {
	// showService.updateShowMetadata(showId);
	// }
	//
	@GetMapping(value = "/metadata/tvshow")
	public Collection<SearchResult> searchShow(@RequestParam("name") String name, @RequestParam("lang") String lang) {
		return showService.search(name, lang);
	}
	//
	// @RequestMapping(value = "/metadata/tvshow/{id}", method = RequestMethod.GET)
	// public TvShow getShow(@PathVariable("id") Long id) {
	// return showService.getShow(id);
	// }
	//
	// @RequestMapping(value = "/metadata/tvshow/{id}/episodes", method = RequestMethod.GET)
	// public Collection<Episode> getEpisodes(@PathVariable("id") Long id) {
	// return showService.getEpisodes(id);
	// }

}
