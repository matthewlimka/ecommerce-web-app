package com.matthewlim.ecommercewebapp.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

import com.matthewlim.ecommercewebapp.enums.OrderStatus;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.OrderRepository;
import com.matthewlim.ecommercewebapp.services.OrderService;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTest {

	@Mock
	private OrderRepository orderRepository;
	
	@InjectMocks
	private OrderService orderService;
	
	@Test
	public void testFindAllOrders() {
		List<Order> orderList = new ArrayList<Order>(Arrays.asList(new Order(), new Order()));
		
		when(orderRepository.findAll()).thenReturn(orderList);
		List<Order> result = orderService.findAllOrders();
		
		assertNotNull(result);
		assertThat(result).hasSize(orderList.size()).containsExactlyElementsOf(orderList);
	}
	
	@Test
	public void testFindByOrderId() {
		Long orderId = 1L;
		Order order = new Order();
		order.setOrderId(orderId);
		
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
		Order result = orderService.findByOrderId(orderId);
		
		assertNotNull(result);
		assertEquals(orderId, result.getOrderId());
	}
	
	@Test
	public void testFindByOrderDate() {
		LocalDateTime orderDate = LocalDateTime.now();
		Order order = new Order();
		order.setOrderDate(orderDate);

		when(orderRepository.findByOrderDate(orderDate)).thenReturn(Optional.of(order));
		Order result = orderService.findByOrderDate(orderDate);
		
		assertNotNull(result);
		assertEquals(orderDate, result.getOrderDate());
	}
	
	@Test
	public void testFindByTotalAmount() {
		BigDecimal totalAmount = BigDecimal.valueOf(88.10);
		Order order = new Order();
		order.setTotalAmount(totalAmount);
		List<Order> orderList = new ArrayList<Order>(Arrays.asList(order));
		
		when(orderRepository.findByTotalAmount(totalAmount)).thenReturn(orderList);
		List<Order> result = orderService.findByTotalAmount(totalAmount);
		
		assertNotNull(result);
		assertThat(result).hasSize(orderList.size()).containsExactlyElementsOf(orderList);
	}
	
	@Test
	public void testFindByOrderStatus() {
		OrderStatus orderStatus = OrderStatus.PROCESSING;
		Order order = new Order();
		order.setOrderStatus(orderStatus);
		List<Order> orderList = new ArrayList<Order>(Arrays.asList(order));
		
		when(orderRepository.findByOrderStatus(orderStatus)).thenReturn(orderList);
		List<Order> result = orderService.findByOrderStatus(orderStatus);
		
		assertNotNull(result);
		assertThat(result).hasSize(orderList.size()).containsExactlyElementsOf(orderList);
	}
	
	@Test
	public void testFindByUser() {
		User user = new User();
		Order order = new Order();
		order.setUser(user);
		List<Order> orderList = new ArrayList<Order>(Arrays.asList(order));
		
		when(orderRepository.findByUser(user)).thenReturn(orderList);
		List<Order> result = orderService.findByUser(user);
		
		assertNotNull(result);
		assertThat(result).hasSize(orderList.size()).containsExactlyElementsOf(orderList);
	}
	
	@Test
	public void testFindByOrderItems() {
		OrderItem orderItem = new OrderItem();
		List<OrderItem> orderItemList = new ArrayList<OrderItem>(Arrays.asList(orderItem));
		Order order = new Order();
		order.setOrderItems(orderItemList);
		
		when(orderRepository.findByOrderItems(orderItem)).thenReturn(Optional.of(order));
		Order result = orderService.findByOrderItems(orderItem);
		
		assertNotNull(result);
		assertEquals(orderItemList, result.getOrderItems());
	}
	
	@Test
	public void testFindByPayment() {
		Payment payment = new Payment();
		Order order = new Order();
		order.setPayment(payment);

		when(orderRepository.findByPayment(payment)).thenReturn(Optional.of(order));
		Order result = orderService.findByPayment(payment);
		
		assertNotNull(result);
		assertEquals(payment, result.getPayment());
	}
	
	@Test
	public void testAddOrder() {
		Order order = new Order();
		
		when(orderRepository.save(order)).thenReturn(order);
		Order result = orderService.addOrder(order);
		
		assertNotNull(result);
		verify(orderRepository, times(1)).save(order);
	}
	
	@Test
	public void testUpdateOrder() {
		Long orderId = 1L;
		Order existingOrder = new Order();
		existingOrder.setOrderId(orderId);
		
		Order updatedOrder = new Order();
		updatedOrder.setUser(new User());
		updatedOrder.setOrderStatus(OrderStatus.SHIPPED);
		
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
		when(orderRepository.save(existingOrder)).thenReturn(updatedOrder);
		Order result = orderService.updateOrder(orderId, updatedOrder);
		
		assertNotNull(result);
		assertEquals(updatedOrder.getUser(), result.getUser());
		assertEquals(updatedOrder.getOrderStatus(), result.getOrderStatus());
	}
	
	@Test
	public void testPartialUpdateOrder() {
		Long orderId = 1L;
		Order existingOrder = new Order();
		existingOrder.setOrderId(orderId);
		
		Map<String, Object> fieldsToUpdate = new HashMap<String, Object>();
		fieldsToUpdate.put("orderStatus", OrderStatus.DELIVERED);
		fieldsToUpdate.put("orderItems", new ArrayList<OrderItem>());
		
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
		when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
		Order result = orderService.partialUpdateOrder(orderId, fieldsToUpdate);
		
		assertNotNull(result);
		assertEquals(fieldsToUpdate.get("orderStatus"), result.getOrderStatus());
		assertEquals(fieldsToUpdate.get("orderItems"), result.getOrderItems());
	}
	
	@Test
	public void testDeleteOrder() {
		Long orderId = 1L;
		Order order = new Order();
		order.setOrderId(orderId);
		
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
		orderService.deleteOrder(orderId);
		
		verify(orderRepository, times(1)).delete(order);
	}
}