package org.sallaire.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class DownloadController {

	// @Autowired
	// private ShowService showService;
	//
	// @Autowired
	// private DownloadService downloadService;
	//
	// @Autowired
	// private EpisodeConverter episodeConverter;
	//
	// @RequestMapping(value = "/episodes/snatched", method = RequestMethod.GET)
	// public Collection<FullEpisode> getSnatchedEpisodes() {
	// return episodeConverter.convertEpisodesStatus(downloadService.getSnatchedEpisodes());
	// }
	//
	// @RequestMapping(value = "/episodes/wanted", method = RequestMethod.GET)
	// public Collection<FullEpisode> getWantedEpisodes() {
	// return episodeConverter.convertEpisodesStatus(downloadService.getWantedEpisodes());
	// }
	//
	// @RequestMapping(value = "/episodes/downloaded", method = RequestMethod.GET)
	// public Collection<FullEpisode> getDownloadedEpisodes(@RequestParam("from") int from, @RequestParam("length") int length) {
	// return episodeConverter.convertEpisodesStatus(downloadService.getDownloadedEpisodes(from, length));
	// }
	//
	// @RequestMapping(value = "/episodes/upcoming", method = RequestMethod.GET)
	// public Collection<FullEpisode> getUpComingEpisodes(@RequestParam("from") int from, @RequestParam("length") int length) {
	// return episodeConverter.convertEpisodes(showService.getUpcomingEpisodes(from, length));
	// }
	//
	// @RequestMapping(value = "/episode/{showId}/{season}/{number}/snatched", method = RequestMethod.DELETE)
	// public void deleteSnatchedEpisodes(@PathVariable("showId") Long showId, @PathVariable("season") Integer season, @PathVariable("number") Integer number, @RequestParam("quality") String quality,
	// @RequestParam("lang") String lang) {
	// downloadService.deleteSnatchedEpisode(showId, season, number, quality, lang);
	// }
}
