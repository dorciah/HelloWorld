package com.simon.exchange.services;

import java.util.List;

import com.simon.exchange.domain.ClientRequest;
import com.simon.exchange.domain.ClientResponse;
import com.simon.exchange.domain.Country;
import com.simon.exchange.domain.RateResponse;
import com.simon.exchange.domain.Result;

/**
 * 
 * Service which takes a {@link ClientRequest} and returns a {@link ClientResponse} with a net salary
 * 
 * @author Simon
 *
 */

public class NetSalaryCalculator {

	public NetSalaryCalculator() {}
	
	public ClientResponse calculate(ClientRequest request) {
		return null;
	}
	
	public Country getCountry(String name) {
		return null;
	}
	
	public RateResponse getRate(String currency) {
		return null;
	}
	
	public List<Result> generateResults(ClientRequest request, RateResponse RR, Country country){
		return null;
	}
	
	
	
}
