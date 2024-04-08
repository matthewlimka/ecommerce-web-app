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
import com.matthewlim.ecommercewebapp.enums.Role;
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
        
        testUser = new User("bobRoss", "ilovepainting", "bobRoss@gmail.com", "Bob", "Ross", Role.USER);
        testUser = userRepo.save(testUser);
    }
    
	@Test
	@WithMockUser
	public void testGetUsersEndpoint() throws Exception {
		mockMvc.perform(get("/api/v1/users"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
	}
	
    @Test
    @WithMockUser
    public void testGetUserEndpoint() throws Exception {
    	User user = new User();
    	user.setUsername("userControllerTestUser");
    	user.setPassword("password");
    	user = userRepo.save(user);
        Long userId = user.getUserId();
        String jwtToken = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoidXNlckNvbnRyb2xsZXJUZXN0VXNlciIsImV4cCI6MTcxMjU3MzkzMCwiaWF0IjoxNzEyNTcwMzMwLCJzY29wZSI6IlVTRVIifQ.mbbg4LuUXOiT1O6In_9Nr4v6n10s7GiPHymKvPZIKRO36sdman-TszHX9tU1ZDI8aqxebDCvTTeuM8I3NPQlHXQp7jKt1u_Aq0OFatnU68FPLVbwAHlIt0MgPLrIhsM7kH0GDEuKX0y9Etyjws-TsK1K96EB0ilGjwZbvhklhsDqZSN8oc1bUsx8OVXbiOka7LDd8PvYdYydYy85xvVqhX26aIA3FAGAEZKBwKy27cHjuRVDjSmQ_5MMDiUcHWlCGfbZsp0P6XEUt__T-4kbF_zQJxu-vsV3XUiFxfExyo-SazCe6Q9zfgLF6nIV8_rC4PFD7Fg-2Fn6sOqzV83hoA";
        
        mockMvc.perform(get("/api/v1/user")
        	.header("Authorization", "Bearer " + jwtToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId", is(userId.intValue())));
    }
    
    @Test
    @WithMockUser
    public void testCreateUserEndpoint() throws Exception {
    	User user = new User("johnWick", "yeah", "johnwick@gmail.com", "John", "Wick", Role.USER);

        mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated());
    }
    
    @Test
    @WithMockUser
    public void testUpdateUserEndpoint() throws Exception {
        Long userId = testUser.getUserId();
        User updatedUser = new User("bobbyRoss", "ilovepainting", "bobRoss@gmail.com", "Bob", "Ross", Role.USER);

        mockMvc.perform(put("/api/v1/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void testPartialUpdateUserEndpoint() throws Exception {
        Long userId = testUser.getUserId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("username", "billNye");
        fieldsToUpdate.put("password", "thescienceguy");
        fieldsToUpdate.put("email", "billnye@science.com");
        fieldsToUpdate.put("firstName", "Bill");
        fieldsToUpdate.put("lastName", "Nye");
        
        mockMvc.perform(patch("/api/v1/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsToUpdate)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void testDeleteUserEndpoint() throws Exception {
    	Long userId = testUser.getUserId();
    	mockMvc.perform(delete("/api/v1/user/{userId}", userId))
    		.andExpect(status().isOk());
    }
    
    @AfterEach
    public void cleanUp() {
    	userRepo.delete(testUser);
    }
}