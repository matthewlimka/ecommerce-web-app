package com.matthewlim.ecommercewebapp.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.CartItemRepository;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartRepositoryIntegrationTest {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private UserRepository userRepository;
	
    @Test
    public void testFindByUser() {
        User user = new User();
    	Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        userRepository.save(user);
        cartRepository.save(cart);

        Optional<Cart> result = cartRepository.findByUser(user);

        assertTrue(result.isPresent());
        assertEquals(cart, result.get());
    }

    @Test
    public void testFindByCartItems() {
        CartItem cartItem = new CartItem();
        List<CartItem> cartItemList = new ArrayList<CartItem>(Arrays.asList(cartItem));
    	Cart cart = new Cart();
        cart.setCartItems(cartItemList);
        cartItem.setCart(cart);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

        Optional<Cart> result = cartRepository.findByCartItems(cartItem);

        assertTrue(result.isPresent());
        assertEquals(cart, result.get());
    }
}