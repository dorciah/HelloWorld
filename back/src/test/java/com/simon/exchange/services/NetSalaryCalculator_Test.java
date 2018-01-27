package com.simon.exchange.services;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class NetSalaryCalculator_Test {
	
	@Autowired
	private NetSalaryCalculator calc;
	
	@MockBean
	private CountryRepository CR;
	
	@MockBean
	private RateService RS;

	@Test
	public void testGetCountry_shouldReturnValidResult() {
		
		Country toCheck = new Country(1L,"Denmark","DKK",0d,0d);
		given(CR.getCountryByName("Denmark")).willReturn(toCheck);
		
		Assert.assertEquals("Denmark", calc.getCountry("Denmark").getName());
		
	}
	
	@Test
	public void testGetCountry_shouldReturnNull() {
		given(calc.getRepository().getCountryByName("Denmark")).willReturn(null);
		
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

		given(RS.getRateResponse("DKK")).willReturn(DRR);
		
		RateResponse toVerify = calc.getRate("DKK");
		Assert.assertEquals(toVerify.getCurrency(), "DKK");
		Assert.assertEquals(toVerify.getRates().size(), 1);
		
	}
	
	@Test
	public void testGetRate_shouldReturnNull() {
		
		given(RS.getRateResponse("DKK")).willReturn(null);
		RateResponse toVerify = calc.getRate("DKK");
		Assert.assertNull(toVerify);
	}

	
	@Test
	public void testCalculate_validInputsReturnResponse() {
		
		ClientRequest request = new ClientRequest();
		request.setCountry("Denmark");
		request.setAmount(500.00);
		
		// should return 11_000 * 0.8 -> 8_800, 8_800 - 1000 -> 7800, 7800*0.5 -> 3900
		Country denmark = new Country(1L,"Denmark","DKK",0.2d,1000d);
		Rate danishRate = new Rate();
		danishRate.setAsk(0.5);
		danishRate.setEffectiveDate("2018-01-01");
		List<Rate> rates = Arrays.asList(danishRate);
		RateResponse DRR = new RateResponse();
		DRR.setCurrency("DKK");
		DRR.setRates(rates);
		
		given(CR.getCountryByName("Denmark")).willReturn(denmark);
		given(RS.getRateResponse("DKK")).willReturn(DRR);
		
		ClientResponse response = calc.calculate(request);
		
		Assert.assertEquals(response.getRate(), 0.5d,0);
		Assert.assertEquals(response.getEffectiveDate(), "2018-01-01");
		Assert.assertEquals(response.getRequest().getAmount(), request.getAmount(),0);
		Assert.assertEquals(response.getRequest().getCountry(), request.getCountry());
		
		Result toCheck = null;
		
		toCheck = response.getResults().get(0);
		Assert.assertEquals(toCheck.getOrder(), 0);
		Assert.assertEquals(toCheck.getDescription(), "gross");
		Assert.assertEquals(toCheck.getAmount(), 11_000,0);
		Assert.assertEquals(toCheck.getDiff(), 0,0);
		
		toCheck = response.getResults().get(1);
		Assert.assertEquals(toCheck.getOrder(), 1);
		Assert.assertEquals(toCheck.getDescription(), "post-tax");
		Assert.assertEquals(toCheck.getAmount(), 8_800,0);
		Assert.assertEquals(toCheck.getDiff(), 2_200,0);
		
		toCheck = response.getResults().get(2);
		Assert.assertEquals(toCheck.getOrder(), 2);
		Assert.assertEquals(toCheck.getDescription(), "post-deduction");
		Assert.assertEquals(toCheck.getAmount(), 7_800,0);
		Assert.assertEquals(toCheck.getDiff(), 1_000,0);
		
		toCheck = response.getResults().get(3);
		Assert.assertEquals(toCheck.getOrder(), 3);
		Assert.assertEquals(toCheck.getDescription(), "post-exchange");
		Assert.assertEquals(toCheck.getAmount(), 3_900,0);
		Assert.assertEquals(toCheck.getDiff(), 0,0);
		
		Assert.assertEquals(response.getResults().size(), 4);
		
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void testCalculate_missingCountryReturnsIllegalArgument() {
		
		ClientRequest request = new ClientRequest();
		request.setCountry("Denmark");
		request.setAmount(10_000.00);
		
		given(CR.getCountryByName("Denmark")).willReturn(null);
		
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Invalid Country name in request");
		
		calc.calculate(request);
		
	}
	
	@Test
	public void testCalculate_negativeAmountReturnsIllegalArgument() {
		
		ClientRequest request = new ClientRequest();
		request.setCountry("Denmark");
		request.setAmount(-1000.00);
		Country denmark = new Country(1L,"Denmark","DKK",0.2d,1000d);
		
		given(CR.getCountryByName("Denmark")).willReturn(denmark);
		
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("Daily salary is negative");
		
		calc.calculate(request);
		
	}
	
	@Test
	public void testCalculate_missingRateReturnsNullPointer() {
		
		ClientRequest request = new ClientRequest();
		request.setCountry("Denmark");
		request.setAmount(10_000.00);
		
		Country denmark = new Country(1L,"Denmark","DKK",0.2d,1000d);
		
		given(CR.getCountryByName("Denmark")).willReturn(denmark);
		given(RS.getRateResponse("DKK")).willReturn(null);
		
		expectedEx.expect(NullPointerException.class);
		expectedEx.expectMessage("Exchange rate api did not return a response");
		
		calc.calculate(request);
	}

	@Test
	public void testGenerateResults_shouldCalculateResultListWithExchange() {
		
		ClientRequest request = new ClientRequest();
		request.setCountry("Denmark");
		request.setAmount(500.00);
		
		// should return 11_000 * 0.8 -> 8_800, 8_800 - 1000 -> 7800, 7800*0.5 -> 3900
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
		Assert.assertEquals(toCheck.getOrder(), 0);
		Assert.assertEquals(toCheck.getDescription(), "gross");
		Assert.assertEquals(toCheck.getAmount(), 11_000,0);
		Assert.assertEquals(toCheck.getDiff(), 0,0);
		
		toCheck = toVerify.get(1);
		Assert.assertEquals(toCheck.getOrder(), 1);
		Assert.assertEquals(toCheck.getDescription(), "post-tax");
		Assert.assertEquals(toCheck.getAmount(), 8_800,0);
		Assert.assertEquals(toCheck.getDiff(), 2_200,0);
		
		toCheck = toVerify.get(2);
		Assert.assertEquals(toCheck.getOrder(), 2);
		Assert.assertEquals(toCheck.getDescription(), "post-deduction");
		Assert.assertEquals(toCheck.getAmount(), 7_800,0);
		Assert.assertEquals(toCheck.getDiff(), 1_000,0);
		
		toCheck = toVerify.get(3);
		Assert.assertEquals(toCheck.getOrder(), 3);
		Assert.assertEquals(toCheck.getDescription(), "post-exchange");
		Assert.assertEquals(toCheck.getAmount(), 3_900,0);
		Assert.assertEquals(toCheck.getDiff(), 0d,0);
		
		Assert.assertEquals(toVerify.size(), 4);
	}
	
	@Test
	public void testGenerateResults_shouldCalculateResultListWithoutExchange() {

		ClientRequest request = new ClientRequest();
		request.setCountry("Poland");
		request.setAmount(500.00);
		
		// should return 11_000 * 0.8 -> 8_800, 8_800 - 1000 -> 7800
		Country poland = new Country(1L,"Poland","PLN",0.2d,1000d);
		
		List<Result> toVerify = calc.generateResults(request, null, poland);
		Result toCheck = null;
		
		toCheck = toVerify.get(0);
		Assert.assertEquals(toCheck.getOrder(), 0);
		Assert.assertEquals(toCheck.getDescription(), "gross");
		Assert.assertEquals(toCheck.getAmount(), 11_000,0);
		Assert.assertEquals(toCheck.getDiff(), 0d,0);
		
		toCheck = toVerify.get(1);
		Assert.assertEquals(toCheck.getOrder(), 1);
		Assert.assertEquals(toCheck.getDescription(), "post-tax");
		Assert.assertEquals(toCheck.getAmount(), 8_800,0);
		Assert.assertEquals(toCheck.getDiff(), 2_200,0);
		
		toCheck = toVerify.get(2);
		Assert.assertEquals(toCheck.getOrder(), 2);
		Assert.assertEquals(toCheck.getDescription(), "post-deduction");
		Assert.assertEquals(toCheck.getAmount(), 7_800,0);
		Assert.assertEquals(toCheck.getDiff(), 1_000,0);
		
		Assert.assertEquals(toVerify.size(), 3);
	}

}
