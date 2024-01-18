package com.matthewlim.ecommercewebapp.controllers.unit;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthewlim.ecommercewebapp.controllers.ProductController;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.services.ProductService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerUnitTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ProductService productService;
	
	private Product testProduct;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
		
		testProduct = new Product();
		testProduct.setProductId(1L);
	}
	
	@Test
	@WithMockUser
	public void testGetProducts() throws Exception {
		List<Product> productList = new ArrayList<Product>();
		when(productService.findAllProducts()).thenReturn(productList);
		
		mockMvc.perform(get("/api/v1/products"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
			.andExpect(jsonPath("$").isArray());
	}
	
	@Test
	@WithMockUser
	public void testGetProduct() throws Exception {
		Long productId = testProduct.getProductId();
		when(productService.findByProductId(productId)).thenReturn(testProduct);
		
		mockMvc.perform(get("/api/v1/products/{productId}", productId))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.productId", is(productId.intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testCreateProduct() throws Exception {
		Product product = new Product("USB-C to USB-C cable (1m)", BigDecimal.valueOf(5.0), 448);
		product.setProductId(1L);
		when(productService.addProduct(testProduct)).thenReturn(testProduct);
		
		mockMvc.perform(post("/api/v1/products")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(testProduct)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.productId", is(product.getProductId().intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testUpdateProduct() throws Exception {
        Long productId = testProduct.getProductId();
        Product updatedProduct = new Product("Microfiber Towel", BigDecimal.valueOf(3.50), 750);
        when(productService.updateProduct(productId, testProduct)).thenReturn(updatedProduct);
        
        mockMvc.perform(put("/api/v1/products/{productId}", productId)
        	.with(csrf())
        	.contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedProduct)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testPartialUpdateProduct() throws Exception {
        Long productId = testProduct.getProductId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("productName", "200ml Hand Sanitizer");
        fieldsToUpdate.put("price", BigDecimal.valueOf(12.50));

        mockMvc.perform(patch("/api/v1/products/{productId}", productId)
        	.with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(fieldsToUpdate)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testDeleteProduct() throws Exception {
		Long productId = testProduct.getProductId();
		
		mockMvc.perform(delete("/api/v1/products/{productId}", productId)
			.with(csrf()))
			.andExpect(status().isOk());
	}
}