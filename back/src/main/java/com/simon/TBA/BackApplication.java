package com.simon.TBA;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.simon.TBA.domain.Message;
import com.simon.TBA.services.MessageRepository;

/**
 * 
 * Main class for the webapp. Simply adds a dummy message to the in mem database
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
	public CommandLineRunner demo(MessageRepository repository) {
		return (args) -> {
			// save a dummy message
			repository.save(new Message(1L,"The Base Application (TBA)"));
		};
	}
}
