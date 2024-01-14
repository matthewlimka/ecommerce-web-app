package com.matthewlim.ecommercewebapp.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matthewlim.ecommercewebapp.enums.OrderStatus;
import com.matthewlim.ecommercewebapp.exceptions.OrderNotFoundException;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.OrderRepository;

@Service
public class OrderService {

	Logger logger = LogManager.getLogger(OrderService.class);
	
	@Autowired
	private OrderRepository orderRepo;
	
	public Order findByOrderId(Long orderId) {
		Order order = orderRepo.findById(orderId)
				.orElseThrow(() -> {
					logger.error("No order with order ID " + orderId + " found");
					return new OrderNotFoundException("No order with order ID " + orderId + " found");
				});
		
		logger.info("Successfully found order with order ID " + orderId);
		return order;
	}
	
	public Order findByOrderDate(LocalDateTime orderDate) {
		Order order = orderRepo.findByOrderDate(orderDate)
				.orElseThrow(() -> {
					logger.error("No order with order date " + orderDate + " found");
					return new OrderNotFoundException("No order with order date " + orderDate + " found");
				});
		
		logger.info("Successfully found order with order date " + orderDate);
		return order;
	}
	
	public List<Order> findByTotalAmount(BigDecimal totalAmount) {
		List<Order> orders = orderRepo.findByTotalAmount(totalAmount);
		
		logger.info("Successfully found " + orders.size() + " order(s) with total amount " + totalAmount);
		return orders;
	}
	
	public List<Order> findByOrderStatus(OrderStatus orderStatus) {
		List<Order> orders = orderRepo.findByOrderStatus(orderStatus);
		
		logger.info("Successfully found " + orders.size() + " order(s) with order status " + orderStatus);
		return orders;
	}
	
	public List<Order> findByUser(User user) {
		List<Order> orders = orderRepo.findByUser(user);
		
		logger.info("Successfully found " + orders.size() + " order(s) with user ID " + user.getUserId());
		return orders;
	}
	
	public Order findByOrderItem(OrderItem orderItem) {
		Order order = orderRepo.findByOrderItem(orderItem)
				.orElseThrow(() -> {
					logger.error("No order with order item ID " + orderItem.getOrderItemId() + " found");
					return new OrderNotFoundException("No order with order item ID " + orderItem.getOrderItemId() + " found");
				});
		
		logger.info("Successfully found order with order item ID " + orderItem.getOrderItemId());
		return order;
	}
	
	public Order findByPayment(Payment payment) {
		Order order = orderRepo.findByPayment(payment)
				.orElseThrow(() -> {
					logger.error("No order with payment ID " + payment.getPaymentId() + " found");
					return new OrderNotFoundException("No order with payment ID" + payment.getPaymentId() + " found");
				});
		
		logger.info("Successfully found order with payment ID " + payment.getPaymentId());
		return order;
	}
	
	public Order addOrder(Order order) {
		logger.info("Successfully registered new order with order ID " + order.getOrderId());
		return orderRepo.save(order);
	}
	
	public Order updateOrder(Long orderId, Order updatedOrder) {
		Order existingOrder = orderRepo.findById(orderId)
				.orElseThrow(() -> {
					logger.error("No order with order ID " + orderId + " found");
					return new OrderNotFoundException("No order with order ID " + orderId + " found");
				});
		
		existingOrder.setOrderDate(updatedOrder.getOrderDate());
		existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
		existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
		existingOrder.setUser(updatedOrder.getUser());
		existingOrder.setOrderItems(updatedOrder.getOrderItems());
		existingOrder.setPayment(updatedOrder.getPayment());		
		
		logger.info("Successfully updated order with order ID " + orderId);
		
		return orderRepo.save(existingOrder);
	}
	
	public void deleteOrder(Long orderId) {
		Order existingOrder = orderRepo.findById(orderId)
				.orElseThrow(() -> {
					logger.error("No order with order ID " + orderId + " found");
					return new OrderNotFoundException("No order with order ID " + orderId + " found");
				});
		
		logger.info("Successfully delete order with order ID " + orderId);
		orderRepo.delete(existingOrder);
	}
}
