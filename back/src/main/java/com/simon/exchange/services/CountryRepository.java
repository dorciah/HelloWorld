package com.simon.exchange.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.simon.exchange.domain.Country;

/**
 * 
 * Repository interface with only one additional method
 * 
 * @author Simon
 *
 */

public interface CountryRepository extends JpaRepository<Country, Long> {

	@Query("select c from Country c where c.name = :name")
	Country getCountryByName(@Param("name") String name);
}
