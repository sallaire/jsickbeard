package org.sallaire.controller;

import java.util.Collection;
import java.util.List;

import org.sallaire.controller.conf.CurrentUser;
import org.sallaire.dto.api.FullShow;
import org.sallaire.dto.api.TvShowConfigurationParam;
import org.sallaire.dto.user.UserDto;
import org.sallaire.service.TvShowConfigurationService;
import org.sallaire.service.TvShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TvShowController {

	@Autowired
	private TvShowConfigurationService tvShowConfigurationService;

	@Autowired
	private TvShowService tvShowService;

	@PostMapping("/tvshow/config/{id}")
	public void upsertShow(@PathVariable("id") Long id, @RequestBody TvShowConfigurationParam showConfig, @CurrentUser UserDto currentUser) {
		tvShowConfigurationService.upsertConfiguration(id, showConfig, currentUser);
	}

	@PutMapping("/tvshow/{id}")
	public void updateShowNames(@PathVariable("id") Long id, @RequestBody List<String> customNames) {
		tvShowService.updateCustomNames(id, customNames);
	}

	@GetMapping("/tvshow/{id}")
	public ResponseEntity<FullShow> getFullShow(@PathVariable("id") Long id, @CurrentUser UserDto currentUser, @RequestParam(name = "fields", required = false) List<String> fields) {
		FullShow result = tvShowConfigurationService.getFullShow(id, currentUser, fields);
		if (result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@DeleteMapping("/tvshow/config/{id}")
	public void unfollow(@PathVariable("id") Long id, @CurrentUser UserDto currentUser) {
		tvShowConfigurationService.unfollow(id, currentUser);
	}

	@GetMapping("/tvshow")
	public Collection<FullShow> getShows(@CurrentUser UserDto currentUser) {
		return tvShowService.getShowsForUser(currentUser);
	}

}
