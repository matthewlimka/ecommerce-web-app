package com.matthewlim.ecommercewebapp.services;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.matthewlim.ecommercewebapp.enums.OrderStatus;
import com.matthewlim.ecommercewebapp.enums.PaymentMethod;
import com.matthewlim.ecommercewebapp.exceptions.UserNotFoundException;
import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	Logger logger = LogManager.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Loading user with username: " + username + "...");
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> {
					logger.error("No user with username " + username + " found");
					return new UsernameNotFoundException("No user with username " + username + " found");
				});
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Arrays.asList(authority));
	}
	
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
	
	public User findByShippingAddress(Address shippingAddress) {
		User user = userRepo.findByShippingAddress(shippingAddress)
				.orElseThrow(() -> {
					logger.error("No user with shipping address " + shippingAddress + " found");
					return new UserNotFoundException("No user with shipping address " + shippingAddress + " found");
				});
		
		logger.info("Successfully found user with shipping address " + shippingAddress);
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
//		existingUser.setRegisteredPaymentMethods(updatedUser.getRegisteredPaymentMethods());
		existingUser.setOrders(updatedUser.getOrders());
		existingUser.setShippingAddress(updatedUser.getShippingAddress());
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
			
//			if (key.equals("registeredPaymentMethods") && value instanceof List) {
//				List<PaymentMethod> registeredPaymentMethods = (List<PaymentMethod>) value;
//				value = registeredPaymentMethods;
//			}
			
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
