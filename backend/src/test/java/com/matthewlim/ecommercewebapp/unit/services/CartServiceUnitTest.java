package com.matthewlim.ecommercewebapp.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;
import com.matthewlim.ecommercewebapp.services.CartService;

@ExtendWith(MockitoExtension.class)
public class CartServiceUnitTest {

	@Mock
	private CartRepository cartRepository;
	
	@InjectMocks
	private CartService cartService;
	
	@Test
	public void testFindAllCarts() {
		List<Cart> cartList = new ArrayList<Cart>(Arrays.asList(new Cart(), new Cart()));
		
		when(cartRepository.findAll()).thenReturn(cartList);
		List<Cart> result = cartService.findAllCarts();
		
		assertNotNull(result);
		assertThat(result).hasSize(cartList.size()).containsExactlyElementsOf(cartList);
	}
	
	@Test
	public void testFindByCartId() {
		Long cartId = 1L;
		Cart cart = new Cart();
		cart.setCartId(cartId);
		
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
		Cart result = cartService.findByCartId(cartId);
		
		assertNotNull(result);
		assertEquals(cartId, result.getCartId());
	}
	
	@Test
	public void testFindByUser() {
		User user = new User();
		Cart cart = new Cart();
		cart.setUser(user);

		when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
		Cart result = cartService.findByUser(user);
		
		assertNotNull(result);
		assertEquals(user, result.getUser());
	}
	
	@Test
	public void testFindByCartItems() {
		CartItem cartItem = new CartItem();
		List<CartItem> cartItemList = new ArrayList<CartItem>(Arrays.asList(cartItem));
		Cart cart = new Cart();
		cart.setCartItems(cartItemList);
		
		when(cartRepository.findByCartItems(cartItem)).thenReturn(Optional.of(cart));
		Cart result = cartService.findByCartItems(cartItem);
		
		assertNotNull(result);
		assertEquals(cartItemList, result.getCartItems());
	}
	
	@Test
	public void testAddCart() {
		Cart cart = new Cart();
		
		when(cartRepository.save(cart)).thenReturn(cart);
		Cart result = cartService.addCart(cart);
		
		assertNotNull(result);
		verify(cartRepository, times(1)).save(cart);
	}
	
	@Test
	public void testUpdateCart() {
		Long cartId = 1L;
		Cart existingCart = new Cart();
		existingCart.setCartId(cartId);
		
		Cart updatedCart = new Cart();
		updatedCart.setUser(new User());
		
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
		when(cartRepository.save(existingCart)).thenReturn(updatedCart);
		Cart result = cartService.updateCart(cartId, updatedCart);
		
		assertNotNull(result);
		assertEquals(updatedCart.getUser(), result.getUser());
	}
	
	@Test
	public void testPartialUpdateCart() {
		Long cartId = 1L;
		Cart existingCart = new Cart();
		existingCart.setCartId(cartId);
		
		Map<String, Object> fieldsToUpdate = new HashMap<String, Object>();
		fieldsToUpdate.put("cartItems", new ArrayList<CartItem>());
		
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
		when(cartRepository.save(existingCart)).thenReturn(existingCart);
		Cart result = cartService.partialUpdateCart(cartId, fieldsToUpdate);
		
		assertNotNull(result);
		assertEquals(fieldsToUpdate.get("cartItems"), result.getCartItems());
	}
	
	@Test
	public void testDeleteCart() {
		Long cartId = 1L;
		Cart cart = new Cart();
		cart.setCartId(cartId);
		
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
		cartService.deleteCart(cartId);
		
		verify(cartRepository, times(1)).delete(cart);
	}
}