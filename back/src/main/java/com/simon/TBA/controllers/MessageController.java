package com.simon.TBA.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simon.TBA.domain.Message;
import com.simon.TBA.services.MessageRepository;

/**
 * 
 * Dummy controller for Message objects. Allows getAll and get with id
 * 
 * @author Simon
 *
 */


@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:49152","http://localhost:2222"})
public class MessageController {
	
	private final Logger logger = Logger.getLogger(MessageController.class);

	@Autowired
	MessageRepository repository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Message> getMessages() {
		logger.info("Request for All Messages");
		return repository.findAll();
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public Message getMessage(@PathVariable("id") long id) {
		logger.info("Request for message with id " + id);
		return repository.findOne(id);
	}
	
}
