package com.matthewlim.ecommercewebapp.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matthewlim.ecommercewebapp.exceptions.ProductNotFoundException;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.ProductRepository;

@Service
public class ProductService {

	Logger logger = LogManager.getLogger(ProductService.class);
	
	@Autowired
	private ProductRepository productRepo;
	
	public Product findByProductId(Long productId) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> {
					logger.error("No product with product ID " + productId + " found");
					return new ProductNotFoundException("No product with product ID " + productId + " found");
				});
		
		logger.info("Successfully found product with product ID " + productId);
		return product;
	}
	
	public List<Product> findByProductName(String productName) {
		List<Product> products = productRepo.findByProductName(productName);
		
		logger.info("Successfully found " + products.size() + " product(s) with product name " + productName);
		return products;
	}
	
	public List<Product> findByPrice(BigDecimal price) {
		List<Product> products = productRepo.findByPrice(price);
		
		logger.info("Successfully found " + products.size() + " product(s) with price " + price);
		return products;
	}
	
	public List<Product> findByStockQuantity(int stockQuantity) {
		List<Product> products = productRepo.findByStockQuantity(stockQuantity);
		
		logger.info("Successfully found " + products.size() + " product(s) with stock quantity " + stockQuantity);
		return products;
	}
	
	public Product addProduct(Product product) {
		logger.info("Successfully registered new product with product ID " + product.getProductId());
		return productRepo.save(product);
	}
	
	public Product updateProduct(Long productId, Product updatedProduct) {
		Product existingProduct = productRepo.findById(productId)
				.orElseThrow(() -> {
					logger.error("No product with product ID " + productId + " found");
					return new ProductNotFoundException("No product with product ID " + productId + " found");
				});
		
		existingProduct.setProductName(updatedProduct.getProductName());
		existingProduct.setPrice(updatedProduct.getPrice());
		existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
		
		logger.info("Successfully updated product with product ID " + productId);
		
		return productRepo.save(existingProduct);
	}
	
	public void deleteProduct(Long productId) {
		Product existingProduct = productRepo.findById(productId)
				.orElseThrow(() -> {
					logger.error("No product with product ID " + productId + " found");
					return new ProductNotFoundException("No product with product ID " + productId + " found");
				});
		
		logger.info("Successfully delete product with product ID " + productId);
		productRepo.delete(existingProduct);
	}
}
