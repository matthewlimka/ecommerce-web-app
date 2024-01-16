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

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;

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

	@GetMapping("/{id}")
	public Order getOrder(@PathVariable Long orderId) throws OrderNotFoundException {
		return orderService.findByOrderId(orderId);
	}
	
	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		Order savedOrder = orderService.addOrder(order);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(order.getOrderId())
				.toUri();
		
		return ResponseEntity.created(location).body(savedOrder);
	}
	
	@PutMapping("/{id}")
	public Order updateOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder) {
		return orderService.updateOrder(orderId, updatedOrder);
	}
	
	@PatchMapping("/{id}")
	public Order partialUpdateOrder(@PathVariable Long orderId, @RequestBody Map<String, Object> fields) {
		return orderService.partialUpdateOrder(orderId, fields);
	}
	
	@DeleteMapping("/{id}")
	public void deleteOrder(@PathVariable Long orderId) {
		orderService.deleteOrder(orderId);
	}
}
