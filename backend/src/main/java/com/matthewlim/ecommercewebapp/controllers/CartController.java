package com.matthewlim.ecommercewebapp.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.matthewlim.ecommercewebapp.exceptions.CartNotFoundException;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.services.CartService;
import com.matthewlim.ecommercewebapp.services.UserService;

@RestController
@RequestMapping("api/v1")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/carts")
	public List<Cart> getCarts(@RequestParam(required = false) User user, @RequestParam(required = false) CartItem cartItem) {
		if ( user!= null ) {
			return new ArrayList<Cart>(Arrays.asList(cartService.findByUser(user)));
		} else if ( cartItem != null ) {
			return new ArrayList<Cart>(Arrays.asList(cartService.findByCartItems(cartItem)));
		} else {
			return cartService.findAllCarts();	
		}
	}

	@GetMapping("/cart")
	public Cart getCart() throws CartNotFoundException, RuntimeException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		return cartService.findByUser(userService.findByUsername(username));
	}
	
	@PostMapping("/carts")
	public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
		Cart savedCart = cartService.addCart(cart);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(cart.getCartId())
				.toUri();
		
		return ResponseEntity.created(location).body(savedCart);
	}
	
	@PutMapping("/cart/{cartId}")
	public Cart updateCart(@PathVariable Long cartId, @RequestBody Cart updatedCart) {
		return cartService.updateCart(cartId, updatedCart);
	}
	
	@PatchMapping("/cart/{cartId}")
	public Cart partialUpdateCart(@PathVariable Long cartId, @RequestBody Map<String, Object> fields) {
		return cartService.partialUpdateCart(cartId, fields);
	}
	
	@DeleteMapping("/cart/{cartId}")
	public void deleteCart(@PathVariable Long cartId) {
		cartService.deleteCart(cartId);
	}
}
