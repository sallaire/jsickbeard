package org.sallaire;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sallaire.service.provider.torrent9.Torrent9Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JackbeardApplication.class)
@WebAppConfiguration
public class JackbeardApplicationTests {

	@Autowired
	private Torrent9Provider provider;

	@Test
	public void test() throws IOException {
		// provider.findEpisode(Arrays.asList("Turn"), "en", 3, 10, Quality.SD, new ArrayList<>());
	}

}
