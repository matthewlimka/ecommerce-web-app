package com.matthewlim.ecommercewebapp.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.OrderItemRepository;
import com.matthewlim.ecommercewebapp.services.OrderItemService;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceUnitTest {

	@Mock
	private OrderItemRepository orderItemRepository;
	
	@InjectMocks
	private OrderItemService orderItemService;
	
	@Test
	public void testFindAllOrderItems() {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>(Arrays.asList(new OrderItem(), new OrderItem()));		
		
		when(orderItemRepository.findAll()).thenReturn(orderItemList);
		List<OrderItem> result = orderItemService.findAllOrderItems();
		
		assertNotNull(result);
		assertThat(result).hasSize(orderItemList.size()).containsExactlyElementsOf(orderItemList);
	}
	
	@Test
	public void testFindByOrderItemId() {
		Long orderItemId = 1L;
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderItemId(orderItemId);
		
		when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(orderItem));
		OrderItem result = orderItemService.findByOrderItemId(orderItemId);
		
		assertNotNull(result);
		assertEquals(orderItemId, result.getOrderItemId());
	}
	
	@Test
	public void testFindByQuantity() {
		int quantity = 2;
		OrderItem orderItem = new OrderItem();
		orderItem.setQuantity(quantity);
		List<OrderItem> orderItemList = new ArrayList<OrderItem>(Arrays.asList(orderItem));
		
		when(orderItemRepository.findByQuantity(quantity)).thenReturn(orderItemList);
		List<OrderItem> result = orderItemService.findByQuantity(quantity);
		
		assertNotNull(result);
		assertThat(result).hasSize(orderItemList.size()).containsExactlyElementsOf(orderItemList);
	}
	
	@Test
	public void testFindBySubtotal() {
		BigDecimal subtotal = BigDecimal.valueOf(20.50);
		OrderItem orderItem = new OrderItem();
		orderItem.setSubtotal(subtotal);
		List<OrderItem> orderItemList = new ArrayList<OrderItem>(Arrays.asList(orderItem));
		
		when(orderItemRepository.findBySubtotal(subtotal)).thenReturn(orderItemList);
		List<OrderItem> result = orderItemService.findBySubtotal(subtotal);
		
		assertNotNull(result);
		assertThat(result).hasSize(orderItemList.size()).containsExactlyElementsOf(orderItemList);
	}
	
	@Test
	public void testFindByOrder() {
		Order order = new Order();
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		List<OrderItem> orderItemList = new ArrayList<OrderItem>(Arrays.asList(orderItem));

		when(orderItemRepository.findByOrder(order)).thenReturn(orderItemList);
		List<OrderItem> result = orderItemService.findByOrder(order);
		
		assertNotNull(result);
		assertThat(result).hasSize(orderItemList.size()).containsExactlyElementsOf(orderItemList);
	}
	
	@Test
	public void testFindByProduct() {
		Product product = new Product();
		OrderItem orderItem = new OrderItem();
		orderItem.setProduct(product);
		List<OrderItem> orderItemList = new ArrayList<OrderItem>(Arrays.asList(orderItem));

		when(orderItemRepository.findByProduct(product)).thenReturn(orderItemList);
		List<OrderItem> result = orderItemService.findByProduct(product);
		
		assertNotNull(result);
		assertThat(result).hasSize(orderItemList.size()).containsExactlyElementsOf(orderItemList);
	}
	
	@Test
	public void testAddOrderItem() {
		OrderItem orderItem = new OrderItem();
		
		when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
		OrderItem result = orderItemService.addOrderItem(orderItem);
		
		assertNotNull(result);
		verify(orderItemRepository, times(1)).save(orderItem);
	}
	
	@Test
	public void testUpdateOrderItem() {
		Long orderItemId = 1L;
		OrderItem existingOrderItem = new OrderItem();
		existingOrderItem.setOrderItemId(orderItemId);
		
		OrderItem updatedOrderItem = new OrderItem();
		updatedOrderItem.setQuantity(5);
		updatedOrderItem.setSubtotal(BigDecimal.valueOf(35.28));
		
		when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(existingOrderItem));
		when(orderItemRepository.save(existingOrderItem)).thenReturn(updatedOrderItem);
		OrderItem result = orderItemService.updateOrderItem(orderItemId, updatedOrderItem);
		
		assertNotNull(result);
		assertEquals(updatedOrderItem.getQuantity(), result.getQuantity());
		assertEquals(updatedOrderItem.getSubtotal().setScale(2, RoundingMode.HALF_UP), result.getSubtotal().setScale(2, RoundingMode.HALF_UP));
	}
	
	@Test
	public void testPartialUpdateOrderItem() {
		Long orderItemId = 1L;
		OrderItem existingOrderItem = new OrderItem();
		existingOrderItem.setOrderItemId(orderItemId);
		
		Map<String, Object> fieldsToUpdate = new HashMap<String, Object>();
		fieldsToUpdate.put("quantity", 10);
		fieldsToUpdate.put("subtotal", BigDecimal.valueOf(40.90));
		
		when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(existingOrderItem));
		when(orderItemRepository.save(existingOrderItem)).thenReturn(existingOrderItem);
		OrderItem result = orderItemService.partialUpdateOrderItem(orderItemId, fieldsToUpdate);
		
		assertNotNull(result);
		assertEquals(fieldsToUpdate.get("quantity"), result.getQuantity());
		assertEquals(((BigDecimal) fieldsToUpdate.get("subtotal")).setScale(2, RoundingMode.HALF_UP), result.getSubtotal().setScale(2, RoundingMode.HALF_UP));
	}
	
	@Test
	public void testDeleteOrderItem() {
		Long orderItemId = 1L;
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderItemId(orderItemId);
		
		when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(orderItem));
		orderItemService.deleteOrderItem(orderItemId);
		
		verify(orderItemRepository, times(1)).delete(orderItem);
	}
}