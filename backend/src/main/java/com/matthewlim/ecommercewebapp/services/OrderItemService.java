package com.matthewlim.ecommercewebapp.services;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.matthewlim.ecommercewebapp.exceptions.OrderItemNotFoundException;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.OrderItemRepository;

@Service
public class OrderItemService {

	Logger logger = LogManager.getLogger(OrderItemService.class);
	
	@Autowired
	private OrderItemRepository orderItemRepo;
	
	public OrderItem findByOrderItemId(Long orderItemId) {
		OrderItem orderItem = orderItemRepo.findById(orderItemId)
				.orElseThrow(() -> {
					logger.error("No order item with order item ID " + orderItemId + " found");
					return new OrderItemNotFoundException("No order item with order item ID " + orderItemId + " found");
				});
		
		logger.info("Successfully found order item with order item ID " + orderItemId);
		return orderItem;
	}
	
	public List<OrderItem> findByQuantity(int quantity) {
		List<OrderItem> orderItems = orderItemRepo.findByQuantity(quantity);
		
		logger.info("Successfully found " + orderItems.size() + " order item(s) with quantity " + quantity);
		return orderItems;
	}
	
	public List<OrderItem> findBySubtotal(BigDecimal subtotal) {
		List<OrderItem> orderItems = orderItemRepo.findBySubtotal(subtotal);
		
		logger.info("Successfully found " + orderItems.size() + " order item(s) with subtotal " + subtotal);
		return orderItems;
	}
	
	public List<OrderItem> findByOrder(Order order) {
		List<OrderItem> orderItems = orderItemRepo.findByOrder(order);
		
		logger.info("Successfully found " + orderItems.size() + " order item(s) with order ID " + order.getOrderId());
		return orderItems;
	}
	
	public List<OrderItem> findByProduct(Product product) {
		List<OrderItem> orderItems = orderItemRepo.findByProduct(product);
		
		logger.info("Successfully found " + orderItems.size() + " order item(s) with product ID " + product.getProductId());
		return orderItems;
	}
	
	public List<OrderItem> findAllOrderItems() {
		List<OrderItem> orderItemList = orderItemRepo.findAll();
		
		logger.info("Successfully found " + orderItemList.size() + " order items");
		return orderItemList;
	}
	
	public OrderItem addOrderItem(OrderItem orderItem) {
		logger.info("Successfully registered new order item with order item ID " + orderItem.getOrderItemId());
		return orderItemRepo.save(orderItem);
	}
	
	public OrderItem updateOrderItem(Long orderItemId, OrderItem updatedOrderItem) {
		OrderItem existingOrderItem = orderItemRepo.findById(orderItemId)
				.orElseThrow(() -> {
					logger.error("No order item with order item ID " + orderItemId + " found");
					return new OrderItemNotFoundException("No order item with order item ID " + orderItemId + " found");
				});
		
		existingOrderItem.setQuantity(updatedOrderItem.getQuantity());
		existingOrderItem.setSubtotal(updatedOrderItem.getSubtotal());
		existingOrderItem.setOrder(updatedOrderItem.getOrder());
		existingOrderItem.setProduct(updatedOrderItem.getProduct());
		
		logger.info("Successfully updated order item with order item ID " + orderItemId);
		return orderItemRepo.save(existingOrderItem);
	}
	
	public OrderItem partialUpdateOrderItem(Long orderItemId, Map<String, Object> fields) {
		OrderItem existingOrderItem = orderItemRepo.findById(orderItemId)
				.orElseThrow(() -> {
					logger.error("No order item with order item ID " + orderItemId + " found");
					return new OrderItemNotFoundException("No order item with order item ID " + orderItemId + " found");
				});
		
		fields.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(OrderItem.class, key);
			field.setAccessible(true);
			
			if (field.getType() == BigDecimal.class && value instanceof Number) {
				value = new BigDecimal(((Number) value).doubleValue());
			}
			
			if (key.equals("quantity") && !fields.containsKey("subtotal")) {
				BigDecimal subtotal = BigDecimal.valueOf(((Integer) value).doubleValue() * existingOrderItem.getProduct().getPrice().doubleValue());
				subtotal.setScale(2, RoundingMode.HALF_UP);
				existingOrderItem.setSubtotal(subtotal);
			}
			
			ReflectionUtils.setField(field, existingOrderItem, value);
		});
		
		logger.info("Successfully updated order item with order item ID " + orderItemId);
		return orderItemRepo.save(existingOrderItem);
	}
	
	public void deleteOrderItem(Long orderItemId) {
		OrderItem existingOrderItem = orderItemRepo.findById(orderItemId)
				.orElseThrow(() -> {
					logger.error("No order item with order item ID " + orderItemId + " found");
					return new OrderItemNotFoundException("No order item with order item ID " + orderItemId + " found");
				});
		
		logger.info("Successfully deleted order item with order item ID " + orderItemId);
		orderItemRepo.delete(existingOrderItem);
	}
}
