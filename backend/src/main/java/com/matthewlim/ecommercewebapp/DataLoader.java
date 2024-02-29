package com.matthewlim.ecommercewebapp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.matthewlim.ecommercewebapp.enums.Role;
import com.matthewlim.ecommercewebapp.models.Address;
import com.matthewlim.ecommercewebapp.models.Cart;
import com.matthewlim.ecommercewebapp.models.CartItem;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.models.Product;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.repositories.AddressRepository;
import com.matthewlim.ecommercewebapp.repositories.CartItemRepository;
import com.matthewlim.ecommercewebapp.repositories.CartRepository;
import com.matthewlim.ecommercewebapp.repositories.OrderItemRepository;
import com.matthewlim.ecommercewebapp.repositories.OrderRepository;
import com.matthewlim.ecommercewebapp.repositories.PaymentRepository;
import com.matthewlim.ecommercewebapp.repositories.ProductRepository;
import com.matthewlim.ecommercewebapp.repositories.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private CartRepository cartRepo;
	
	@Autowired
	private CartItemRepository cartItemRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private OrderItemRepository orderItemRepo;
	
	@Autowired
	private PaymentRepository paymentRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<User> usersToSave = new ArrayList<>();
		usersToSave.add(new User("user", passwordEncoder.encode("password"), "user@email.com", "user", "test", Role.USER, new ArrayList<Order>(), new Address(), new Cart()));
		usersToSave.add(new User("admin", passwordEncoder.encode("password"), "admin@email.com", "admin", "test", Role.ADMIN, new ArrayList<Order>(), new Address(), new Cart()));
		userRepo.saveAll(usersToSave);
		
		List<Address> addressesToSave = new ArrayList<>();
		addressRepo.saveAll(addressesToSave);

		List<Product> productsToSave = new ArrayList<>();
		productsToSave.add(new Product("M3 MacBook Pro", BigDecimal.valueOf(2599.90), 15));
		productsToSave.add(new Product("Baseus USB-C to USB-A cable (1m)", BigDecimal.valueOf(12.50), 30));
		productsToSave.add(new Product("Xiaomi 10000mAh powerbank", BigDecimal.valueOf(21.20), 42));
		productsToSave.add(new Product("Keychron K3 Keyboard", BigDecimal.valueOf(123.00), 7));
		productsToSave.add(new Product("Logitech MX Master 3", BigDecimal.valueOf(115.80), 23));
		productsToSave.add(new Product("NVIDIA RTX 4090 Founders' Edition", BigDecimal.valueOf(2199.90), 17));
		productsToSave.add(new Product("Cross Body Bag/Sling Bag Canvas", BigDecimal.valueOf(12.97), 98));
		productsToSave.add(new Product("Premium Leather Desk Pad", BigDecimal.valueOf(29.90), 309));
		productsToSave.add(new Product("Magnetic USB Cable Organizer", BigDecimal.valueOf(4.42), 57));
		productRepo.saveAll(productsToSave);
		
		List<CartItem> cartItemsToSave = new ArrayList<>();
		cartItemRepo.saveAll(cartItemsToSave);
		
		List<Cart> cartsToSave = new ArrayList<>();
		cartRepo.saveAll(cartsToSave);
		
		List<OrderItem> orderItemsToSave = new ArrayList<>();
		orderItemRepo.saveAll(orderItemsToSave);
		
		List<Order> ordersToSave = new ArrayList<>();
		orderRepo.saveAll(ordersToSave);
		
		List<Payment> paymentsToSave = new ArrayList<>();
		paymentRepo.saveAll(paymentsToSave);
	}
}