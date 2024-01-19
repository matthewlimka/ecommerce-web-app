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
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.CartItemRepository;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;
import com.matthewlim.ecommercewebapp.repositories.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CartItemControllerIntegrationTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CartRepository cartRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private CartItemRepository cartItemRepo;
	
	private CartItem testCartItem;
	
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
        
        Cart cart = new Cart();
        cart = cartRepo.save(cart);
        Product product = new Product();
        product = productRepo.save(product);
        testCartItem = new CartItem(2, cart, product);
        testCartItem = cartItemRepo.save(testCartItem);
    }
    
	@Test
	@WithMockUser
	public void testGetCartItemsEndpoint() throws Exception {
		mockMvc.perform(get("/api/v1/cartItems"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
	}
	
    @Test
    @WithMockUser
    public void testGetCartItemEndpoint() throws Exception {
        Long cartItemId = testCartItem.getCartItemId();
        mockMvc.perform(get("/api/v1/cartItems/{cartItemId}", cartItemId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cartItemId", is(cartItemId.intValue())));
    }
    
    @Test
    @WithMockUser
    public void testCreateCartItemEndpoint() throws Exception {
        CartItem cartItem = new CartItem();

        mockMvc.perform(post("/api/v1/cartItems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cartItem)))
            .andExpect(status().isCreated());
    }
    
    @Test
    @WithMockUser
    public void testUpdateCartItemEndpoint() throws Exception {
        Long cartItemId = testCartItem.getCartItemId();
        CartItem updatedCartItem = new CartItem();

        mockMvc.perform(put("/api/v1/cartItems/{cartItemId}", cartItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCartItem)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void testPartialUpdateCartItemEndpoint() throws Exception {
        Long cartItemId = testCartItem.getCartItemId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("quantity", 5);
        
        mockMvc.perform(patch("/api/v1/cartItems/{cartItemId}", cartItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsToUpdate)))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void testDeleteCartItemEndpoint() throws Exception {
    	Long cartItemId = testCartItem.getCartItemId();
    	mockMvc.perform(delete("/api/v1/cartItems/{cartItemId}", cartItemId))
    		.andExpect(status().isOk());
    }
    
    @AfterEach
    public void cleanUp() {
    	cartItemRepo.delete(testCartItem);
    }
}