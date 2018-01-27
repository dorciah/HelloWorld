package com.simon.exchange.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.simon.exchange.domain.RateResponse;

/**
 * 
 * Service that gets a {@link RateResponse} from an external api
 * 
 * @author Simon
 *
 */

@Service
public class RateService {

	private final Logger logger = Logger.getLogger(RateService.class);
	private final String url = "http://api.nbp.pl/api/exchangerates/rates/C/";
	private final String params = "?format=json";
	private RestTemplate restTemplate;
	
	public RateService() {
		this.restTemplate = new RestTemplate();
	}
	
	public RateResponse getRateResponse(String currency) throws NullPointerException {
		final String targetAddress = getUrl() + currency + getParams();
		try{
			return this.restTemplate.getForObject(targetAddress, RateResponse.class);
		} catch (RestClientException ex) {
			logger.error("Could not get a rate for " + currency + " from " + targetAddress +" - " + ex.getMessage() ,ex);
			throw new NullPointerException("Exchange rate api did not return a response");
		}
	}
	
	public RestTemplate getRestTemplate() {
		return this.restTemplate;
	}
	
	public void setRestTemplate(RestTemplate temp) {
		this.restTemplate = temp;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getParams() {
		return this.params;
	}
	
}
