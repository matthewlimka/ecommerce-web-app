package com.matthewlim.ecommercewebapp.unit.controllers;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthewlim.ecommercewebapp.controllers.OrderController;
import com.matthewlim.ecommercewebapp.enums.OrderStatus;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.services.OrderService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerUnitTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private OrderService orderService;
	
	private Order testOrder;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
		
		testOrder = new Order();
		testOrder.setOrderId(1L);
	}
	
	@Test
	@WithMockUser
	public void testGetOrders() throws Exception {
		List<Order> orderList = new ArrayList<Order>();
		when(orderService.findAllOrders()).thenReturn(orderList);
		
		mockMvc.perform(get("/api/v1/orders"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
			.andExpect(jsonPath("$").isArray());
	}
	
	@Test
	@WithMockUser
	public void testGetOrder() throws Exception {
		Long orderId = testOrder.getOrderId();
		when(orderService.findByOrderId(orderId)).thenReturn(testOrder);
		
		mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.orderId", is(orderId.intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testCreateOrder() throws Exception {
		Order order = new Order(LocalDateTime.now(), BigDecimal.valueOf(20.0), OrderStatus.PENDING, new User(), new ArrayList<OrderItem>());
		order.setOrderId(1L);
		when(orderService.addOrder(testOrder)).thenReturn(testOrder);
		
		mockMvc.perform(post("/api/v1/orders")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(testOrder)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.orderId", is(order.getOrderId().intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testUpdateOrder() throws Exception {
        Long orderId = testOrder.getOrderId();
        Order updatedOrder = new Order(LocalDateTime.now(), BigDecimal.valueOf(129.28), OrderStatus.PROCESSING, new User(), new ArrayList<OrderItem>(Arrays.asList(new OrderItem(), new OrderItem())));
        when(orderService.updateOrder(orderId, testOrder)).thenReturn(updatedOrder);
        
        mockMvc.perform(put("/api/v1/orders/{orderId}", orderId)
        	.with(csrf())
        	.contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedOrder)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testPartialUpdateOrder() throws Exception {
        Long orderId = testOrder.getOrderId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("totalAmount", BigDecimal.valueOf(310.23));
        fieldsToUpdate.put("orderStatus", OrderStatus.PROCESSING);

        mockMvc.perform(patch("/api/v1/orders/{orderId}", orderId)
        	.with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(fieldsToUpdate)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testDeleteOrder() throws Exception {
		Long orderId = testOrder.getOrderId();
		
		mockMvc.perform(delete("/api/v1/orders/{orderId}", orderId)
			.with(csrf()))
			.andExpect(status().isOk());
	}
}