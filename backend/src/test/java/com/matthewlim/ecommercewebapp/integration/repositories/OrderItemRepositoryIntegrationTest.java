package com.matthewlim.ecommercewebapp.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.OrderItemRepository;
import com.matthewlim.ecommercewebapp.repositories.OrderRepository;
import com.matthewlim.ecommercewebapp.repositories.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderItemRepositoryIntegrationTest {
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
    @Test
    public void testFindByQuantity() {
    	int quantity = 5;
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(quantity);
        orderItemRepository.save(orderItem);

        List<OrderItem> result = orderItemRepository.findByQuantity(quantity);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(orderItem, result.get(0));
    }
    
    @Test
    public void testFindBySubtotal() {
    	BigDecimal subtotal = BigDecimal.valueOf(55.00);
        OrderItem orderItem = new OrderItem();
        orderItem.setSubtotal(subtotal);
        orderItemRepository.save(orderItem);

        List<OrderItem> result = orderItemRepository.findBySubtotal(subtotal);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(orderItem, result.get(0));
    }
    
    @Test
    public void testFindByOrder() {
    	Order order = new Order();
        OrderItem orderItem = new OrderItem();
        List<OrderItem> orderItemList = new ArrayList<OrderItem>(Arrays.asList(orderItem));
        orderItem.setOrder(order);
        order.setOrderItems(orderItemList);
        orderRepository.save(order);
        orderItemRepository.save(orderItem);

        List<OrderItem> result = orderItemRepository.findByOrder(order);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(orderItem, result.get(0));
    }
    
    @Test
    public void testFindByProduct() {
    	Product product = new Product();
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        productRepository.save(product);
        orderItemRepository.save(orderItem);

        List<OrderItem> result = orderItemRepository.findByProduct(product);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(orderItem, result.get(0));
    }
}