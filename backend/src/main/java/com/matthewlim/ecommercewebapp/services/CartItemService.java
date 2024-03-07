package com.matthewlim.ecommercewebapp.services;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.matthewlim.ecommercewebapp.exceptions.CartItemNotFoundException;
import com.matthewlim.ecommercewebapp.exceptions.CartNotFoundException;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.CartItemRepository;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;

@Service
public class CartItemService {

	Logger logger = LogManager.getLogger(CartItemService.class);
	
	@Autowired
	private CartItemRepository cartItemRepo;
	
	@Autowired
	private CartRepository cartRepo;
	
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
	
	public List<CartItem> findBySubtotal(BigDecimal subtotal) {
		List<CartItem> cartItems = cartItemRepo.findBySubtotal(subtotal);
		
		logger.info("Successfully found " + cartItems.size() + " cart item(s) with subtotal " + subtotal);
		return cartItems;
	}
	
	public List<CartItem> findByCart(Cart cart) {
		List<CartItem> cartItems = cartItemRepo.findByCart(cart);
		
		logger.info("Successfully found " + cartItems.size() + " cart item(s) with cart " + cart);
		return cartItems;
	}
	
	public List<CartItem> findByProduct(Product product) {
		List<CartItem> cartItems = cartItemRepo.findByProduct(product);
		
		logger.info("Successfully found " + cartItems.size() + " cart item(s) with product " + product);
		return cartItems;
	}
	
	public List<CartItem> findAllCartItems() {
		List<CartItem> cartItemList = cartItemRepo.findAll();
		
		logger.info("Successfully found " + cartItemList.size() + " cart items");
		return cartItemList;
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
		existingCartItem.setSubtotal(updatedCartItem.getSubtotal());
		existingCartItem.setCart(updatedCartItem.getCart());
		existingCartItem.setProduct(updatedCartItem.getProduct());
		
		logger.info("Successfully updated cart item with cart item ID " + cartItemId);
		return cartItemRepo.save(existingCartItem);
	}
	
	public CartItem partialUpdateCartItem(Long cartItemId, Map<String, Object> fields) {
		CartItem existingCartItem = cartItemRepo.findById(cartItemId)
				.orElseThrow(() -> {
					logger.error("No cart item with cart item ID " + cartItemId + " found");
					return new CartItemNotFoundException("No cart item with cart item ID " + cartItemId + " found");
				});
		
		// Retrieve cart if there is a change in cart item quantity
		Cart existingCart = null; 
		if (fields.containsKey("quantity")) {
			existingCart = cartRepo.findByCartItems(existingCartItem)
				.orElseThrow(() -> {
					logger.error("No cart with cart item ID " + cartItemId + " found");
					return new CartNotFoundException("No cart with cart item ID " + cartItemId + " found");
				});
		}
		
		fields.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(CartItem.class, key);
			field.setAccessible(true);
			
			if (field.getType() == BigDecimal.class && value instanceof Number) {
				value = new BigDecimal(((Number) value).doubleValue());
			}
			
			// Calculate and updated subtotal based on quantity if not provided 
			if (key.equals("quantity") && !fields.containsKey("subtotal")) {
				BigDecimal subtotal = BigDecimal.valueOf(((Integer) value).doubleValue() * existingCartItem.getProduct().getPrice().doubleValue());
				subtotal.setScale(2, RoundingMode.HALF_UP);
				existingCartItem.setSubtotal(subtotal);
			}
			
			ReflectionUtils.setField(field, existingCartItem, value);
		});
		
		// Calculate and update cart total amount if there is a change in cart item quantity
		if (fields.containsKey("quantity")) {
			BigDecimal updatedTotalAmount = BigDecimal.valueOf(0);
			updatedTotalAmount.setScale(2, RoundingMode.HALF_UP);
			
			for (CartItem cartItem : existingCart.getCartItems()) {
				updatedTotalAmount = updatedTotalAmount.add(cartItem.getSubtotal());
			}
			existingCart.setTotalAmount(updatedTotalAmount);
			logger.info("Updated total amount for cart ID " + existingCart.getCartId());
			cartRepo.save(existingCart);
		}
		
		logger.info("Successfully updated cart item with cart item ID " + cartItemId);
		return cartItemRepo.save(existingCartItem);
	}
	
	public void deleteCartItem(Long cartItemId) {
		CartItem existingCartItem = cartItemRepo.findById(cartItemId)
				.orElseThrow(() -> {
					logger.error("No cart item with cart item ID " + cartItemId + " found");
					return new CartItemNotFoundException("No cart item with cart item ID " + cartItemId + " found");
				});
		
		logger.info("Successfully deleted cart item with cart item ID " + cartItemId);
		cartItemRepo.delete(existingCartItem);
	}
	
	public void deleteCartItemsByCart(Cart cart) {
		logger.info("Successfully deleted cart items with cart ID " + cart.getCartId());
		cartItemRepo.deleteByCart(cart);
	}
}
