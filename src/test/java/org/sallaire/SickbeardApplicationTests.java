package org.sallaire;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sallaire.dao.db.TvShowDao;
import org.sallaire.service.ShowService;
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

	@Test
	public void contextLoads() {
		proc.updateShow();
	}

}
