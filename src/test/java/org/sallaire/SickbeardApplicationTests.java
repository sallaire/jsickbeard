package org.sallaire;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sallaire.dto.TvShowConfiguration.Quality;
import org.sallaire.providers.t411.T411Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SickbeardApplication.class)
@WebAppConfiguration
public class SickbeardApplicationTests {

	@Autowired
	T411Provider provider;

	@Test
	public void contextLoads() {
		try {
			provider.findEpisode("Les revenants", "fr", 2, 2, Quality.SD);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
