package org.sallaire.controller;

import org.sallaire.dao.db.TvShowRepository;
import org.sallaire.dao.db.entity.TvShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TvShowController {
	@Autowired
	private TvShowRepository showDao;

	@RequestMapping(value = "/tvshow/{id}", method = RequestMethod.GET)
	public String getShow(@PathVariable("id") Long id, Model uiModel) {
		TvShow show = showDao.findOne(id);
		uiModel.addAttribute("tvShow", show);
		return "home";
	}
}
