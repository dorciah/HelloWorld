package com.simon.exchange.domain;

import org.junit.Assert;
import org.junit.Test;

public class Country_Test {

	@Test
	public void testCountry() {
		Country country = new Country(1L,"Corfu","EUR",0d,0d);
		
		Assert.assertEquals(country.getId(), 1);
		Assert.assertEquals(country.getName(),"Corfu");
		Assert.assertEquals(country.getCurrency(), "EUR");
		Assert.assertEquals(country.getTax(), 0d,0);
		Assert.assertEquals(country.getDeductions(), 0d,0);
		
	}

}
