package com.matthewlim.ecommercewebapp.models;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@NoArgsConstructor
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	
	private String productName;
	private BigDecimal price;
	private int stockQuantity;
	
	public Product(String productName, BigDecimal price, int stockQuantity) {
		super();
		this.productName = productName;
		this.price = price;
		this.stockQuantity = stockQuantity;
	}
}
