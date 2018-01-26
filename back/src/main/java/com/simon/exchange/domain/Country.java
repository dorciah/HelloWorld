package com.simon.exchange.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Domain object for a country including currency, tax and deductions
 * 
 * @author Simon
 * 
 */

@Entity
@Table(name="country")
public class Country {
	
	@Id
	@Column(name="ID", nullable=false, unique=true, length=11)
	private long id;
	
	@Column(name="NAME",nullable=false,length=100)
	private String name;
	
	@Column(name="CURRENCY",nullable=false,length=10)
	private String currency;
	
	@Column(name="TAX",nullable=false,precision=2)
	private double tax;
	
	@Column(name="DEDUCTIONS",nullable=false,precision=2)
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
