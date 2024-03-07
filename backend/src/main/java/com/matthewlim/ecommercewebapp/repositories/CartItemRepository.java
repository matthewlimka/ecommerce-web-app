package com.matthewlim.ecommercewebapp.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByQuantity(int quantity);
	List<CartItem> findBySubtotal(BigDecimal subtotal);
	List<CartItem> findByCart(Cart cart);
	List<CartItem> findByProduct(Product product);
	@Transactional
	void deleteByCart(Cart cart);
}
