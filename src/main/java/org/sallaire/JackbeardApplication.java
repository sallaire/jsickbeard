package org.sallaire;

import javax.annotation.PostConstruct;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
@EnableJSONDoc

@ComponentScan(basePackages = { "org.sallaire.dao", "org.sallaire.service", "org.sallaire.controller" })
public class JackbeardApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(JackbeardApplication.class);

	// @Autowired
	// private AddShowProcessor showProcessor;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		LOGGER.info("Starting application...");
		SpringApplication.run(JackbeardApplication.class, args);
		LOGGER.info("Application started");
	}

	@PostConstruct
	public void postConstruct() {
		LOGGER.info("Starting show processor started");
		// showProcessor.startShowProcessor();
		LOGGER.info("Show processor started");
	}

	// @Bean
	// public DataSource dataSource() {
	// return DataSourceBuilder.create() //
	// .driverClassName("org.h2.Driver") //
	// .url("jdbc:h2:file:" + JackBeardConstants.APPLICATION_DIRECTORY.resolve("jackbeard.db").toString()) //
	// .build();
	// }
}
