package com.matthewlim.ecommercewebapp.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	List<Cart> findByTotalAmount(BigDecimal totalAmount);
	Optional<Cart> findByUser(User user);
	Optional<Cart> findByCartItems(CartItem cartItem);
}
