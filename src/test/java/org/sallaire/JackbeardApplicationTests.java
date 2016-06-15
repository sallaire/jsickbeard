package org.sallaire;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sallaire.service.processor.UpdateShowProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JackbeardApplication.class)
@WebAppConfiguration
public class JackbeardApplicationTests {

	@Autowired
	private UpdateShowProcessor processor;

	@Test
	public void test() {
		processor.updateShow();
	}

}
