package com.matthewlim.ecommercewebapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matthewlim.ecommercewebapp.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByStreet(String street);
	List<Address> findByCity(String city);
	List<Address> findByState(String state);
	List<Address> findByPostalCode(String postalCode);
	List<Address> findByCountry(String country);
}
