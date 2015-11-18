package org.sallaire;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sallaire.dao.DaoException;
import org.sallaire.dao.metadata.tmdb.TMDBDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SickbeardApplication.class)
@WebAppConfiguration
public class SickbeardApplicationTests {

	@Autowired
	private TMDBDao tmdbDao;

	@Test
	public void contextLoads() {
		try {
			tmdbDao.getShowEpisodes(1396L, "fr");
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
