package com.matthewlim.ecommercewebapp.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.matthewlim.ecommercewebapp.enums.Role;
import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;
import com.matthewlim.ecommercewebapp.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private ApplicationContext applicationContext;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private UserService userService;
	
	@Test
	public void testFindAllUsers() {
		List<User> userList = new ArrayList<User>(Arrays.asList(new User(), new User()));	
		
		when(userRepository.findAll()).thenReturn(userList);
		List<User> result = userService.findAllUsers();
		
		assertNotNull(result);
		assertThat(result).hasSize(userList.size()).containsExactlyElementsOf(userList);
	}
	
	@Test
	public void testFindByUserId() {
		Long userId = 1L;
		User user = new User();
		user.setUserId(userId);
		
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		User result = userService.findByUserId(userId);
		
		assertNotNull(result);
		assertEquals(userId, result.getUserId());
	}
	
	@Test
	public void testFindByUsername() {
		String username = "username";
		User user = new User();
		user.setUsername(username);
		
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		User result = userService.findByUsername(username);
		
		assertNotNull(result);
		assertEquals(username, result.getUsername());
	}
	
	@Test
	public void testFindByEmail() {
		String email = "email";
		User user = new User();
		user.setEmail(email);
		
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		User result = userService.findByEmail(email);
		
		assertNotNull(result);
		assertEquals(email, result.getEmail());
	}
	
	@Test
	public void testFindByShippingAddress() {
		Address shippingAddress = new Address();
		User user = new User();
		user.setShippingAddress(shippingAddress);
		
		when(userRepository.findByShippingAddress(shippingAddress)).thenReturn(Optional.of(user));
		User result = userService.findByShippingAddress(shippingAddress);
		
		assertNotNull(result);
		assertEquals(shippingAddress, result.getShippingAddress());
	}
	
	@Test
	public void testFindByOrders() {
		Order order = new Order();
		List<Order> orderList = new ArrayList<Order>(Arrays.asList(order));
		User user = new User();
		user.setOrders(orderList);
		
		when(userRepository.findByOrders(order)).thenReturn(Optional.of(user));
		User result = userService.findByOrders(order);
		
		assertNotNull(result);
		assertEquals(orderList, result.getOrders());
	}
	
	@Test
	public void testAddUser() {
		String originalPassword = "password";
		String encodedPassword = "$2a$10$mockEncodedPassword";
		User user = new User("user", originalPassword, "user@email.com", "user", "test", Role.USER);
		
		when(applicationContext.getBean(PasswordEncoder.class)).thenReturn(passwordEncoder);
		when(passwordEncoder.encode(originalPassword)).thenReturn(encodedPassword);
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		User result = userService.addUser(user);
		
		assertNotNull(result);
		verify(userRepository, times(1)).save(any(User.class));
		assertEquals(encodedPassword, result.getPassword());
	}
	
	@Test
	public void testUpdateUser() {
		Long userId = 1L;
		User existingUser = new User();
		existingUser.setUserId(userId);
		
		User updatedUser = new User();
		updatedUser.setUsername("updatedUsername");
		
		when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
		when(userRepository.save(existingUser)).thenReturn(updatedUser);
		User result = userService.updateUser(userId, updatedUser);
		
		assertNotNull(result);
		assertEquals(updatedUser.getUsername(), result.getUsername());
	}
	
	@Test
	public void testPartialUpdateUser() {
		Long userId = 1L;
		User existingUser = new User();
		existingUser.setUserId(userId);
		
		Map<String, Object> fieldsToUpdate = new HashMap<String, Object>();
		fieldsToUpdate.put("username", "updatedUsername");
		fieldsToUpdate.put("email", "updatedEmail@gmail.com");
		
		when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
		when(userRepository.save(existingUser)).thenReturn(existingUser);
		User result = userService.partialUpdateUser(userId, fieldsToUpdate);
		
		assertNotNull(result);
		assertEquals(fieldsToUpdate.get("username"), result.getUsername());
		assertEquals(fieldsToUpdate.get("email"), result.getEmail());
	}
	
	@Test
	public void testDeleteUser() {
		Long userId = 1L;
		User user = new User();
		user.setUserId(userId);
		
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		userService.deleteUser(userId);
		
		verify(userRepository, times(1)).delete(user);
	}
}