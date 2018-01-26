package com.simon.exchange.domain;

/**
 * 
 * Rate object aligned with api from nbp. embedded within {@link RateResponse} wrapper
 * 
 * @author Simon
 * 
 */

public class Rate {
	
	private double ask;
	private String no;
	private double bid;
	private String effectiveDate;
	
	public Rate() {}

	/* getters and setters */
	public double getAsk() {
		return ask;
	}

	public void setAsk(double ask) {
		this.ask = ask;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		this.bid = bid;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	
}
