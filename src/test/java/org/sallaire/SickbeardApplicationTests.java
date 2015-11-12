package org.sallaire;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.service.ShowService;
import org.sallaire.service.TorrentService;
import org.sallaire.service.processor.WantedShowProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SickbeardApplication.class)
@WebAppConfiguration
public class SickbeardApplicationTests {

	@Autowired
	private WantedShowProcessor proc;

	@Autowired
	private TvShowDao showDao;

	@Autowired
	private ShowService showService;

	@Autowired
	private TorrentService torrentService;

	@Test
	public void contextLoads() {
		// TvShowConfiguration conf = showDao.getShowConfiguration(281623L);
		// conf.setLocation("/home/mediacenter/content/series/");
		// showDao.saveShowConfiguration(281623L, conf);
		//
		// for (Episode ep : showDao.getShowEpisodes(281623L)) {
		// ep.setEpisode(22);
		// ep.setSeason(1);
		// ep.setShowId(281623L);
		// showDao.saveWantedEpisode(ep);
		// break;
		// }
		// proc.updateShow();
	}

}
