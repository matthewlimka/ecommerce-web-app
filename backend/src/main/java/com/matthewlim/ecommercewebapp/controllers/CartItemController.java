package com.matthewlim.ecommercewebapp.controllers;

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

import com.matthewlim.ecommercewebapp.exceptions.CartItemNotFoundException;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.services.CartItemService;

@RestController
@RequestMapping("api/v1/cartItems")
public class CartItemController {

	@Autowired
	private CartItemService cartItemService;

	@GetMapping
	public List<CartItem> getCartItems(@RequestParam(required = false) Integer quantity, @RequestParam(required = false) Cart cart, @RequestParam(required = false) Product product) {
		if ( quantity != null ) {
			return cartItemService.findByQuantity(quantity);
		} else if ( cart != null ) {
			return cartItemService.findByCart(cart);
		} else if ( product != null ) {
			return cartItemService.findByProduct(product);
		} else {
			return cartItemService.findAllCartItems();	
		}
	}

	@GetMapping("/{cartItemId}")
	public CartItem getCartItem(@PathVariable Long cartItemId) throws CartItemNotFoundException {
		return cartItemService.findByCartItemId(cartItemId);
	}
	
	@PostMapping
	public ResponseEntity<CartItem> createCartItem(@RequestBody CartItem cartItem) {
		CartItem savedCartItem = cartItemService.addCartItem(cartItem);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(cartItem.getCartItemId())
				.toUri();
		
		return ResponseEntity.created(location).body(savedCartItem);
	}
	
	@PutMapping("/{cartItemId}")
	public CartItem updateCartItem(@PathVariable Long cartItemId, @RequestBody CartItem updatedCartItem) {
		return cartItemService.updateCartItem(cartItemId, updatedCartItem);
	}
	
	@PatchMapping("/{cartItemId}")
	public CartItem partialUpdateCartItem(@PathVariable Long cartItemId, @RequestBody Map<String, Object> fields) {
		return cartItemService.partialUpdateCartItem(cartItemId, fields);
	}
	
	@DeleteMapping("/{cartItemId}")
	public void deleteCartItem(@PathVariable Long cartItemId) {
		cartItemService.deleteCartItem(cartItemId);
	}
}
