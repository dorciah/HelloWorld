package com.simon.exchange.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.simon.exchange.domain.Country;
import com.simon.exchange.services.CountryRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(RequestController.class)
public class RequestController_Test {
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private CountryRepository repository;

	@Test
	public void testGetCountries_shouldReturnEmptyList() throws Exception {
		
		mvc.perform(get("/api/exchange/countries")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$").isEmpty())
				;		
	}
	
	@Test
	public void testGetCountries_shouldReturnTwoResults() throws Exception {
		Country first = new Country(1L,"Poland","PLN",0d,0d);
		Country second = new Country(2L,"UK","GBP",0d,0d);
		
		List<Country> toTest = Arrays.asList(first,second);
		given(repository.findAll()).willReturn(toTest);
		
		mvc.perform(get("/api/messages")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$[0].id",is(1)))
				.andExpect(jsonPath("$[0].name",is("Poland")))
				.andExpect(jsonPath("$[1].id",is(2)))
				.andExpect(jsonPath("$[1].name",is("UK")))
				.andExpect(jsonPath("$.length()",is(2)))
				;		
		
	}
	

	@Test
	public void testGetClientResponse() {
		fail("Not yet implemented");
	}

}
