package com.simon.exchange.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simon.exchange.domain.ClientRequest;
import com.simon.exchange.domain.ClientResponse;
import com.simon.exchange.domain.Country;
import com.simon.exchange.domain.Rate;
import com.simon.exchange.domain.RateResponse;
import com.simon.exchange.domain.Result;

/**
 * 
 * Service which takes a {@link ClientRequest} and returns a {@link ClientResponse} with a net salary
 * 
 * @author Simon
 *
 */

@Service
public class NetSalaryCalculator {

	private final Logger logger = Logger.getLogger(NetSalaryCalculator.class);
	private final String HOME_COUNTRY = "Poland";
	private String HOME_CURRENCY = "PLN";
	private final int DAYS = 22;
	
	@Autowired
	CountryRepository repository;
	
	@Autowired
	private RateService rateService;
	
	public NetSalaryCalculator() {}
	
	public CountryRepository getRepository() {
		return this.repository;
	}
	
	public ClientResponse calculate(ClientRequest request) {
		
		final ClientResponse response = new ClientResponse();
		
		// first get the country
		Country country = getCountry(request.getCountry());
		if (country == null) throw new IllegalArgumentException("Invalid Country name in request");
		else if (request.getAmount() < 0) throw new IllegalArgumentException("Daily salary is negative!");
		else {
			
			// get the rate if not home country
			RateResponse RR = null;
			Rate R = null;
			if (!country.getName().equalsIgnoreCase(this.HOME_COUNTRY)) {
				RR = getRate(country.getCurrency());
				if (RR == null) {
					throw new NullPointerException("Exchange rate api did not return a response");
				}
				R = RR.getRates().get(0);
				response.setEffectiveDate(R.getEffectiveDate());
				response.setRate(R.getAsk());
			}
			
			// calculate the results
			List<Result> results = generateResults(request, RR, country);
			
			response.setCurrency(country.getCurrency());
			response.setHomeCurrency(HOME_CURRENCY);
			response.setRequest(request);
			response.setResults(results);
			
			return response;
		}
	}
	
	public Country getCountry(String name) {
		return repository.getCountryByName(name);
	}
	
	public RateResponse getRate(String currency) {
		try {
			return this.rateService.getRateResponse(currency);
		} catch (NullPointerException e) {
			logger.error("Unable to get rate for currency " + currency);
			return null;
		}
	}
	
	public List<Result> generateResults(ClientRequest request, RateResponse RR, Country country){
		
		final List<Result> results = new ArrayList<>();
		final double monthlyGross = this.DAYS * request.getAmount();
		
		Result gross = new Result(0,"gross",round(monthlyGross,2),0d);
		logger.info(gross);
		results.add(gross);
		
		// first do the tax
		Result postTax = new Result(1,"post-tax",
				round(monthlyGross*(1-country.getTax()),2), round(monthlyGross -monthlyGross*(1-country.getTax()),2));
		logger.info(postTax);
		results.add(postTax);
		
		// deduct costs
		Result postDeduction = new Result(2,"post-deduction",round(postTax.getAmount() - country.getDeductions(),2),country.getDeductions());
		logger.info(postDeduction);
		results.add(postDeduction);
		
		// if not base country then use exchange rate
		if (!this.HOME_COUNTRY.equalsIgnoreCase(country.getName())) {
			final Rate rate = RR.getRates().get(0);
			Result postExchange = 
					new Result(3,"post-exchange",round(postDeduction.getAmount()*rate.getAsk(),2)
							,0d);
			logger.info(postExchange);
			results.add(postExchange);
		}
		
		return results;
	}

	public static double round(double value, int places) {
	    
		if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	    
	}
	
	
}
