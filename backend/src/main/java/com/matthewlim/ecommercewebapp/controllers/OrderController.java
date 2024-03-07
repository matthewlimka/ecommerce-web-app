package com.matthewlim.ecommercewebapp.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
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

import com.matthewlim.ecommercewebapp.enums.OrderStatus;
import com.matthewlim.ecommercewebapp.exceptions.OrderNotFoundException;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.services.OrderService;
import com.matthewlim.ecommercewebapp.services.UserService;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;

	@GetMapping
	public List<Order> getOrders(@RequestParam(required = false) LocalDateTime orderDate, @RequestParam(required = false) BigDecimal totalAmount, @RequestParam(required = false) OrderStatus orderStatus, @RequestParam(required = false) User user, @RequestParam(required = false) OrderItem orderItem, @RequestParam(required = false) Payment payment) {
		if ( orderDate != null ) {
			return new ArrayList<Order>(Arrays.asList(orderService.findByOrderDate(orderDate)));
		} else if ( totalAmount != null ) {
			return orderService.findByTotalAmount(totalAmount);
		} else if ( orderStatus != null ) {
			return orderService.findByOrderStatus(orderStatus);
		} else if ( user != null ) {
			return orderService.findByUser(user);
		} else if ( orderItem != null ) {
			return new ArrayList<Order>(Arrays.asList(orderService.findByOrderItems(orderItem)));
		} else if ( payment != null ) {
			return new ArrayList<Order>(Arrays.asList(orderService.findByPayment(payment)));
		} else {
			return orderService.findAllOrders();	
		}
	}

	@GetMapping("/{orderId}")
	public Order getOrder(@PathVariable Long orderId) throws OrderNotFoundException {
		return orderService.findByOrderId(orderId);
	}
	
	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User user = userService.findByUsername(username);
		Order incomingOrder = order;
		incomingOrder.setUser(user);
		Order savedOrder = orderService.addOrder(incomingOrder);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(order.getOrderId())
				.toUri();
		
		return ResponseEntity.created(location).body(savedOrder);
	}
	
	@PutMapping("/{orderId}")
	public Order updateOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User user = userService.findByUsername(username);
		Order incomingOrder = updatedOrder;
		incomingOrder.setUser(user);
		return orderService.updateOrder(orderId, incomingOrder);
	}
	
	@PatchMapping("/{orderId}")
	public Order partialUpdateOrder(@PathVariable Long orderId, @RequestBody Map<String, Object> fields) {
		return orderService.partialUpdateOrder(orderId, fields);
	}
	
	@DeleteMapping("/{orderId}")
	public void deleteOrder(@PathVariable Long orderId) {
		orderService.deleteOrder(orderId);
	}
}
