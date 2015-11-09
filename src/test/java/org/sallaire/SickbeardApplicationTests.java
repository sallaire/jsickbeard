package org.sallaire;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.dto.Episode;
import org.sallaire.dto.Episode.Status;
import org.sallaire.dto.TvShowConfiguration.Quality;
import org.sallaire.dto.tvdb.ISearchResult;
import org.sallaire.service.ShowService;
import org.sallaire.service.TorrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SickbeardApplication.class)
@WebAppConfiguration
public class SickbeardApplicationTests {

	@Autowired
	private TvShowDao showDao;

	@Autowired
	private ShowService showService;

	@Autowired
	private TorrentService torrentService;

	@Test
	public void contextLoads() {
		List<? extends ISearchResult> results = showService.search("fargo", "fr");
		ISearchResult result = results.get(0);
		showService.add(result.getId(), "", Status.SKIPPED.name(), Quality.SD.name(), "en");
		Map<Long, List<Episode>> episodes = showDao.getAllShowEpisodes();
		Episode episode = episodes.entrySet().iterator().next().getValue().iterator().next();
		torrentService.searchAndGetEpisode(episode);
	}

}
