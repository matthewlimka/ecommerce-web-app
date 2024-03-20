package com.matthewlim.ecommercewebapp.services;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.matthewlim.ecommercewebapp.enums.OrderStatus;
import com.matthewlim.ecommercewebapp.exceptions.OrderNotFoundException;
import com.matthewlim.ecommercewebapp.exceptions.ProductNotFoundException;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.OrderRepository;
import com.matthewlim.ecommercewebapp.repositories.ProductRepository;

@Service
public class OrderService {

	Logger logger = LogManager.getLogger(OrderService.class);
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
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
	
	public Order findByOrderItems(OrderItem orderItem) {
		Order order = orderRepo.findByOrderItems(orderItem)
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
	
	public List<Order> findAllOrders() {
		List<Order> orderList = orderRepo.findAll();
		
		logger.info("Successfully found " + orderList.size() + " orders");
		return orderList;
	}
	
	public Order addOrder(Order order) {
		Order updatedOrder = order;
		for (OrderItem orderItem: order.getOrderItems()) {
			orderItem.setOrder(updatedOrder);
		}
		Order savedOrder = orderRepo.save(updatedOrder);
		logger.info("Successfully registered new order with order ID " + savedOrder.getOrderId());
		return savedOrder;
	}
	
	public Order updateOrder(Long orderId, Order updatedOrder) {
		Order existingOrder = orderRepo.findById(orderId)
				.orElseThrow(() -> {
					logger.error("No order with order ID " + orderId + " found");
					return new OrderNotFoundException("No order with order ID " + orderId + " found");
				});
		
		Payment updatedOrderPayment = updatedOrder.getPayment();
		String transactionId = "SG-" + updatedOrderPayment.getPaymentDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		Payment payment = new Payment(updatedOrderPayment.getPaymentDate(), transactionId, updatedOrderPayment.getAmount(), updatedOrderPayment.getPaymentMethod(), existingOrder);
		
		existingOrder.setOrderDate(updatedOrder.getOrderDate());
		existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
		existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
		existingOrder.setUser(updatedOrder.getUser());
		existingOrder.setOrderItems(updatedOrder.getOrderItems());
		existingOrder.setPayment(payment);
		
		for (OrderItem orderItem: existingOrder.getOrderItems()) {
			orderItem.setOrder(existingOrder);
			
			// Update product's stock quantity once order has been processed
			if (existingOrder.getOrderStatus().equals(OrderStatus.PROCESSING)) {
				Product productToUpdate = productRepo.findById(orderItem.getProduct().getProductId())
						.orElseThrow(() -> {
							logger.error("No product with product ID " + orderItem.getProduct().getProductId() + " found");
							return new ProductNotFoundException("No product with product ID " + orderItem.getProduct().getProductId() + " found");
						});
				int updatedStockQuantity = productToUpdate.getStockQuantity() - orderItem.getQuantity();
				
				if (updatedStockQuantity > 0) {					
					productToUpdate.setStockQuantity(updatedStockQuantity);
					orderItem.setProduct(productToUpdate);
				} else {
					throw new RuntimeException("Failed to update stock quantity - insufficient stock for order item ID " + orderItem.getOrderItemId());
				}
			}
		}
		
		logger.info("Successfully updated order with order ID " + orderId);
		return orderRepo.save(existingOrder);
	}
	
	public Order partialUpdateOrder(Long orderId, Map<String, Object> fields) {
		Order existingOrder = orderRepo.findById(orderId)
				.orElseThrow(() -> {
					logger.error("No order with order ID " + orderId + " found");
					return new OrderNotFoundException("No order with order ID " + orderId + " found");
				});
		
		fields.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(Order.class, key);
			field.setAccessible(true);
			
			if (field.getType() == OrderStatus.class && value instanceof String) {
				value = OrderStatus.valueOf((String) value);
			} else if (field.getType() == BigDecimal.class && value instanceof Number) {
				value = new BigDecimal(((Number) value).doubleValue());
			}
			
			ReflectionUtils.setField(field, existingOrder, value);
		});
		
		logger.info("Successfully updated order with order ID " + orderId);
		return orderRepo.save(existingOrder);
	}
	
	public void deleteOrder(Long orderId) {
		Order existingOrder = orderRepo.findById(orderId)
				.orElseThrow(() -> {
					logger.error("No order with order ID " + orderId + " found");
					return new OrderNotFoundException("No order with order ID " + orderId + " found");
				});
		
		logger.info("Successfully deleted order with order ID " + orderId);
		orderRepo.delete(existingOrder);
	}
}
