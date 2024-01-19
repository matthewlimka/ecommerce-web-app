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
import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.AddressRepository;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerIntegrationTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AddressRepository addressRepo;
	
	private Address testAddress;
	
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
        
        User user = new User();
        user = userRepo.save(user);
        testAddress = new Address("Wall Street", "New York City", "New York", "123456", "USA", user);
        testAddress = addressRepo.save(testAddress);
    }
    
	@Test
	@WithMockUser
	public void testGetAddressesEndpoint() throws Exception {
		mockMvc.perform(get("/api/v1/addresses"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
	}
	
    @Test
    @WithMockUser
    public void testGetAddressEndpoint() throws Exception {
        Long addressId = testAddress.getAddressId();
        mockMvc.perform(get("/api/v1/addresses/{addressId}", addressId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.addressId", is(addressId.intValue())));
    }
    
    @Test
    @WithMockUser
    public void testCreateAddressEndpoint() throws Exception {
        Address address = new Address();

        mockMvc.perform(post("/api/v1/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(address)))
            .andExpect(status().isCreated());
    }
    
    @Test
    @WithMockUser
    public void testUpdateAddressEndpoint() throws Exception {
        Long addressId = testAddress.getAddressId();
        Address updatedAddress = new Address("Oak Street", "San Francisco", "California", "111111", "USA", new User());

        mockMvc.perform(put("/api/v1/addresses/{addressId}", addressId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAddress)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void testPartialUpdateAddressEndpoint() throws Exception {
        Long addressId = testAddress.getAddressId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("street", "Oak Street");
        fieldsToUpdate.put("city", "San Francisco");
        fieldsToUpdate.put("postalCode", "987654");

        mockMvc.perform(patch("/api/v1/addresses/{addressId}", addressId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsToUpdate)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void testDeleteAddressEndpoint() throws Exception {
    	Long addressId = testAddress.getAddressId();
    	mockMvc.perform(delete("/api/v1/addresses/{addressId}", addressId))
    		.andExpect(status().isOk());
    }
    
    @AfterEach
    public void cleanUp() {
    	addressRepo.delete(testAddress);
    }
}