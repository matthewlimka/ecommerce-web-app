package com.matthewlim.ecommercewebapp.controllers;

import java.math.BigDecimal;
import java.net.URI;
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

import com.matthewlim.ecommercewebapp.exceptions.OrderItemNotFoundException;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.services.OrderItemService;

@RestController
@RequestMapping("api/v1/orderItems")
public class OrderItemController {
	
	@Autowired
	private OrderItemService orderItemService;

	@GetMapping
	public List<OrderItem> getOrderItems(@RequestParam(required = false) Integer quantity, @RequestParam(required = false) BigDecimal subtotal, @RequestParam(required = false) Order order, @RequestParam(required = false) Product product) {
		if ( quantity != null ) {
			return orderItemService.findByQuantity(quantity);
		} else if ( subtotal != null ) {
			return orderItemService.findBySubtotal(subtotal);
		} else if ( order != null ) {
			return orderItemService.findByOrder(order);
		} else if ( product != null ) {
			return orderItemService.findByProduct(product);
		} else {
			return orderItemService.findAllOrderItems();	
		}
	}

	@GetMapping("/{id}")
	public OrderItem getOrderItem(@PathVariable Long orderItemId) throws OrderItemNotFoundException {
		return orderItemService.findByOrderItemId(orderItemId);
	}
	
	@PostMapping
	public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
		OrderItem savedOrderItem = orderItemService.addOrderItem(orderItem);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(orderItem.getOrderItemId())
				.toUri();
		
		return ResponseEntity.created(location).body(savedOrderItem);
	}
	
	@PutMapping("/{id}")
	public OrderItem updateOrderItem(@PathVariable Long orderItemId, @RequestBody OrderItem updatedOrderItem) {
		return orderItemService.updateOrderItem(orderItemId, updatedOrderItem);
	}
	
	@PatchMapping("/{id}")
	public OrderItem partialUpdateOrderItem(@PathVariable Long orderItemId, @RequestBody Map<String, Object> fields) {
		return orderItemService.partialUpdateOrderItem(orderItemId, fields);
	}
	
	@DeleteMapping("/{id}")
	public void deleteOrderItem(@PathVariable Long orderItemId) {
		orderItemService.deleteOrderItem(orderItemId);
	}
}
