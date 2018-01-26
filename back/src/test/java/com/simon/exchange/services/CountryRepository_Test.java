package com.simon.exchange.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.simon.exchange.domain.Country;
import com.simon.exchange.services.CountryRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CountryRepository_Test {

	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired
	private CountryRepository repository;
	
	@Test
	public void getCountryByName_shouldReturnResult() {
		Country toPersist = new Country(1L,"Corfu","",0d,0d);
		entityManager.persist(toPersist);
		
		Country toVerify = repository.getCountryByName("Corfu");
		Assert.assertEquals(toPersist.getName(),toVerify.getName());
		
	}
	
	@Test
	public void getCountryByName_shouldReturnNull() {
		Country toPersist = new Country(1L,"Corfu","",0d,0d);
		entityManager.persist(toPersist);
		
		Country toVerify = repository.getCountryByName("USA");
		Assert.assertNull(toVerify);
	}
	
	@After
	public void after() {
		this.entityManager.clear();
	}

}
