package com.simon.exchange;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
			
		};
	}
}
