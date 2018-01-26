package com.simon.exchange.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Assert;

import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import com.simon.exchange.domain.ClientRequest;
import com.simon.exchange.domain.ClientResponse;
import com.simon.exchange.domain.Country;
import com.simon.exchange.domain.Rate;
import com.simon.exchange.domain.RateResponse;
import com.simon.exchange.domain.Result;

public class NetSalaryCalculator_Test {

	private NetSalaryCalculator calc;
	
	@Autowired
	CountryRepository CR;
	
	@Before
	public void setup() {
		calc = new NetSalaryCalculator();
	}
	
	@Test
	public void testCalculate_validInputsReturnResponse() {
		
		ClientRequest request = new ClientRequest();
		request.setCountry("Denmark");
		request.setAmount(10_000.00);
		
		// should return 10_000 * 0.8 -> 8_000, 8_000 - 1000 -> 7000, 7000*0.5 -> 3500
		Country denmark = new Country(1L,"Denmark","DKK",0.2d,1000d);
		Rate danishRate = new Rate();
		danishRate.setAsk(0.5);
		danishRate.setEffectiveDate("2018-01-01");
		List<Rate> rates = Arrays.asList(danishRate);
		RateResponse DRR = new RateResponse();
		DRR.setCurrency("DKK");
		DRR.setRates(rates);
		
		Result first = new Result(1,"post-tax",8_000d,2_000d);
		Result second = new Result(2,"post-deduction",7_000,1_000);
		Result third = new Result(3,"post-exchange",3_500,3_500);
		List<Result> intendedResults = Arrays.asList(first,second,third);
		
		given(calc.getCountry("Denmark")).willReturn(denmark);
		given(calc.getRate("DKK")).willReturn(DRR);
		given(calc.generateResults(request, DRR, denmark)).willReturn(intendedResults);
		
		ClientResponse response = calc.calculate(request);
		
		Assert.assertEquals(response.getRate(), 0.5d,0);
		Assert.assertEquals(response.getEffectiveDate(), "2018-01-01");
		Assert.assertEquals(response.getRequest().getAmount(), request.getAmount(),0);
		Assert.assertEquals(response.getRequest().getCountry(), request.getCountry());
		
		Result toCheck = null;
		
		toCheck = response.getResults().get(0);
		Assert.assertEquals(toCheck.getOrder(), 1);
		Assert.assertEquals(toCheck.getDescription(), "post-tax");
		Assert.assertEquals(toCheck.getAmount(), 8_000,0);
		Assert.assertEquals(toCheck.getDiff(), 2_000,0);
		
		toCheck = response.getResults().get(1);
		Assert.assertEquals(toCheck.getOrder(), 1);
		Assert.assertEquals(toCheck.getDescription(), "post-deduction");
		Assert.assertEquals(toCheck.getAmount(), 7_000,0);
		Assert.assertEquals(toCheck.getDiff(), 1_000,0);
		
		toCheck = response.getResults().get(2);
		Assert.assertEquals(toCheck.getOrder(), 2);
		Assert.assertEquals(toCheck.getDescription(), "post-exchange");
		Assert.assertEquals(toCheck.getAmount(), 3_500,0);
		Assert.assertEquals(toCheck.getDiff(), 3_500,0);
		
		Assert.assertEquals(response.getResults().size(), 2);
		
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void testCalculate_missingCountryReturnsIllegalArgument() {
		
		ClientRequest request = new ClientRequest();
		request.setCountry("Denmark");
		request.setAmount(10_000.00);
		
		given(calc.getCountry("Denmark")).willReturn(null);
		
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid Country name in request");
		
		calc.calculate(request);
		
	}
	
	@Test
	public void testCalculate_missingRateReturnsNullPointer() {
		
		ClientRequest request = new ClientRequest();
		request.setCountry("Denmark");
		request.setAmount(10_000.00);
		
		Country denmark = new Country(1L,"Denmark","DKK",0.2d,1000d);
		
		given(calc.getCountry("Denmark")).willReturn(denmark);
		given(calc.getRate("DKK")).willReturn(null);
		
		expectedEx.expect(NullPointerException.class);
		expectedEx.expectMessage("Exchange rate api did not return a response");
		
		calc.calculate(request);
	}
	
	@Test
	public void testCalculate_invalidInputReturnsIllegalArgument() {
		
		ClientRequest request = new ClientRequest();
		request.setCountry("Denmark");
		request.setAmount(Double.NaN);
		
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid request attribute");
		
		calc.calculate(request);
		
	}

	@Test
	public void testGetCountry_shouldReturnValidResult() {
		
		Country toCheck = new Country(1L,"Denmark","DKK",0d,0d);
		
		given(CR.getCountryByName("Denmark")).willReturn(toCheck);
		
		Assert.assertEquals(toCheck.getName(), calc.getCountry("Denmark").getName());
		
	}
	
	@Test
	public void testGetCountry_shouldReturnNull() {
		given(CR.getCountryByName("Denmark")).willReturn(null);
		
		Assert.assertNull(calc.getCountry("Denmark"));
	}

	@Test
	public void testGetRate_shouldReturnValidResult() {
		
		Rate danishRate = new Rate();
		danishRate.setAsk(0.5);
		danishRate.setEffectiveDate("2018-01-01");
		List<Rate> rates = Arrays.asList(danishRate);
		RateResponse DRR = new RateResponse();
		DRR.setCurrency("DKK");
		DRR.setRates(rates);
		
		RateService RS = new RateService();
		given(RS.getRateResponse("DKK")).willReturn(DRR);
		
		RateResponse toVerify = calc.getRate("DKK");
		Assert.assertEquals(toVerify.getCurrency(), "DKK");
		Assert.assertEquals(toVerify.getRates().size(), 1);
		
	}
	
	@Test
	public void testGetRate_shouldReturnNull() {
		
		RateService RS = new RateService();
		given(RS.getRateResponse("DKK")).willReturn(null);
		RateResponse toVerify = calc.getRate("DKK");
		Assert.assertNull(toVerify);
	}

	@Test
	public void testGenerateResults_shouldCalculateResultListWithExchange() {
		
		ClientRequest request = new ClientRequest();
		request.setCountry("Denmark");
		request.setAmount(10_000.00);
		
		// should return 10_000 * 0.8 -> 8_000, 8_000 - 1000 -> 7000, 7000*0.5 -> 3500
		Country denmark = new Country(1L,"Denmark","DKK",0.2d,1000d);
		Rate danishRate = new Rate();
		danishRate.setAsk(0.5);
		danishRate.setEffectiveDate("2018-01-01");
		List<Rate> rates = Arrays.asList(danishRate);
		RateResponse DRR = new RateResponse();
		DRR.setCurrency("DKK");
		DRR.setRates(rates);
		
		List<Result> toVerify = calc.generateResults(request, DRR, denmark);
		Result toCheck = null;
		
		toCheck = toVerify.get(0);
		Assert.assertEquals(toCheck.getOrder(), 1);
		Assert.assertEquals(toCheck.getDescription(), "post-tax");
		Assert.assertEquals(toCheck.getAmount(), 8_000,0);
		Assert.assertEquals(toCheck.getDiff(), 2_000,0);
		
		toCheck = toVerify.get(1);
		Assert.assertEquals(toCheck.getOrder(), 1);
		Assert.assertEquals(toCheck.getDescription(), "post-deduction");
		Assert.assertEquals(toCheck.getAmount(), 7_000,0);
		Assert.assertEquals(toCheck.getDiff(), 1_000,0);
		
		toCheck = toVerify.get(2);
		Assert.assertEquals(toCheck.getOrder(), 2);
		Assert.assertEquals(toCheck.getDescription(), "post-exchange");
		Assert.assertEquals(toCheck.getAmount(), 3_500,0);
		Assert.assertEquals(toCheck.getDiff(), 3_500,0);
		
		Assert.assertEquals(toVerify.size(), 3);
	}
	
	@Test
	public void testGenerateResults_shouldCalculateResultListWithoutExchange() {

		ClientRequest request = new ClientRequest();
		request.setCountry("Poland");
		request.setAmount(10_000.00);
		
		// should return 10_000 * 0.8 -> 8_000, 8_000 - 1000 -> 7000
		Country poland = new Country(1L,"Poland","PLN",0.2d,1000d);
		
		List<Result> toVerify = calc.generateResults(request, null, poland);
		Result toCheck = null;
		
		toCheck = toVerify.get(0);
		Assert.assertEquals(toCheck.getOrder(), 1);
		Assert.assertEquals(toCheck.getDescription(), "post-tax");
		Assert.assertEquals(toCheck.getAmount(), 8_000,0);
		Assert.assertEquals(toCheck.getDiff(), 2_000,0);
		
		toCheck = toVerify.get(1);
		Assert.assertEquals(toCheck.getOrder(), 1);
		Assert.assertEquals(toCheck.getDescription(), "post-deduction");
		Assert.assertEquals(toCheck.getAmount(), 7_000,0);
		Assert.assertEquals(toCheck.getDiff(), 1_000,0);
		
		Assert.assertEquals(toVerify.size(), 2);
	}

}
