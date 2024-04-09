package com.matthewlim.ecommercewebapp.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.AddressRepository;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AddressRepositoryIntegrationTest {
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private UserRepository userRepository;
	
    @Test
    public void testFindByStreetAddress() {
    	String streetAddress = "streetAddress";
        Address address = new Address();
        address.setStreetAddress(streetAddress);
        addressRepository.save(address);

        List<Address> result = addressRepository.findByStreetAddress(streetAddress);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(address, result.get(0));
    }
    
    @Test
    public void testFindByCity() {
    	String city = "city";
        Address address = new Address();
        address.setCity(city);
        addressRepository.save(address);

        List<Address> result = addressRepository.findByCity(city);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(address, result.get(0));
    }
    
    @Test
    public void testFindByState() {
    	String state = "state";
        Address address = new Address();
        address.setState(state);
        addressRepository.save(address);

        List<Address> result = addressRepository.findByState(state);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(address, result.get(0));
    }

    @Test
    public void testFindByPostalCode() {
        String postalCode = "postalCode";
        Address address = new Address();
        address.setPostalCode(postalCode);
        addressRepository.save(address);

        List<Address> result = addressRepository.findByPostalCode(postalCode);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(address, result.get(0));
    }

    @Test
    public void testFindByCountry() {
        String country = "country";
        Address address = new Address();
        address.setCountry(country);        
        addressRepository.save(address);

        List<Address> result = addressRepository.findByCountry(country);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(address, result.get(0));
    }

    @Test
    public void testFindByUser() {
        User user = new User();
        Address shippingAddress = new Address();
        shippingAddress.setUser(user);
        user.setShippingAddress(shippingAddress);
        userRepository.save(user);
        addressRepository.save(shippingAddress);

        Optional<Address> result = addressRepository.findByUser(user);

        assertTrue(result.isPresent());
        assertEquals(shippingAddress, result.get());
    }
}