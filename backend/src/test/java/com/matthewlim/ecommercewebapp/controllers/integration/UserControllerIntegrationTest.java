package com.matthewlim.ecommercewebapp.controllers.integration;

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
import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserRepository userRepo;
	
	private User testUser;
	
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
        
        testUser = new User("bobRoss", "ilovepainting", "bobRoss@gmail.com", "Bob", "Ross", new ArrayList<Order>(), new Address(), new Cart());
        testUser = userRepo.save(testUser);
    }
    
	@Test
	public void testGetUsersEndpoint() throws Exception {
		mockMvc.perform(get("/api/v1/users"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
	}
	
    @Test
    public void testGetUserEndpoint() throws Exception {
        Long userId = testUser.getUserId();
        mockMvc.perform(get("/api/v1/users/{userId}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId", is(userId.intValue())));
    }
    
    @Test
    public void testCreateUserEndpoint() throws Exception {
        User user = new User("johnWick", "yeah", "johnwick@gmail.com", "John", "Wick", new ArrayList<Order>(), new Address(), new Cart());

        mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated());
    }
    
    @Test
    public void testUpdateUserEndpoint() throws Exception {
        Long userId = testUser.getUserId();
        User updatedUser = new User("bobbyRoss", "ilovepainting", "bobRoss@gmail.com", "Bob", "Ross", new ArrayList<Order>(), new Address(), new Cart());

        mockMvc.perform(put("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testPartialUpdateUserEndpoint() throws Exception {
        Long userId = testUser.getUserId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("username", "billNye");
        fieldsToUpdate.put("password", "thescienceguy");
        fieldsToUpdate.put("email", "billnye@science.com");
        fieldsToUpdate.put("firstName", "Bill");
        fieldsToUpdate.put("lastName", "Nye");
        
        mockMvc.perform(patch("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsToUpdate)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testDeleteUserEndpoint() throws Exception {
    	Long userId = testUser.getUserId();
    	mockMvc.perform(delete("/api/v1/users/{userId}", userId))
    		.andExpect(status().isOk());
    }
    
    @AfterEach
    public void cleanUp() {
    	userRepo.delete(testUser);
    }
}
