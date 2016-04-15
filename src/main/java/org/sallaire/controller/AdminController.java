package org.sallaire.controller;

import java.util.Collection;

import org.sallaire.dto.user.EpisodeStatus;
import org.sallaire.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

	@Autowired
	private DownloadService downloadService;

	@RequestMapping(value = "/admin/snatched", method = RequestMethod.GET)
	public Collection<EpisodeStatus> getSnatchedEpisodes() {
		return downloadService.getSnatchedEpisodes();
	}

	@RequestMapping(value = "/admin/wanted", method = RequestMethod.GET)
	public Collection<EpisodeStatus> getWantedEpisodes() {
		return downloadService.getWantedEpisodes();
	}

	@RequestMapping(value = "/admin/snatched", method = RequestMethod.DELETE)
	public void deleteSnatchedEpisodes() {
		downloadService.deleteSnatchedEpisodes();
	}
}
