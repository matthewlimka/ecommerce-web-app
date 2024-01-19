package com.matthewlim.ecommercewebapp.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.ProductRepository;
import com.matthewlim.ecommercewebapp.services.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private ProductService productService;
	
	@Test
	public void testFindAllProducts() {
		List<Product> productList = new ArrayList<Product>(Arrays.asList(new Product(), new Product()));		
		when(productRepository.findAll()).thenReturn(productList);
		
		List<Product> result = productService.findAllProducts();
		
		assertNotNull(result);
		assertThat(result).hasSize(productList.size()).containsExactlyElementsOf(productList);
	}
	
	@Test
	public void testFindByProductId() {
		Long productId = 1L;
		Product product = new Product();
		product.setProductId(productId);
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		
		Product result = productService.findByProductId(productId);
		
		assertNotNull(result);
		assertEquals(productId, result.getProductId());
	}
	
	@Test
	public void testFindByProductName() {
		String productName = "productName";
		Product product = new Product();
		product.setProductName(productName);
		List<Product> productList = new ArrayList<Product>(Arrays.asList(product));
		
		when(productRepository.findByProductName(productName)).thenReturn(productList);
		List<Product> result = productService.findByProductName(productName);
		
		assertNotNull(result);
		assertThat(result).hasSize(productList.size()).containsExactlyElementsOf(productList);
	}
	
	@Test
	public void testFindByPrice() {
		BigDecimal price = BigDecimal.valueOf(123.10);
		Product product = new Product();
		product.setPrice(price);
		List<Product> productList = new ArrayList<Product>(Arrays.asList(product));
		
		when(productRepository.findByPrice(price)).thenReturn(productList);
		List<Product> result = productService.findByPrice(price);
		
		assertNotNull(result);
		assertThat(result).hasSize(productList.size()).containsExactlyElementsOf(productList);
	}
	
	@Test
	public void testFindByStockQuantity() {
		int stockQuantity = 510;
		Product product = new Product();
		product.setStockQuantity(stockQuantity);
		List<Product> productList = new ArrayList<Product>(Arrays.asList(product));
		
		when(productRepository.findByStockQuantity(stockQuantity)).thenReturn(productList);
		List<Product> result = productService.findByStockQuantity(stockQuantity);
		
		assertNotNull(result);
		assertThat(result).hasSize(productList.size()).containsExactlyElementsOf(productList);
	}
	
	@Test
	public void testAddProduct() {
		Product product = new Product();
		when(productRepository.save(product)).thenReturn(product);
		
		Product result = productService.addProduct(product);
		
		assertNotNull(result);
		verify(productRepository, times(1)).save(product);
	}
	
	@Test
	public void testUpdateProduct() {
		Long productId = 1L;
		Product existingProduct = new Product();
		existingProduct.setProductId(productId);
		
		Product updatedProduct = new Product();
		updatedProduct.setProductName("updatedProductName");
		updatedProduct.setStockQuantity(258);
		
		when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
		when(productRepository.save(existingProduct)).thenReturn(updatedProduct);
		
		Product result = productService.updateProduct(productId, updatedProduct);
		
		assertNotNull(result);
		assertEquals(updatedProduct.getProductName(), result.getProductName());
		assertEquals(updatedProduct.getStockQuantity(), result.getStockQuantity());
	}
	
	@Test
	public void testPartialUpdateProduct() {
		Long productId = 1L;
		Product existingProduct = new Product();
		existingProduct.setProductId(productId);
		
		Map<String, Object> fieldsToUpdate = new HashMap<String, Object>();
		fieldsToUpdate.put("productName", "updatedProductName");
		fieldsToUpdate.put("stockQuantity", 125);
		
		when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
		when(productRepository.save(existingProduct)).thenReturn(existingProduct);
		
		Product result = productService.partialUpdateProduct(productId, fieldsToUpdate);
		
		assertNotNull(result);
		assertEquals(fieldsToUpdate.get("productName"), result.getProductName());
		assertEquals(fieldsToUpdate.get("stockQuantity"), result.getStockQuantity());
	}
	
	@Test
	public void testDeleteProduct() {
		Long productId = 1L;
		Product product = new Product();
		product.setProductId(productId);
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		
		productService.deleteProduct(productId);
		
		verify(productRepository, times(1)).delete(product);
	}
}