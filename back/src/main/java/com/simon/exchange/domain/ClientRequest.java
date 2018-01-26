package com.simon.exchange.domain;

/**
 * 
 * Simple object to wrap a client request in the front end
 * 
 * @author Simon
 * 
 */

public class ClientRequest {
	
	private String country;
	private double amount;
	
	public ClientRequest() {}

	/* getters and setters */
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
