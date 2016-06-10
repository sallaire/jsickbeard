package org.sallaire;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
@EnableJSONDoc

@ComponentScan(basePackages = { "org.sallaire.dao", "org.sallaire.service", "org.sallaire.controller" })
public class JackbeardApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(JackbeardApplication.class);

	public static void main(String[] args) {
		LOGGER.info("Starting application...");
		SpringApplication.run(JackbeardApplication.class, args);
		LOGGER.info("Application started");
	}

}
