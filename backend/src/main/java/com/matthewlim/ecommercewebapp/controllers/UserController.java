package com.matthewlim.ecommercewebapp.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matthewlim.ecommercewebapp.exceptions.UserNotFoundException;
import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.services.UserService;

@RestController
@RequestMapping("api/v1")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public List<User> getUsers(@RequestParam(required = false) String username, @RequestParam(required = false) String email, @RequestParam(required = false) Address shippingAddress, @RequestParam(required = false) Order order) {
		if ( username != null ) {
			return new ArrayList<User>(Arrays.asList(userService.findByUsername(username)));
		} else if ( email != null ) {
			return new ArrayList<User>(Arrays.asList(userService.findByEmail(email)));
		} else if ( shippingAddress != null ) {
			return new ArrayList<User>(Arrays.asList(userService.findByShippingAddress(shippingAddress)));
		} else if ( order != null ) {
			return new ArrayList<User>(Arrays.asList(userService.findByOrders(order)));
		} else {
			return userService.findAllUsers();	
		}
	}

	@GetMapping("/user")
	public User getUser() throws UserNotFoundException, RuntimeException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication.toString());
		System.out.println("Credentials: " + authentication.getCredentials());
		System.out.println("Principal: " + authentication.getPrincipal());
		String username = authentication.getName();	
		
		return userService.findByUsername(username);
	}
	
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User savedUser = userService.addUser(user);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{userId}")
				.buildAndExpand(user.getUserId())
				.toUri();
		
		return ResponseEntity.created(location).body(savedUser);
	}
	
	@PutMapping("/user/{userId}")
	public User updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
		return userService.updateUser(userId, updatedUser);
	}
	
	@PatchMapping("/user/{userId}")
	public User partialUpdateUser(@PathVariable Long userId, @RequestBody Map<String, Object> fields) {
		return userService.partialUpdateUser(userId, fields);
	}
	
	@DeleteMapping("/user/{userId}")
	public void deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
	}
}
