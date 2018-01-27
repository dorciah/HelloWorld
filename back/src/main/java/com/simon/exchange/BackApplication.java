package com.simon.exchange;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.simon.exchange.domain.Country;
import com.simon.exchange.services.CountryRepository;

/**
 * 
 * Main class for the webapp.
 * 
 * @author Simon
 *
 */

@SpringBootApplication
public class BackApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(CountryRepository repository) {
		return (args) -> {
			repository.save(new Country(1L,"UK","GBP",0.25d,600.00d));
			repository.save(new Country(2L,"Germany","EUR",0.20d,800.00d));
			repository.save(new Country(3L,"Poland","PLN",0.19d,1200.00d));
		};
	}
}
