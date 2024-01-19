package com.matthewlim.ecommercewebapp.integration.controllers;

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
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthewlim.ecommercewebapp.enums.PaymentMethod;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.repositories.OrderRepository;
import com.matthewlim.ecommercewebapp.repositories.PaymentRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerIntegrationTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private PaymentRepository paymentRepo;
	
	private Payment testPayment;
	
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
        
        Order order = new Order();
        order = orderRepo.save(order);
        testPayment = new Payment(LocalDateTime.now(), "123456", BigDecimal.valueOf(990.87), PaymentMethod.CreditCard_Mastercard, order);
        testPayment = paymentRepo.save(testPayment);
    }
    
	@Test
	@WithMockUser
	public void testGetPaymentsEndpoint() throws Exception {
		mockMvc.perform(get("/api/v1/payments"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
	}
	
    @Test
    @WithMockUser
    public void testGetPaymentEndpoint() throws Exception {
        Long paymentId = testPayment.getPaymentId();
        mockMvc.perform(get("/api/v1/payments/{paymentId}", paymentId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentId", is(paymentId.intValue())));
    }
    
    @Test
    @WithMockUser
    public void testCreatePaymentEndpoint() throws Exception {
        Payment payment = new Payment();

        mockMvc.perform(post("/api/v1/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(payment)))
            .andExpect(status().isCreated());
    }
    
    @Test
    @WithMockUser
    public void testUpdatePaymentEndpoint() throws Exception {
        Long paymentId = testPayment.getPaymentId();
        Payment updatedPayment = new Payment();

        mockMvc.perform(put("/api/v1/payments/{paymentId}", paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPayment)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void testPartialUpdatePaymentEndpoint() throws Exception {
        Long paymentId = testPayment.getPaymentId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("amount", BigDecimal.valueOf(1299.90));
        fieldsToUpdate.put("paymentMethod", PaymentMethod.PayNow);
        
        mockMvc.perform(patch("/api/v1/payments/{paymentId}", paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsToUpdate)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void testDeletePaymentEndpoint() throws Exception {
    	Long paymentId = testPayment.getPaymentId();
    	mockMvc.perform(delete("/api/v1/payments/{paymentId}", paymentId))
    		.andExpect(status().isOk());
    }
    
    @AfterEach
    public void cleanUp() {
    	paymentRepo.delete(testPayment);
    }
}