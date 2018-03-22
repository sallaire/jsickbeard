package org.sallaire.controller;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.sallaire.controller.conf.CurrentUser;
import org.sallaire.dto.api.EpisodeDto;
import org.sallaire.dto.api.UpdateEpisodeStatusParam;
import org.sallaire.dto.user.Status;
import org.sallaire.dto.user.UserDto;
import org.sallaire.service.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EpisodeController {

	@Autowired
	private EpisodeService episodeService;

	@PutMapping(value = "/episode")
	public ResponseEntity<String> updateEpisodesStatus(@RequestBody UpdateEpisodeStatusParam params, @CurrentUser UserDto currentUser) {
		if (params.getStatus() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required String parameter 'status' is not present");
		}
		if (CollectionUtils.isEmpty(params.getIds())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required List<Long> parameter 'ids' is not present");
		}
		episodeService.updateEpisodesStatus(params, currentUser);
		return ResponseEntity.ok("");
	}
	
	@PutMapping(value = "/episode/{id}")
	public ResponseEntity<Void> updateEpisodeStatus(@PathVariable("id") Long id, @RequestParam("status") Status status, @CurrentUser UserDto currentUser) {
		if (episodeService.updateEpisodeStatus(id, status, currentUser)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	//
	// @RequestMapping(value = "/tvshow/{id}/episode/{epId}", method = RequestMethod.POST)
	// public void searchEpisode(@PathVariable("id") Long id, @PathVariable("epId") Long episodeId) {
	// showService.searchEpisode(id, episodeId);
	// }
	//
	// @RequestMapping(value = "/tvshow/{id}/episode/{epId}", method = RequestMethod.DELETE)
	// public void truncateDownloadedEpisode(@PathVariable("id") Long id, @PathVariable("epId") Long episodeId) {
	// showService.truncateDownloadedEpisode(id, episodeId);
	// }

	//
	@GetMapping(value = "/episode")
	public Collection<EpisodeDto> getEpisodesForStatus(@CurrentUser UserDto currentUser, @RequestParam("status") Status status, @RequestParam("from") int from, @RequestParam("length") int length) {
		return episodeService.getEpisodesForStatus(currentUser, status, from, length);
	}

}
