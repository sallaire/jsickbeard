package org.sallaire;

import java.io.IOException;
import java.nio.file.Files;

import javax.annotation.PostConstruct;

import org.sallaire.service.processor.AddShowProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = { "org.sallaire.dao", "org.sallaire.service", "org.sallaire.controller" })
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
public class SickbeardApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SickbeardApplication.class);

	@Autowired
	private AddShowProcessor showProcessor;

	public static void main(String[] args) {
		LOGGER.info("Starting application...");
		if (Files.notExists(SickBeardConstants.APPLICATION_DIRECTORY)) {
			LOGGER.debug("Create sickbeard directory");
			try {
				Files.createDirectory(SickBeardConstants.APPLICATION_DIRECTORY);
			} catch (IOException e) {
				LOGGER.error("Unable to create SickBeard directory in [{}]", SickBeardConstants.APPLICATION_DIRECTORY, e);
			}
			LOGGER.debug("Sickbeard directory [{}] created", SickBeardConstants.APPLICATION_DIRECTORY);
		}
		SpringApplication.run(SickbeardApplication.class, args);

		LOGGER.info("Application started");
	}

	@PostConstruct
	public void postConstruct() {
		LOGGER.info("Starting processors thread...");
		LOGGER.info("Starting show processor started");
		showProcessor.startShowProcessor();
		LOGGER.info("Show processor started");
		LOGGER.info("Processors started");
	}
}
