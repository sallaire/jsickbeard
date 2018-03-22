package org.sallaire.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.sallaire.controller.conf.CurrentUser;
import org.sallaire.dto.api.EpisodeDto;
import org.sallaire.dto.api.ShowDto;
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

	@PostMapping("/tvshow/{id}/config")
	public void upsertShow(@PathVariable("id") Long id, @RequestBody TvShowConfigurationParam showConfig, @CurrentUser UserDto currentUser) {
		tvShowConfigurationService.upsertConfiguration(id, showConfig, currentUser);
	}

	@PutMapping("/tvshow/{id}")
	public void updateShowNames(@PathVariable("id") Long id, @RequestBody List<String> customNames) {
		tvShowService.updateCustomNames(id, customNames);
	}

	@GetMapping("/tvshow/{id}")
	public ResponseEntity<ShowDto> getFullShow(@PathVariable("id") Long id, @CurrentUser UserDto currentUser, @RequestParam(name = "fields", required = false) List<String> fields) {
		ShowDto result = tvShowConfigurationService.getShow(id, currentUser, fields);
		if (result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@PostMapping("/tvshow/{id}")
	public void updateShow(@PathVariable("id") Long id) {
		tvShowService.updateShow(id);
	}

	@DeleteMapping("/tvshow/{id}/config")
	public void unfollow(@PathVariable("id") Long id, @CurrentUser UserDto currentUser) {
		tvShowConfigurationService.unfollow(id, currentUser);
	}

	@GetMapping("/tvshow")
	public Collection<ShowDto> getShows(@CurrentUser UserDto currentUser) {
		return tvShowConfigurationService.getShowsForUser(currentUser);
	}

	@GetMapping("/tvshows")
	public Collection<ShowDto> findShows(@CurrentUser UserDto currentUser, @RequestParam("name") String name, @RequestParam("lang") String lang) {
		return tvShowService.find(currentUser, name, lang);
	}

	@GetMapping("/tvshows/{id}")
	public ResponseEntity<ShowDto> getShow(@CurrentUser UserDto currentUser, @PathVariable("id") Long id) {
		ShowDto result = tvShowConfigurationService.getShow(id, currentUser, Arrays.asList("tvshow", "config"));
		if (result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/tvshows/{id}/season/{season}")
	public List<EpisodeDto> getSeason(@CurrentUser UserDto currentUser, @PathVariable("id") Long id, @PathVariable("season") Integer season) {
		return tvShowConfigurationService.getSeason(currentUser, id, season);
	}

	@GetMapping("/tvshows/{id}/season/{season}/episode/{episode}")
	public EpisodeDto getEpisode(@CurrentUser UserDto currentUser, @PathVariable("id") Long id, @PathVariable("season") Integer season, @PathVariable("episode") Integer episode) {
		return tvShowConfigurationService.getEpisode(currentUser, id, season, episode);
	}

}
