package com.matthewlim.ecommercewebapp.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matthewlim.ecommercewebapp.exceptions.ProductNotFoundException;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.services.ProductService;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	@GetMapping
	public List<Product> getProducts(@RequestParam(required = false) String productName, @RequestParam(required = false) BigDecimal price, @RequestParam(required = false) Integer stockQuantity) {
		if ( productName != null ) {
			return productService.findByProductName(productName);
		} else if ( price != null ) {
			return productService.findByPrice(price);
		} else if ( stockQuantity != null ) {
			return productService.findByStockQuantity(stockQuantity);
		} else {
			return productService.findAllProducts();	
		}
	}

	@GetMapping("/{productId}")
	public Product getProduct(@PathVariable Long productId) throws ProductNotFoundException {
		return productService.findByProductId(productId);
	}
	
	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		Product savedProduct = productService.addProduct(product);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(product.getProductId())
				.toUri();
		
		return ResponseEntity.created(location).body(savedProduct);
	}
	
	@PutMapping("/{productId}")
	public Product updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
		return productService.updateProduct(productId, updatedProduct);
	}
	
	@PatchMapping("/{productId}")
	public Product partialUpdateProduct(@PathVariable Long productId, @RequestBody Map<String, Object> fields) {
		return productService.partialUpdateProduct(productId, fields);
	}
	
	@DeleteMapping("/{productId}")
	public void deleteProduct(@PathVariable Long productId) {
		productService.deleteProduct(productId);
	}
}
