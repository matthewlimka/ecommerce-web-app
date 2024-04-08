package com.matthewlim.ecommercewebapp.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.CartItemRepository;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;
import com.matthewlim.ecommercewebapp.services.CartItemService;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceUnitTest {

	@Mock
	private CartItemRepository cartItemRepository;
	
	@Mock
	private CartRepository cartRepository;
	
	@InjectMocks
	private CartItemService cartItemService;
	
	@Test
	public void testFindAllCartItems() {
		List<CartItem> cartItemList = new ArrayList<CartItem>(Arrays.asList(new CartItem(), new CartItem()));		
		
		when(cartItemRepository.findAll()).thenReturn(cartItemList);
		List<CartItem> result = cartItemService.findAllCartItems();
		
		assertNotNull(result);
		assertThat(result).hasSize(cartItemList.size()).containsExactlyElementsOf(cartItemList);
	}
	
	@Test
	public void testFindByCartItemId() {
		Long cartItemId = 1L;
		CartItem cartItem = new CartItem();
		cartItem.setCartItemId(cartItemId);
		
		when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));
		CartItem result = cartItemService.findByCartItemId(cartItemId);
		
		assertNotNull(result);
		assertEquals(cartItemId, result.getCartItemId());
	}
	
	@Test
	public void testFindByQuantity() {
		int quantity = 2;
		CartItem cartItem = new CartItem();
		cartItem.setQuantity(quantity);
		List<CartItem> cartItemList = new ArrayList<CartItem>(Arrays.asList(cartItem));
		
		when(cartItemRepository.findByQuantity(quantity)).thenReturn(cartItemList);
		List<CartItem> result = cartItemService.findByQuantity(quantity);
		
		assertNotNull(result);
		assertThat(result).hasSize(cartItemList.size()).containsExactlyElementsOf(cartItemList);
	}
	
	@Test
	public void testFindByCart() {
		Cart cart = new Cart();
		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		List<CartItem> cartItemList = new ArrayList<CartItem>(Arrays.asList(cartItem));

		when(cartItemRepository.findByCart(cart)).thenReturn(cartItemList);
		List<CartItem> result = cartItemService.findByCart(cart);
		
		assertNotNull(result);
		assertThat(result).hasSize(cartItemList.size()).containsExactlyElementsOf(cartItemList);
	}
	
	@Test
	public void testFindByProduct() {
		Product product = new Product();
		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);
		List<CartItem> cartItemList = new ArrayList<CartItem>(Arrays.asList(cartItem));

		when(cartItemRepository.findByProduct(product)).thenReturn(cartItemList);
		List<CartItem> result = cartItemService.findByProduct(product);
		
		assertNotNull(result);
		assertThat(result).hasSize(cartItemList.size()).containsExactlyElementsOf(cartItemList);
	}
	
	@Test
	public void testAddCartItem() {
		CartItem cartItem = new CartItem();
		
		when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
		CartItem result = cartItemService.addCartItem(cartItem);
		
		assertNotNull(result);
		verify(cartItemRepository, times(1)).save(cartItem);
	}
	
	@Test
	public void testUpdateCartItem() {
		Long cartItemId = 1L;
		CartItem existingCartItem = new CartItem();
		existingCartItem.setCartItemId(cartItemId);
		
		CartItem updatedCartItem = new CartItem();
		updatedCartItem.setQuantity(5);
		
		when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(existingCartItem));
		when(cartItemRepository.save(existingCartItem)).thenReturn(updatedCartItem);
		CartItem result = cartItemService.updateCartItem(cartItemId, updatedCartItem);
		
		assertNotNull(result);
		assertEquals(updatedCartItem.getQuantity(), result.getQuantity());
	}
	
	@Test
	public void testPartialUpdateCartItem() {
		Long cartItemId = 1L;
		CartItem existingCartItem = new CartItem();
		existingCartItem.setCartItemId(cartItemId);
		Cart cart = new Cart();
		
		Map<String, Object> fieldsToUpdate = new HashMap<String, Object>();
		fieldsToUpdate.put("quantity", 10);
		fieldsToUpdate.put("product", new Product("Test product", BigDecimal.valueOf(32.05), 23));
		
		when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(existingCartItem));
		when(cartRepository.findByCartItems(existingCartItem)).thenReturn(Optional.of(cart));
		when(cartItemRepository.save(existingCartItem)).thenReturn(existingCartItem);
		CartItem result = cartItemService.partialUpdateCartItem(cartItemId, fieldsToUpdate);
		
		assertNotNull(result);
		assertEquals(fieldsToUpdate.get("quantity"), result.getQuantity());
		assertEquals(fieldsToUpdate.get("product"), result.getProduct());
	}
	
	@Test
	public void testDeleteCartItem() {
		Long cartItemId = 1L;
		CartItem cartItem = new CartItem();
		cartItem.setCartItemId(cartItemId);
		
		when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));
		cartItemService.deleteCartItem(cartItemId);
		
		verify(cartItemRepository, times(1)).delete(cartItem);
	}
}