package org.sallaire.controller;

import org.sallaire.dto.admin.JsonResults;
import org.sallaire.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

	@Autowired
	private AdminService adminService;
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

	@GetMapping("/admin/upload/stats")
	public JsonResults getUploadStats() {
		return adminService.getUploadStats();
	}
}
