package org.sallaire;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sallaire.dao.db.TvShowConfigurationRepository;
import org.sallaire.dao.db.TvShowRepository;
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
	private TvShowRepository repo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TvShowConfigurationRepository confRepo;

	@Test
	public void test() {
		// confRepo.deleteAll();
		// TvShow show = repo.getTvShowFromSourceId(1399L);
		// User user = userRepo.getUserFromName("admin");
		// TvShowConfiguration test = new TvShowConfiguration();
		// test.setUser(user);
		// test.setTvShow(show);
		// test.setQuality(Quality.P720);
		// test.setLang("en");
		// // confRepo.save(test);
		// show.addFollower(test);
		// repo.save(show);
		repo.getTvShowFromSourceId(1399L);
	}

}
