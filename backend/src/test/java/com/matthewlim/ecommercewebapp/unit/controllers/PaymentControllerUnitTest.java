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
import com.matthewlim.ecommercewebapp.controllers.PaymentController;
import com.matthewlim.ecommercewebapp.enums.PaymentMethod;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.services.PaymentService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PaymentController.class)
public class PaymentControllerUnitTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private PaymentService paymentService;
	
	private Payment testPayment;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
		
		testPayment = new Payment();
		testPayment.setPaymentId(1L);
	}
	
	@Test
	@WithMockUser
	public void testGetPayments() throws Exception {
		List<Payment> paymentList = new ArrayList<Payment>();
		when(paymentService.findAllPayments()).thenReturn(paymentList);
		
		mockMvc.perform(get("/api/v1/payments"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
			.andExpect(jsonPath("$").isArray());
	}
	
	@Test
	@WithMockUser
	public void testGetPayment() throws Exception {
		Long paymentId = testPayment.getPaymentId();
		when(paymentService.findByPaymentId(paymentId)).thenReturn(testPayment);
		
		mockMvc.perform(get("/api/v1/payments/{paymentId}", paymentId))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.paymentId", is(paymentId.intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testCreatePayment() throws Exception {
		Payment payment = new Payment(LocalDateTime.now(), "123456", BigDecimal.valueOf(20.0), PaymentMethod.CREDIT_CARD_MASTERCARD, new Order());
		payment.setPaymentId(1L);
		when(paymentService.addPayment(testPayment)).thenReturn(testPayment);
		
		mockMvc.perform(post("/api/v1/payments")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(testPayment)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.paymentId", is(payment.getPaymentId().intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testUpdatePayment() throws Exception {
        Long paymentId = testPayment.getPaymentId();
        Payment updatedPayment = new Payment(LocalDateTime.now(), "123456", BigDecimal.valueOf(129.28), PaymentMethod.CREDIT_CARD_VISA, new Order());
        when(paymentService.updatePayment(paymentId, testPayment)).thenReturn(updatedPayment);
        
        mockMvc.perform(put("/api/v1/payments/{paymentId}", paymentId)
        	.with(csrf())
        	.contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedPayment)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testPartialUpdatePayment() throws Exception {
        Long paymentId = testPayment.getPaymentId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("amount", BigDecimal.valueOf(683.10));
        fieldsToUpdate.put("paymentMethod", PaymentMethod.PAYNOW);

        mockMvc.perform(patch("/api/v1/payments/{paymentId}", paymentId)
        	.with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(fieldsToUpdate)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testDeletePayment() throws Exception {
		Long paymentId = testPayment.getPaymentId();
		
		mockMvc.perform(delete("/api/v1/payments/{paymentId}", paymentId)
			.with(csrf()))
			.andExpect(status().isOk());
	}
}