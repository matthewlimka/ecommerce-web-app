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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthewlim.ecommercewebapp.controllers.CartItemController;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.services.CartItemService;
import com.matthewlim.ecommercewebapp.services.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CartItemController.class)
public class CartItemControllerUnitTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private JwtDecoder jwtDecoder;
	
	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private CartItemService cartItemService;
	
	private CartItem testCartItem;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
		
		testCartItem = new CartItem();
		testCartItem.setCartItemId(1L);
	}
	
	@Test
	@WithMockUser
	public void testGetCartItems() throws Exception {
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		when(cartItemService.findAllCartItems()).thenReturn(cartItemList);
		
		mockMvc.perform(get("/api/v1/cartItems"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
			.andExpect(jsonPath("$").isArray());
	}
	
	@Test
	@WithMockUser
	public void testGetCartItem() throws Exception {
		Long cartItemId = testCartItem.getCartItemId();
		when(cartItemService.findByCartItemId(cartItemId)).thenReturn(testCartItem);
		
		mockMvc.perform(get("/api/v1/cartItems/{cartItemId}", cartItemId))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.cartItemId", is(cartItemId.intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testCreateCartItem() throws Exception {
		CartItem cartItem = new CartItem(2, BigDecimal.valueOf(12.90), new Cart(), new Product());
		cartItem.setCartItemId(1L);
		when(cartItemService.addCartItem(testCartItem)).thenReturn(testCartItem);
		
		mockMvc.perform(post("/api/v1/cartItems")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(testCartItem)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.cartItemId", is(cartItem.getCartItemId().intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testUpdateCartItem() throws Exception {
        Long cartItemId = testCartItem.getCartItemId();
        CartItem updatedCartItem = new CartItem(2, BigDecimal.valueOf(12.90), new Cart(), new Product());
        when(cartItemService.updateCartItem(cartItemId, testCartItem)).thenReturn(updatedCartItem);
        
        mockMvc.perform(put("/api/v1/cartItems/{cartItemId}", cartItemId)
        	.with(csrf())
        	.contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedCartItem)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testPartialUpdateCartItem() throws Exception {
        Long cartItemId = testCartItem.getCartItemId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("quantity", 3);
        fieldsToUpdate.put("product", new Product());

        mockMvc.perform(patch("/api/v1/cartItems/{cartItemId}", cartItemId)
        	.with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(fieldsToUpdate)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testDeleteCartItem() throws Exception {
		Long cartItemId = testCartItem.getCartItemId();
		
		mockMvc.perform(delete("/api/v1/cartItems/{cartItemId}", cartItemId)
			.with(csrf()))
			.andExpect(status().isOk());
	}
}