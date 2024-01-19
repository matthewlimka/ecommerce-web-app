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
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.matthewlim.ecommercewebapp.enums.OrderStatus;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.OrderRepository;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	private Order testOrder;
	
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
        
        User user = new User();
        user = userRepo.save(user);
        testOrder = new Order(LocalDateTime.now(), BigDecimal.valueOf(26.67), OrderStatus.PENDING, user, null);
        testOrder = orderRepo.save(testOrder);
    }
    
	@Test
	public void testGetOrdersEndpoint() throws Exception {
		mockMvc.perform(get("/api/v1/orders"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
	}
	
    @Test
    public void testGetOrderEndpoint() throws Exception {
        Long orderId = testOrder.getOrderId();
        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId", is(orderId.intValue())));
    }
    
    @Test
    public void testCreateOrderEndpoint() throws Exception {
        Order order = new Order();

        mockMvc.perform(post("/api/v1/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(order)))
            .andExpect(status().isCreated());
    }
    
    @Test
    public void testUpdateOrderEndpoint() throws Exception {
        Long orderId = testOrder.getOrderId();
        Order updatedOrder = new Order();

        mockMvc.perform(put("/api/v1/orders/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testPartialUpdateOrderEndpoint() throws Exception {
        Long orderId = testOrder.getOrderId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("orderItems", new ArrayList<OrderItem>());
        fieldsToUpdate.put("orderStatus", OrderStatus.PROCESSING);

        mockMvc.perform(patch("/api/v1/orders/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsToUpdate)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testDeleteOrderEndpoint() throws Exception {
    	Long orderId = testOrder.getOrderId();
    	mockMvc.perform(delete("/api/v1/orders/{orderId}", orderId))
    		.andExpect(status().isOk());
    }
    
    @AfterEach
    public void cleanUp() {
    	orderRepo.delete(testOrder);
    }
}
