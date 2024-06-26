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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;
import com.matthewlim.ecommercewebapp.services.TokenService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
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
    	user.setUsername("testGetCartEndpointTestUser");
    	user.setPassword("password");
    	user = userRepo.save(user);
    	
    	Cart cart = new Cart(user, null);
    	cart = cartRepo.save(cart);
    	
    	// Placing JWT in request header as controller method's logic checks for the authenticated user
        Long cartId = cart.getCartId();
        TokenService tokenService = context.getBean(TokenService.class);
        String jwtToken = tokenService.generateToken(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        
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