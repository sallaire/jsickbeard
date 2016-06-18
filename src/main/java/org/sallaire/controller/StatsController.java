package org.sallaire.controller;

import org.sallaire.dto.stats.ChartData;
import org.sallaire.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

	@Autowired
	private StatsService adminService;
	//
	// @RequestMapping(value = "/admin/snatched", method = RequestMethod.GET)
	// public Collection<EpisodeStatus> getSnatchedEpisodes() {
	// return downloadService.getSnatchedEpisodes();
	// }
	//
	// @RequestMapping(value = "/admin/wanted", method = RequestMethod.GET)
	// public Collection<EpisodeStatus> getWantedEpisodes() {
	// return downloadService.getWantedEpisodes();
	// }
	//
	// @RequestMapping(value = "/admin/snatched", method = RequestMethod.DELETE)
	// public void deleteSnatchedEpisodes() {
	// downloadService.deleteSnatchedEpisodes();
	// }

	@GetMapping("/stats/upload")
	public ChartData getUploadStats(@RequestParam(name = "filter", required = false) Long filter) {
		return adminService.getUploadStats(filter);
	}
}
