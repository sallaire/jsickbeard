package org.sallaire.controller;

import java.util.List;

import org.sallaire.dto.admin.AdminConfig;
import org.sallaire.dto.admin.AdminShow;
import org.sallaire.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping(value = "/admin/tvshows")
	public List<AdminShow> getAllShows() {
		return adminService.getAllShows();
	}

	@GetMapping(value = "/admin/tvshows/{id}/config")
	public List<AdminConfig> getAllShowConfigs(@PathVariable("id") Long id) {
		return adminService.getAllConfigs(id);
	}

}
