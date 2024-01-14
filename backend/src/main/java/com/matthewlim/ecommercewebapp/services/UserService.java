package com.matthewlim.ecommercewebapp.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matthewlim.ecommercewebapp.exceptions.UserNotFoundException;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@Service
public class UserService {

	Logger logger = LogManager.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepo;
	
	public User findByUserId(Long userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> {
					logger.error("No user with user ID " + userId + " found");
					return new UserNotFoundException("No user with user ID " + userId + " found");
				});
		
		logger.info("Successfully found user with user ID " + userId);
		return user;
	}
	
	public User findByUsername(String username) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> {
					logger.error("No user with username " + username + " found");
					return new UserNotFoundException("No user with username " + username + " found");
				});
		
		logger.info("Successfully found user with username " + username);
		return user;
	}
	
	public User findByEmail(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> {
					logger.error("No user with email: " + email + " found");
					return new UserNotFoundException("No user with email " + email + " found");
				});
		
		logger.info("Successfully found user with email" + email);
		return user;
	}
	
	public List<User> findAllUsers() {
		List<User> userList = userRepo.findAll();
		
		return userList;
	}
	
	public User addUser(User user) {
		logger.info("Successfully registered new user with user ID " + user.getUserId());
		return userRepo.save(user);
	}
	
	public User updateUser(Long userId, User updatedUser) {
		User existingUser = userRepo.findById(userId)
				.orElseThrow(() -> {
					logger.error("No user with user ID " + userId + " found");
					return new UserNotFoundException("No user with user ID " + userId + " found");
				});

		existingUser.setUsername(updatedUser.getUsername());
		existingUser.setPassword(updatedUser.getPassword());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setFirstName(updatedUser.getFirstName());
		existingUser.setLastName(updatedUser.getLastName());
		existingUser.setAddress(updatedUser.getAddress());
		existingUser.setCart(updatedUser.getCart());
		
		logger.info("Successfully updated user with user ID " + userId);
		return userRepo.save(existingUser);
	}
	
	public void deleteUser(Long userId) {
		User existingUser = userRepo.findById(userId)
				.orElseThrow(() -> {
					logger.error("No ucser with user ID " + userId + " found");
					return new UserNotFoundException("No user with user ID " + userId + " found");
				});
		
		logger.info("Successfully deleted user with user ID " + userId);
		userRepo.delete(existingUser);
	}
}
