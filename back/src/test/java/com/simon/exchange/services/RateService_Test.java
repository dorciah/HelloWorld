package com.simon.exchange.services;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import com.simon.exchange.domain.Rate;
import com.simon.exchange.domain.RateResponse;

public class RateService_Test {

	private RateService service;
	private RestTemplate restTemplate;
	private RateResponse mockedValue;
	
	@Before
	public void setup() {
		service = new RateService();
		restTemplate = new RestTemplate();
		mockedValue = new RateResponse();
	}
	
	@Test
	public void testGetRateResponse_shouldReturnResult() {
		
		List<Rate> rates = new ArrayList<>();
		rates.add(new Rate());
		mockedValue.setCurrency("Euro");
		mockedValue.setRates(rates);
		
		given(restTemplate.getForObject(service.getUrl(),RateResponse.class)).willReturn(mockedValue);
		RateResponse response = service.getRateResponse("EUR");
		
		Assert.assertEquals(response.getCurrency(), "Euro");
		Assert.assertEquals(response.getRates().size(), 1);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	public void testGetRateResponse_cantAccessServerShouldThrowNullPointer() {
		
		given(restTemplate.getForObject(service.getUrl(),RateResponse.class)).willThrow(new RestClientException("Exchange rate api did not return a response"));
		expectedEx.expect(NullPointerException.class);
	    expectedEx.expectMessage("Exchange rate api did not return a response");
		service.getRateResponse("EUR");
		
		
	}

}
