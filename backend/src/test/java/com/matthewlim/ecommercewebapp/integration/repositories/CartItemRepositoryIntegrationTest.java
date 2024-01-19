package com.matthewlim.ecommercewebapp.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.repositories.CartItemRepository;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;
import com.matthewlim.ecommercewebapp.repositories.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryIntegrationTest {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
    @Test
    public void testFindByQuantity() {
    	int quantity = 10;
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        List<CartItem> result = cartItemRepository.findByQuantity(quantity);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
    }
    
    @Test
    public void testFindByCart() {
    	Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        List<CartItem> cartItemList = new ArrayList<CartItem>(Arrays.asList(cartItem));
        cartItem.setCart(cart);
        cart.setCartItems(cartItemList);
        cartRepository.save(cart);
        cartItemRepository.save(cartItem);

        List<CartItem> result = cartItemRepository.findByCart(cart);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
    }
    
    @Test
    public void testFindByProduct() {
    	Product product = new Product();
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        productRepository.save(product);
        cartItemRepository.save(cartItem);

        List<CartItem> result = cartItemRepository.findByProduct(product);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
    }
}