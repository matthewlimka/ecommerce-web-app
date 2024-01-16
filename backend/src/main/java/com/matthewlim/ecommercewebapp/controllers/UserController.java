package com.matthewlim.ecommercewebapp.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.services.UserService;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping
	public List<User> getUsers(@RequestParam(required = false) String username, @RequestParam(required = false) String email) {
		if ( username != null ) {
			return new ArrayList<User>(Arrays.asList(userService.findByUsername(username)));
		} else if ( email != null ) {
			return new ArrayList<User>(Arrays.asList(userService.findByEmail(email)));
		} else {
			return userService.findAllUsers();	
		}
	}

	@GetMapping("/{id}")
	public User getUser(@PathVariable Long userId) throws UserNotFoundException {
		return userService.findByUserId(userId);
	}
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User savedUser = userService.addUser(user);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(user.getUserId())
				.toUri();
		
		return ResponseEntity.created(location).body(savedUser);
	}
	
	@PutMapping("/{id}")
	public User updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
		return userService.updateUser(userId, updatedUser);
	}
	
	@PatchMapping("/{id}")
	public User partialUpdateUser(@PathVariable Long userId, @RequestBody Map<String, Object> fields) {
		return userService.partialUpdateUser(userId, fields);
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
	}
}
