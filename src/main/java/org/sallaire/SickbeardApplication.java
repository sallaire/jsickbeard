package org.sallaire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.sallaire.dao.db")
@ComponentScan(basePackages = { "org.sallaire.dao", "org.sallaire.service", "org.sallaire.controller" })
public class SickbeardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SickbeardApplication.class, args);
	}
}
