package com.matthewlim.ecommercewebapp.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Product;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	List<OrderItem> findByQuantity(int quantity);
	List<OrderItem> findBySubtotal(BigDecimal subtotal);
	List<OrderItem> findByOrder(Order order);
	List<OrderItem> findByProduct(Product product);
}
