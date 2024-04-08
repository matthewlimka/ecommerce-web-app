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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIntegrationTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CartRepository cartRepo;
	
	private Cart testCart;
	
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
        
        User user = new User();
        user = userRepo.save(user);

        testCart = new Cart(user, null);
        testCart = cartRepo.save(testCart);
    }
    
	@Test
	@WithMockUser
	public void testGetCartsEndpoint() throws Exception {
		mockMvc.perform(get("/api/v1/carts"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
	}
	
    @Test
    @WithMockUser
    public void testGetCartEndpoint() throws Exception {
    	User user = new User();
    	user.setUsername("cartControllerTestUser");
    	user.setPassword("password");
    	userRepo.save(user);
    	Cart cart = new Cart(user, null);
    	cart = cartRepo.save(cart);
    	
        Long cartId = cart.getCartId();
        String jwtToken = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiY2FydENvbnRyb2xsZXJUZXN0VXNlciIsImV4cCI6MTcxMjU3Mzk4NiwiaWF0IjoxNzEyNTcwMzg2LCJzY29wZSI6IlVTRVIifQ.bKhYKw9kxkQjGCRDg5qyazMpci-tj8EeGEk5FKzJCFfqOH5cPocuP_ZsDpZyNAU1gsaN-sBzu5ld9mk8_VvMHDQ7dRFgIcdMgRbunLrD_pW9zeVy_88KpmWBtAnQHDUY-tl5C36SPOwIsTfeUIKwFe0rOyLFs6r0l6XPm16MuWockz2H3DkaLJWayZQnDXT7wmtyHa10ror_CA9X3NQO5RvQhVzdg7hCQOBYZKAHscOyC0yAmetWKslmaTf3OxMO-nqPr719lVxnHTaNZMcOKluJGHDidBLbaOpPZh3DLmsfDK7p9WZQlk8cJi-2x5IuLXLi4Rn0xRvy05guW9F5hg";
        
        mockMvc.perform(get("/api/v1/cart")
        	.header("Authorization", "Bearer " + jwtToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cartId", is(cartId.intValue())));
    }
    
    @Test
    @WithMockUser
    public void testCreateCartEndpoint() throws Exception {
        Cart cart = new Cart();

        mockMvc.perform(post("/api/v1/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cart)))
            .andExpect(status().isCreated());
    }
    
    @Test
    @WithMockUser
    public void testUpdateCartEndpoint() throws Exception {
        Long cartId = testCart.getCartId();
        testCart.setCartItems(new ArrayList<CartItem>());

        mockMvc.perform(put("/api/v1/cart/{cartId}", cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCart)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void testPartialUpdateCartEndpoint() throws Exception {
        Long cartId = testCart.getCartId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("cartItems", new ArrayList<CartItem>());
        
        mockMvc.perform(patch("/api/v1/cart/{cartId}", cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsToUpdate)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void testDeleteCartEndpoint() throws Exception {
    	Long cartId = testCart.getCartId();
    	mockMvc.perform(delete("/api/v1/cart/{cartId}", cartId))
    		.andExpect(status().isOk());
    }
    
    @AfterEach
    public void cleanUp() {
    	cartRepo.delete(testCart);
    }
}