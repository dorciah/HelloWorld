package com.simon.exchange.domain;

/**
 * 
 * Object to wrap an individual calculation within the {@link ClientResponse}
 * 
 * @author Simon
 *
 */

public class Result {

	public Result() {}
	
	public Result(int order, String description, double amount, double diff) {}
	
	public int getOrder() {
		return Integer.MIN_VALUE;
	}
	
	public String getDescription() {
		return null;
	}
	
	public double getAmount() {
		return Double.NaN;
	}
	
	public double getDiff() {
		return Double.NaN;
	}
	
	
	
}
