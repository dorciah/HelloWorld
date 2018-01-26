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

	public ClientResponse() {}
	
	public double getRate() {
		return Double.NaN;
	}
	
	public String getEffectiveDate() {
		return null;
	}
	
	public ClientRequest getRequest() {
		return null;
	}
	
	public List<Result> getResults(){
		return null;
	}
	
}
