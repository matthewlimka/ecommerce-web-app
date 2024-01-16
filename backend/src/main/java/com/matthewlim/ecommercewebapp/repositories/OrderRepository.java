package com.matthewlim.ecommercewebapp.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matthewlim.ecommercewebapp.enums.OrderStatus;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.OrderItem;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.models.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByOrderDate(LocalDateTime orderDate);
	List<Order> findByTotalAmount(BigDecimal totalAmount);
	List<Order> findByOrderStatus(OrderStatus orderStatus);
	List<Order> findByUser(User user);
	Optional<Order> findByOrderItems(OrderItem orderItem);
	Optional<Order> findByPayment(Payment payment);
}
