package com.matthewlim.ecommercewebapp.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRepositoryIntegrationTest {
	
	@Autowired
	private UserRepository userRepository;
	
    @Test
    public void testFindByUsername() {
    	String username = "username";
        User user = new User();
        user.setUsername(username);
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindByEmail() {
    	String email = "email"; 
        User user = new User();
        user.setEmail(email);
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindByShippingAddress() {
        Address shippingAddress = new Address();
        User user = new User();
        user.setShippingAddress(shippingAddress);
        shippingAddress.setUser(user);
        userRepository.save(user);

        Optional<User> result = userRepository.findByShippingAddress(shippingAddress);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindByOrders() {
        Order order = new Order();
        User user = new User();
        user.setOrders(new ArrayList<Order>(Arrays.asList(order)));
        order.setUser(user);
        userRepository.save(user);

        Optional<User> result = userRepository.findByOrders(order);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }
}