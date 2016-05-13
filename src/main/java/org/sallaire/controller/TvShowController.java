package org.sallaire.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TvShowController {

	// @Autowired
	// private TvShowService tvShowService;

	//
	// @Autowired
	// private DownloadService downloadService;
	//

	// @PostMapping(value = "/tvshow/config/{id}")
	// public void upsertShow(@PathVariable("id") Long id, @RequestBody TvShowConfigurationParam showConfig, @CurrentUser User currentUser) {
	// tvShowService.upsertShow(id, showConfig, currentUser);
	// }
	//
	// @RequestMapping(value = "/tvshow/{id}", method = RequestMethod.GET)
	// public ResponseEntity<FullShow> getFullShow(@PathVariable("id") Long id) {
	// FullShow result = showService.getFullShow(id);
	// if (result != null) {
	// return ResponseEntity.ok(result);
	// } else {
	// return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	// }
	// }
	//
	// @RequestMapping(value = "/tvshow/config/{id}", method = RequestMethod.GET)
	// public TvShowConfiguration getShowConfig(@PathVariable("id") Long id) {
	// return showService.getTvShowConfiguration(id);
	// }
	//
	// @RequestMapping(value = "/tvshow/{id}", method = RequestMethod.DELETE)
	// public void unFollowShow(@PathVariable("id") Long id) {
	// showService.unFollowShow(id);
	// }
	//
	// @RequestMapping(value = "/tvshows", method = RequestMethod.GET)
	// public Collection<TvShow> getShows() {
	// return showService.getShows();
	// }
	//
	// @RequestMapping(value = "/tvshow/{id}/episodes", method = RequestMethod.PUT)
	// public ResponseEntity<String> updateEpisodesStatus(@PathVariable("id") Long id, @RequestBody UpdateEpisodeStatusParam params) {
	// if (params.getStatus() == null) {
	// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required String parameter 'status' is not present");
	// }
	// if (CollectionUtils.isEmpty(params.getIds())) {
	// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required List<Long> parameter 'ids' is not present");
	// }
	// showService.updateEpisodesStatus(id, params);
	// return ResponseEntity.ok("");
	// }
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
	// @RequestMapping(value = "/tvshow/{id}/episode/{season}/{episode}/status", method = RequestMethod.GET)
	// public EpisodeStatus getEpisodeStatus(@PathVariable("id") Long id, @PathVariable("season") Integer season, @PathVariable("episode") Integer episode, @RequestParam("quality") String quality,
	// @RequestParam("lang") String lang) {
	// return downloadService.getEpisodeStatus(id, season, episode, quality, lang);
	// }
	//
	// @RequestMapping(value = "/tvshow/episodes/status", method = RequestMethod.GET)
	// public Collection<EpisodeStatus> getEpisodeStatus() {
	// return downloadService.getEpisodeStatus();
	// }
}
