package com.simon.exchange.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simon.exchange.domain.ClientRequest;
import com.simon.exchange.domain.ClientResponse;
import com.simon.exchange.domain.Country;

@RestController
@RequestMapping("/api/exchange")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:49152","http://localhost:2222"})
public class RequestController {

	private final Logger logger = Logger.getLogger(RequestController.class);
	
	@RequestMapping(value="/countries",method=RequestMethod.GET)
	public List<Country> getCountries(){
		return null;
	}
	
	@RequestMapping(value="/request", method = RequestMethod.POST)
	public ClientResponse getClientResponse(ClientRequest request) {
		return null;
	}
	
}
