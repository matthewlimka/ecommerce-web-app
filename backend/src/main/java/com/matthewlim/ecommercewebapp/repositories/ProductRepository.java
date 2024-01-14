package com.matthewlim.ecommercewebapp.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matthewlim.ecommercewebapp.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByProductName(String productName);
	List<Product> findByPrice(BigDecimal price);
	List<Product> findByStockQuantity(int stockQuantity);
}
