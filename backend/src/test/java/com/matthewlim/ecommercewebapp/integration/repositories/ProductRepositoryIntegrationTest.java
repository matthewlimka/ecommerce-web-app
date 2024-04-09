package com.matthewlim.ecommercewebapp.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductRepositoryIntegrationTest {

	@Autowired
	private ProductRepository productRepository;
	
    @Test
    public void testFindByProductName() {
    	String productName = "productName";
        Product product = new Product();
        product.setProductName(productName);
        productRepository.save(product);

        List<Product> result = productRepository.findByProductName(productName);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }
    
    @Test
    public void testFindByPrice() {
    	BigDecimal price = BigDecimal.valueOf(12.31);
        Product product = new Product();
        product.setPrice(price);
        productRepository.save(product);

        List<Product> result = productRepository.findByPrice(price);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }
    
    @Test
    public void testFindByStockQuantity() {
    	int stockQuantity = 5;
        Product product = new Product();
        product.setStockQuantity(stockQuantity);
        productRepository.save(product);

        List<Product> result = productRepository.findByStockQuantity(stockQuantity);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }
}