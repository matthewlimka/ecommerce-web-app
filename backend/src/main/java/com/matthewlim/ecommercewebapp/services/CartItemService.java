package com.matthewlim.ecommercewebapp.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matthewlim.ecommercewebapp.exceptions.CartItemNotFoundException;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.CartItemRepository;

@Service
public class CartItemService {

	Logger logger = LogManager.getLogger(CartItemService.class);
	
	@Autowired
	private CartItemRepository cartItemRepo;
	
	public CartItem findByCartItemId(Long cartItemId) {
		CartItem cartItem = cartItemRepo.findById(cartItemId)
				.orElseThrow(() -> {
					logger.error("No cart item with cart item ID " + cartItemId + " found");
					return new CartItemNotFoundException("No cart item with cart item ID " + cartItemId + " found");
				});
		
		logger.info("Successfully found cart item with cart item ID " + cartItemId);
		return cartItem;
	}
	
	public List<CartItem> findByQuantity(int quantity) {
		List<CartItem> cartItems = cartItemRepo.findByQuantity(quantity);
		
		logger.info("Successfully found " + cartItems.size() + " cart item(s) with quantity " + quantity);
		return cartItems;
	}
	
	public List<CartItem> findByCart(Cart cart) {
		List<CartItem> cartItems = cartItemRepo.findByCart(cart);
		
		logger.info("Successfully found " + cartItems.size() + " cart item(s) with cart " + cart);
		return cartItems;
	}
	
	public List<CartItem> findByProduct(Product product) {
		List<CartItem> cartItems = cartItemRepo.findByProduct(product);
		
		logger.info("Successfully found " + cartItems.size() + " cart item(s) with stock quantity " + product);
		return cartItems;
	}
	
	public CartItem addCartItem(CartItem cartItem) {
		logger.info("Successfully registered new cart item with cart item ID " + cartItem.getCartItemId());
		return cartItemRepo.save(cartItem);
	}
	
	public CartItem updateCartItem(Long cartItemId, CartItem updatedCartItem) {
		CartItem existingCartItem = cartItemRepo.findById(cartItemId)
				.orElseThrow(() -> {
					logger.error("No cart item with cart item ID " + cartItemId + " found");
					return new CartItemNotFoundException("No cart item with cart item ID " + cartItemId + " found");
				});
		
		existingCartItem.setQuantity(updatedCartItem.getQuantity());
		existingCartItem.setCart(updatedCartItem.getCart());
		existingCartItem.setProduct(updatedCartItem.getProduct());
		
		logger.info("Successfully updated cart item with cart item ID " + cartItemId);
		
		return cartItemRepo.save(existingCartItem);
	}
	
	public void deleteCartItem(Long cartItemId) {
		CartItem existingCartItem = cartItemRepo.findById(cartItemId)
				.orElseThrow(() -> {
					logger.error("No cart item with cart item ID " + cartItemId + " found");
					return new CartItemNotFoundException("No cart item with cart item ID " + cartItemId + " found");
				});
		
		logger.info("Successfully delete cart item with cart item ID " + cartItemId);
		cartItemRepo.delete(existingCartItem);
	}
}
