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
import java.util.ArrayList;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthewlim.ecommercewebapp.controllers.OrderItemController;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.services.OrderItemService;
import com.matthewlim.ecommercewebapp.services.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderItemController.class)
public class OrderItemControllerUnitTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private JwtDecoder jwtDecoder;
	
	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private OrderItemService orderItemService;
	
	private OrderItem testOrderItem;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
		
		testOrderItem = new OrderItem();
		testOrderItem.setOrderItemId(1L);
	}
	
	@Test
	@WithMockUser
	public void testGetOrderItems() throws Exception {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		when(orderItemService.findAllOrderItems()).thenReturn(orderItemList);
		
		mockMvc.perform(get("/api/v1/orderItems"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
			.andExpect(jsonPath("$").isArray());
	}
	
	@Test
	@WithMockUser
	public void testGetOrderItem() throws Exception {
		Long orderItemId = testOrderItem.getOrderItemId();
		when(orderItemService.findByOrderItemId(orderItemId)).thenReturn(testOrderItem);
		
		mockMvc.perform(get("/api/v1/orderItems/{orderItemId}", orderItemId))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.orderItemId", is(orderItemId.intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testCreateOrderItem() throws Exception {
		OrderItem orderItem = new OrderItem(2, BigDecimal.valueOf(50.0), new Order(), new Product());
		orderItem.setOrderItemId(1L);
		when(orderItemService.addOrderItem(testOrderItem)).thenReturn(testOrderItem);
		
		mockMvc.perform(post("/api/v1/orderItems")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(testOrderItem)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.orderItemId", is(orderItem.getOrderItemId().intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testUpdateOrderItem() throws Exception {
        Long orderItemId = testOrderItem.getOrderItemId();
        OrderItem updatedOrderItem = new OrderItem(2, BigDecimal.valueOf(61.95), new Order(), new Product());
        when(orderItemService.updateOrderItem(orderItemId, testOrderItem)).thenReturn(updatedOrderItem);
        
        mockMvc.perform(put("/api/v1/orderItems/{orderItemId}", orderItemId)
        	.with(csrf())
        	.contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedOrderItem)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testPartialUpdateOrderItem() throws Exception {
        Long orderItemId = testOrderItem.getOrderItemId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("quantity", 4);
        fieldsToUpdate.put("subtotal", BigDecimal.valueOf(123.90));

        mockMvc.perform(patch("/api/v1/orderItems/{orderItemId}", orderItemId)
        	.with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(fieldsToUpdate)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testDeleteOrderItem() throws Exception {
		Long orderItemId = testOrderItem.getOrderItemId();
		
		mockMvc.perform(delete("/api/v1/orderItems/{orderItemId}", orderItemId)
			.with(csrf()))
			.andExpect(status().isOk());
	}
}