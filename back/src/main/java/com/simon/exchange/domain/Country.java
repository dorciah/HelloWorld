package com.simon.exchange.domain;

/**
 * Domain object for a country including currency, tax and deductions
 * 
 * @author Simon
 * 
 */

public class Country {
	
	private long id;
	private String name;
	private String currency;
	private double tax;
	private double deductions;
	
	public Country() {}
	
	public Country(long id, String name, String currency, double tax, double deduction) {
		this.id = id;
		this.name = name;
		this.currency = currency;
		this.tax = tax;
		this.deductions = deduction;
	}

	/* getters and setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getDeductions() {
		return deductions;
	}

	public void setDeductions(double deductions) {
		this.deductions = deductions;
	}

	
}
