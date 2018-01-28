package com.simon.exchange.domain;

import java.util.List;

/**
 * 
 * Object to wrap the response sent to the front end. Has embedded {@link Result} list
 * 
 * @author Simon
 *
 */

public class ClientResponse {

	private ClientRequest request;
	private double rate;
	private String effectiveDate;
	private String currency;
	private List<Result> results;
	private String homeCurrency;
	
	public ClientResponse() {}

	/* getters and setters */
	public ClientRequest getRequest() {
		return request;
	}

	public void setRequest(ClientRequest request) {
		this.request = request;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getHomeCurrency() {
		return homeCurrency;
	}

	public void setHomeCurrency(String homeCurrency) {
		this.homeCurrency = homeCurrency;
	}
	
	
}
