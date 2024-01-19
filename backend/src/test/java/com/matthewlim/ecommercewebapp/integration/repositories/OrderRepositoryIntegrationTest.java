package com.matthewlim.ecommercewebapp.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.matthewlim.ecommercewebapp.enums.OrderStatus;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.OrderItemRepository;
import com.matthewlim.ecommercewebapp.repositories.OrderRepository;
import com.matthewlim.ecommercewebapp.repositories.PaymentRepository;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryIntegrationTest {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private UserRepository userRepository;
	
    @Test
    public void testFindByOrderDate() {
        LocalDateTime orderDate = LocalDateTime.now();
    	Order order = new Order();
        order.setOrderDate(orderDate);
        orderRepository.save(order);

        Optional<Order> result = orderRepository.findByOrderDate(orderDate);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

    @Test
    public void testFindByTotalAmount() {
    	BigDecimal totalAmount = BigDecimal.valueOf(199.00);
        Order order = new Order();
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        List<Order> result = orderRepository.findByTotalAmount(totalAmount);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }
    
    @Test
    public void testFindByOrderStatus() {
    	OrderStatus orderStatus = OrderStatus.PROCESSING;
        Order order = new Order();
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        List<Order> result = orderRepository.findByOrderStatus(orderStatus);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }
    
    @Test
    public void testFindByUser() {
    	User user = new User();
        Order order = new Order();
        List<Order> orderList = new ArrayList<Order>(Arrays.asList(order));
        order.setUser(user);
        user.setOrders(orderList);
        userRepository.save(user);
        orderRepository.save(order);

        List<Order> result = orderRepository.findByUser(user);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }
    
    @Test
    public void testFindByOrderItems() {
        OrderItem orderItem = new OrderItem();
        List<OrderItem> orderItemList = new ArrayList<OrderItem>(Arrays.asList(orderItem));
    	Order order = new Order();
        order.setOrderItems(orderItemList);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);
        orderRepository.save(order);

        Optional<Order> result = orderRepository.findByOrderItems(orderItem);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }
    
    @Test
    public void testFindByPayment() {
        Payment payment = new Payment();
    	Order order = new Order();
        order.setPayment(payment);
        payment.setOrder(order);
        paymentRepository.save(payment);
        orderRepository.save(order);

        Optional<Order> result = orderRepository.findByPayment(payment);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }
}