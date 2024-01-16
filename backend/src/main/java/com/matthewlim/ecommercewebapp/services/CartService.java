package com.matthewlim.ecommercewebapp.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.matthewlim.ecommercewebapp.exceptions.CartNotFoundException;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;

@Service
public class CartService {

	Logger logger = LogManager.getLogger(CartService.class);
	
	@Autowired
	private CartRepository cartRepo;
	
	public Cart findByCartId(Long cartId) {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> {
					logger.error("No cart with cart ID " + cartId + " found");
					return new CartNotFoundException("No cart with cart ID " + cartId + " found");
				});
		
		logger.info("Successfully found cart with cart ID " + cartId);
		return cart;
	}
	
	public Cart findByUser(User user) {
		Cart cart = cartRepo.findByUser(user)
				.orElseThrow(() -> {
					logger.error("No cart with user ID " + user.getUserId() + " found");
					return new CartNotFoundException("No cart with user ID " + user.getUserId() + " found");
				});
		
		logger.info("Successfully found cart with user ID " + user.getUserId());
		return cart;
	}
	
	public Cart findByCartItem(CartItem cartItem) {
		Cart cart = cartRepo.findByCartItem(cartItem)
				.orElseThrow(() -> {
					logger.error("No cart with cart item ID " + cartItem.getCartItemId() + " found");
					return new CartNotFoundException("No cart with cart item ID " + cartItem.getCartItemId() + " found");
				});
		
		logger.info("Successfully found cart with cart item ID " + cartItem.getCartItemId());
		return cart;
	}
	
	public List<Cart> findAllCarts() {
		List<Cart> cartList = cartRepo.findAll();
		
		logger.info("Successfully found " + cartList.size() + " carts");
		return cartList;
	}
	
	public Cart addCart(Cart cart) {
		logger.info("Successfully registered new cart with cart ID " + cart.getCartId());
		return cartRepo.save(cart);
	}
	
	public Cart updateCart(Long cartId, Cart updatedCart) {
		Cart existingCart = cartRepo.findById(cartId)
				.orElseThrow(() -> {
					logger.error("No cart with cart ID " + cartId + " found");
					return new CartNotFoundException("No cart with cart ID " + cartId + " found");
				});
		
		existingCart.setUser(updatedCart.getUser());
		existingCart.setCartItems(updatedCart.getCartItems());
		
		logger.info("Successfully updated cart with cart ID " + cartId);
		return cartRepo.save(existingCart);
	}
	
	public Cart partialUpdateCart(Long cartId, Map<String, Object> fields) {
		Cart existingCart = cartRepo.findById(cartId)
				.orElseThrow(() -> {
					logger.error("No cart with cart ID " + cartId + " found");
					return new CartNotFoundException("No cart with cart ID " + cartId + " found");
				});
		
		fields.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(Cart.class, key);
			field.setAccessible(true);
			ReflectionUtils.setField(field, existingCart, value);
		});
		
		logger.info("Successfully updated cart with cart ID " + cartId);
		return cartRepo.save(existingCart);
	}
	
	public void deleteCart(Long cartId) {
		Cart existingCart = cartRepo.findById(cartId)
				.orElseThrow(() -> {
					logger.error("No cart with cart ID " + cartId + " found");
					return new CartNotFoundException("No cart with cart ID " + cartId + " found");
				});
		
		logger.info("Successfully deleted cart with cart ID " + cartId);
		cartRepo.delete(existingCart);
	}
}
