package com.matthewlim.ecommercewebapp.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.matthewlim.ecommercewebapp.exceptions.UserNotFoundException;
import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.Order;
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
					logger.error("No user with email " + email + " found");
					return new UserNotFoundException("No user with email " + email + " found");
				});
		
		logger.info("Successfully found user with email " + email);
		return user;
	}
	
	public User findByAddress(Address address) {
		User user = userRepo.findByAddress(address)
				.orElseThrow(() -> {
					logger.error("No user with address " + address + " found");
					return new UserNotFoundException("No user with address " + address + " found");
				});
		
		logger.info("Successfully found user with address " + address);
		return user;
	}
	
	public User findByOrders(Order order) {
		User user = userRepo.findByOrders(order)
				.orElseThrow(() -> {
					logger.error("No user with order " + order + " found");
					return new UserNotFoundException("No user with order " + order + " found");
				});
		
		logger.info("Successfully found user with order " + order);
		return user;
	}
	
	public List<User> findAllUsers() {
		List<User> userList = userRepo.findAll();
		
		logger.info("Successfully found " + userList.size() + " users");
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
		existingUser.setOrders(updatedUser.getOrders());
		existingUser.setAddress(updatedUser.getAddress());
		existingUser.setCart(updatedUser.getCart());
		
		logger.info("Successfully updated user with user ID " + userId);
		return userRepo.save(existingUser);
	}
	
	public User partialUpdateUser(Long userId, Map<String, Object> fields) {
		User existingUser = userRepo.findById(userId)
				.orElseThrow(() -> {
					logger.error("No user with user ID " + userId + " found");
					return new UserNotFoundException("No user with user ID " + userId + " found");
				});
		
		fields.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(User.class, key);
			field.setAccessible(true);
			ReflectionUtils.setField(field, existingUser, value);
		});
		
		logger.info("Successfully updated user with user ID " + userId);
		return userRepo.save(existingUser);
	}
	
	public void deleteUser(Long userId) {
		User existingUser = userRepo.findById(userId)
				.orElseThrow(() -> {
					logger.error("No user with user ID " + userId + " found");
					return new UserNotFoundException("No user with user ID " + userId + " found");
				});
		
		logger.info("Successfully deleted user with user ID " + userId);
		userRepo.delete(existingUser);
	}
}
