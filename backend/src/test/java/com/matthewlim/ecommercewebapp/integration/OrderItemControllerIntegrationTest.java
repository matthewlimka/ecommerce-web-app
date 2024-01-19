package com.matthewlim.ecommercewebapp.integration;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.OrderItemRepository;
import com.matthewlim.ecommercewebapp.repositories.OrderRepository;
import com.matthewlim.ecommercewebapp.repositories.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderItemControllerIntegrationTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private OrderItemRepository orderItemRepo;
	
	private OrderItem testOrderItem;
	
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
        
        Order order = new Order();
        order = orderRepo.save(order);
        Product product = new Product();
        product = productRepo.save(product);
        testOrderItem = new OrderItem(3, BigDecimal.valueOf(89.90), order, product);
        testOrderItem = orderItemRepo.save(testOrderItem);
    }
    
	@Test
	public void testGetOrderItemsEndpoint() throws Exception {
		mockMvc.perform(get("/api/v1/orderItems"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
	}
	
    @Test
    public void testGetOrderItemEndpoint() throws Exception {
        Long orderItemId = testOrderItem.getOrderItemId();
        mockMvc.perform(get("/api/v1/orderItems/{orderItemId}", orderItemId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderItemId", is(orderItemId.intValue())));
    }
    
    @Test
    public void testCreateOrderItemEndpoint() throws Exception {
        OrderItem orderItem = new OrderItem();

        mockMvc.perform(post("/api/v1/orderItems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderItem)))
            .andExpect(status().isCreated());
    }
    
    @Test
    public void testUpdateOrderItemEndpoint() throws Exception {
        Long orderItemId = testOrderItem.getOrderItemId();
        OrderItem updatedOrderItem = new OrderItem();

        mockMvc.perform(put("/api/v1/orderItems/{orderItemId}", orderItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedOrderItem)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testPartialUpdateOrderItemEndpoint() throws Exception {
        Long orderItemId = testOrderItem.getOrderItemId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("quantity", 5);
        fieldsToUpdate.put("subtotal", BigDecimal.valueOf(119.90));
        
        mockMvc.perform(patch("/api/v1/orderItems/{orderItemId}", orderItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsToUpdate)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testDeleteOrderItemEndpoint() throws Exception {
    	Long orderItemId = testOrderItem.getOrderItemId();
    	mockMvc.perform(delete("/api/v1/orderItems/{orderItemId}", orderItemId))
    		.andExpect(status().isOk());
    }
    
    @AfterEach
    public void cleanUp() {
    	orderItemRepo.delete(testOrderItem);
    }
}
