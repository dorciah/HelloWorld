package com.simon.exchange.domain;

/**
 * 
 * Object to wrap an individual calculation within the {@link ClientResponse}
 * 
 * @author Simon
 *
 */

public class Result {

	private int order;
	private String description;
	private double amount;
	private double diff;
	
	public Result() {}
	
	public Result(int order, String description, double amount, double diff) {
		this.order = order;
		this.description = description;
		this.amount =  amount;
		this.diff = diff;
	}
	
	public String toString() {
		return this.order + ": " + this.description + " --- " + this.amount + " (" + this.diff + ")";
	}

	/* getters and setters */
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getDiff() {
		return diff;
	}

	public void setDiff(double diff) {
		this.diff = diff;
	}
	
	
	
	
}
