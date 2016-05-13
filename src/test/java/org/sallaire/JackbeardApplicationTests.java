package org.sallaire;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sallaire.dao.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JackbeardApplication.class)
@WebAppConfiguration
public class JackbeardApplicationTests {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	// private TvShowService showService;

	@Test
	public void test() {
		// User user = userRepo.findUserByName("admin");
		// user = userRepo.findOne(user.getId());
		// TvShowConfigurationParam params = new TvShowConfigurationParam();
		// params.setAudio("en");
		// params.setQuality(Quality.P720);
		// params.setStatus(Status.SKIPPED);
		// showService.upsertShow(1399L, params, user);
		// repo.getTvShowFromSourceId(1399L);
	}

}
