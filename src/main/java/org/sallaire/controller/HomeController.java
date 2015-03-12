package org.sallaire.controller;

import org.sallaire.dao.db.TvShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	@Autowired
	private TvShowRepository showDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model uiModel) {
		return "home";
	}

	@RequestMapping(value = "/home/addShows", method = RequestMethod.GET)
	public String addShow(Model uiModel) {
		return "home_addShow";
	}
}
