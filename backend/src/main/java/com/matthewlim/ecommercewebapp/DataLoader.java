package com.matthewlim.ecommercewebapp;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.matthewlim.ecommercewebapp.enums.Role;
import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		String encodedPassword = passwordEncoder.encode("password");
		User user = new User("user", encodedPassword, "test@email.com", "user", "test", Role.USER, new ArrayList<Order>(), new Address(), new Cart());
		userRepo.save(user);
	}
}