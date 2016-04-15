package org.sallaire;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sallaire.dao.db.DownloadDao;
import org.sallaire.dto.configuration.ClientConfiguration;
import org.sallaire.service.DownloadService;
import org.sallaire.service.FileHelper.Finder;
import org.sallaire.service.ShowService;
import org.sallaire.service.client.deluge.DelugeClient;
import org.sallaire.service.provider.t411.T411Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JackbeardApplication.class)
@WebAppConfiguration
public class JackbeardApplicationTests {

	@Autowired
	private DownloadService downloadService;

	@Autowired
	private DownloadDao downloadDao;

	@Autowired
	private ShowService showService;

	@Test
	public void test() {
		Finder finder = new Finder("Homeland.S05E09.FASTSUB.VOSTFR.HDTV.XviD-FDS");
		try {
			Files.walkFileTree(Paths.get("D:", "Tests", "sick"), finder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(finder.isFound());
	}

	// @Test
	public void initDownloadConfig() {
		// EpisodeKey key = new EpisodeKey(123L, 1, 2, Quality.SD, "fr");
		// EpisodeStatus ep = new EpisodeStatus(key, Status.SNATCHED);
		// downloadDao.saveEpisodeStatus(ep);
		// System.out.println(downloadDao.getSnatchedEpisodes());
		Map<String, String> t411Params = new HashMap<>();
		t411Params.put(T411Provider.USER, "maitrefolace");
		t411Params.put(T411Provider.PASSWORD, "cartman_T411");
		downloadService.saveProvider(T411Provider.ID, t411Params);

		LinkedHashMap<String, Boolean> providerOrder = new LinkedHashMap<>();
		providerOrder.put(T411Provider.ID, true);
		downloadService.saveProviders(providerOrder);

		ClientConfiguration conf = new ClientConfiguration();
		conf.setClient(DelugeClient.ID);
		conf.setMoveShow(true);
		conf.setPassword("H4d0pi");
		conf.setUrl("http://37.187.19.83:8112/");
		conf.setSeasonPattern("Saison_%s");
		downloadService.saveClient(conf);
	}

}
