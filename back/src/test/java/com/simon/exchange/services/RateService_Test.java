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
	private RateResponse mockedValue;
	
	@Before
	public void setup() {
		service = new RateService();
		service.setRestTemplate(mock(RestTemplate.class));
		mockedValue = new RateResponse();
	}
	
	@Test
	public void testGetRateResponse_shouldReturnResult() {
		
		List<Rate> rates = new ArrayList<>();
		rates.add(new Rate());
		mockedValue.setCurrency("EUR");
		mockedValue.setRates(rates);
		
		when(service.getRestTemplate().getForObject(service.getUrl() + "EUR" + service.getParams(),RateResponse.class))
			.thenReturn(mockedValue);
		RateResponse response = service.getRateResponse("EUR");
		
		Assert.assertEquals(response.getCurrency(), "EUR");
		Assert.assertEquals(response.getRates().size(), 1);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	public void testGetRateResponse_cantAccessServerShouldThrowNullPointer() {
		
		given(service.getRestTemplate().getForObject(service.getUrl() + "EUR" + service.getParams(),RateResponse.class))
			.willThrow(new RestClientException("Exchange rate api did not return a response"));
		expectedEx.expect(NullPointerException.class);
	    expectedEx.expectMessage("Exchange rate api did not return a response");
		service.getRateResponse("EUR");
		
		
	}

}
