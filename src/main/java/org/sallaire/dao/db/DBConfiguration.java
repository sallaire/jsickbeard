package org.sallaire.dao.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;
import org.sallaire.JackBeardConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@org.springframework.context.annotation.Configuration
@EnableNeo4jRepositories(basePackages = "org.sallaire.dao.db")
public class DBConfiguration extends Neo4jConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBConfiguration.class);

	private static final Path DB_PATH = JackBeardConstants.APPLICATION_DIRECTORY.resolve("db");

	@Bean
	public Configuration getConfiguration() {
		if (Files.notExists(DB_PATH)) {
			try {
				Files.createDirectories(DB_PATH);
			} catch (IOException e) {
				LOGGER.error("Unable to create DB directory {}", DB_PATH, e);
			}
		}
		Configuration config = new Configuration();
		config.driverConfiguration() //
				.setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver")//
				.setURI(DB_PATH.toUri().toString());
		return config;
	}

	@Bean
	public SessionFactory getSessionFactory() {
		return new SessionFactory(getConfiguration(), "org.sallaire.dao.db.entity");
	}
}
