package com.simon.exchange.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simon.exchange.domain.ClientRequest;
import com.simon.exchange.domain.ClientResponse;
import com.simon.exchange.domain.Country;
import com.simon.exchange.services.CountryRepository;
import com.simon.exchange.services.NetSalaryCalculator;

@RestController
@RequestMapping("/api/exchange")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:49152","http://localhost:2222"})
public class RequestController {

	private final Logger logger = Logger.getLogger(RequestController.class);
	
	@Autowired
	private NetSalaryCalculator calc;
	
	@Autowired
	private CountryRepository repository;
	
	@RequestMapping(value="/countries",method=RequestMethod.GET)
	public List<Country> getCountries(){
		logger.info("Getting all countries");
		return repository.findAll();
	}
	
	@RequestMapping(value="/request", method = RequestMethod.POST)
	public ClientResponse getClientResponse(@RequestBody ClientRequest request) {
		logger.info("Input: " + request.getCountry() + " and " + request.getAmount());
		return calc.calculate(request);
	}
	
}
