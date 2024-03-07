package com.matthewlim.ecommercewebapp.services;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.matthewlim.ecommercewebapp.exceptions.CartItemNotFoundException;
import com.matthewlim.ecommercewebapp.exceptions.CartNotFoundException;
import com.matthewlim.ecommercewebapp.exceptions.ProductNotFoundException;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.CartItemRepository;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;
import com.matthewlim.ecommercewebapp.repositories.ProductRepository;

@Service
public class CartService {

	Logger logger = LogManager.getLogger(CartService.class);
	
	@Autowired
	private CartRepository cartRepo;
	
	@Autowired
	private CartItemRepository cartItemRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	public Cart findByCartId(Long cartId) {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> {
					logger.error("No cart with cart ID " + cartId + " found");
					return new CartNotFoundException("No cart with cart ID " + cartId + " found");
				});
		
		logger.info("Successfully found cart with cart ID " + cartId);
		return cart;
	}
	
	public List<Cart> findByTotalAmount(BigDecimal totalAmount) {
		List<Cart> carts = cartRepo.findByTotalAmount(totalAmount);
		
		logger.info("Successfully found " + carts.size() + " cart(s) with total amount " + totalAmount);
		return carts;
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
	
	public Cart findByCartItems(CartItem cartItem) {
		Cart cart = cartRepo.findByCartItems(cartItem)
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
		
		existingCart.setTotalAmount(updatedCart.getTotalAmount());
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
			if (key.equals("cartItems")) {
				// Updating cartItems field
				logger.info("Updating cartItems field...");
				if (value instanceof List) {
					List<Map<String, Object>> cartItems = (List<Map<String, Object>>) value;
					List<CartItem> cartItemListToSave = new ArrayList<CartItem>();
					
					// Updating each cartItem in cartItems field
					for (Map<String, Object> cartItem : cartItems) {
						// Replacing productId in cartItem's product field with Product object
						// Find existing product from database
						Product product = null;
						if (cartItem.containsKey("product")) {
							logger.info("cartItems - product field found");
							Integer productId = (Integer) cartItem.get("product");
							product = productRepo.findById(productId.longValue())
									.orElseThrow(() -> {
										logger.error("No product with product ID " + cartItem.get("product") + " found");
										return new ProductNotFoundException("No product with product ID " + cartItem.get("product") + " found");
									});
						}
						
						// Updating cart item
						// Find existing cart item from database if any
						CartItem cartItemToSave = null;
						if (cartItem.containsKey("cartItemId")) {
							// Cart item ID provided
							// Update existing cart item
							logger.info("cartItems - cartItemId field found");
							Integer cartItemId = (Integer) cartItem.get("cartItemId");
							cartItemToSave = cartItemRepo.findById(cartItemId.longValue())
									.orElseThrow(() -> {
										logger.error("No cart item with cart item ID " + cartItem.get("cartItemId") + " found");
										return new CartItemNotFoundException("No cart item with cart item ID " + cartItem.get("cartItemId") + " found");
									});
							
							BigDecimal subtotal = BigDecimal.valueOf((Double) cartItem.get("subtotal"));
							subtotal.setScale(2, RoundingMode.HALF_UP);
							
							cartItemToSave.setCart(existingCart);
							cartItemToSave.setProduct(product);
							cartItemToSave.setQuantity((Integer) cartItem.get("quantity"));
							cartItemToSave.setSubtotal(subtotal);
						} else {
							// No cart item ID provided
							// Check if cart contains any existing cart items with the same product
							logger.info("cartItems - no cartItemId field found");
							List<CartItem> existingCartItems = cartItemRepo.findByCart(existingCart);
							boolean existingCartItemWithSameProduct = false;

							// Existing cart item with the same product
							for (CartItem existingCartItem : existingCartItems) {
								if (existingCartItem.getProduct().getProductId() == product.getProductId()) {
									// Add quantity and subtotal to existing cart item
									logger.info("Existing cart item with product id " + product.getProductId() + " found");
									cartItemToSave = existingCartItem;
									
									BigDecimal subtotal = BigDecimal.valueOf((Double) cartItem.get("subtotal"));
									subtotal.setScale(2, RoundingMode.HALF_UP);
									
									cartItemToSave.setQuantity(existingCartItem.getQuantity() + (Integer) cartItem.get("quantity"));
									cartItemToSave.setSubtotal(existingCartItem.getSubtotal().add(subtotal));
									existingCartItemWithSameProduct = true;
								}
							}
								
							// No existing cart item with the same product
							if (!existingCartItemWithSameProduct) {
								// Create new cart item for the product
								logger.info("No existing cart item with product id " + product.getProductId());
								BigDecimal subtotal = BigDecimal.valueOf((Double) cartItem.get("subtotal"));
								subtotal.setScale(2, RoundingMode.HALF_UP);
								
								cartItemToSave = new CartItem((Integer) cartItem.get("quantity"), subtotal, existingCart, product);
							}
						}
						// Add updated cart item to list of cart items to save
						cartItemListToSave.add(cartItemToSave);
					}
					// Update cart items field with updated cart items
					existingCart.setCartItems(cartItemListToSave);
				}	
			} else {
				// Updating non-cartItems fields
				logger.info("Updating non-cartItems fields...");
				Field field = ReflectionUtils.findField(Cart.class, key);
				field.setAccessible(true);
				
				if (field.getType() == BigDecimal.class && value instanceof Number) {
					value = new BigDecimal(((Number) value).doubleValue());
				}
				
				ReflectionUtils.setField(field, existingCart, value);
			}
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
