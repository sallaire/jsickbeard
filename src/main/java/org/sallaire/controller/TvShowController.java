package org.sallaire.controller;

import java.util.List;

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
	//
	// @RequestMapping(value = "/tvshow/{id}", method = RequestMethod.GET)
	// public String getShow(@PathVariable("id") Long id, Model uiModel) {
	// TvShow show = showDao.findOne(id);
	// uiModel.addAttribute("tvShow", show);
	// return "home";
	// }

	@RequestMapping(value = "/tvshow", method = RequestMethod.GET)
	public List<? extends ISearchResult> searchShow(@RequestParam("name") String name, @RequestParam("lang") String lang) {
		return showService.search(name, lang);
	}

	@RequestMapping(value = "/tvshow/{id}", method = RequestMethod.POST)
	public void addShow(@PathVariable("id") Long id, @RequestParam("location") String location, @RequestParam("status") String initalStatus, @RequestParam("quality") String quality, @RequestParam("audio") String audioLang) {
		showService.add(id, location, initalStatus, quality, audioLang);
	}

}
