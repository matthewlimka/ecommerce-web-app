package com.matthewlim.ecommercewebapp.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matthewlim.ecommercewebapp.enums.PaymentMethod;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Optional<Payment> findByPaymentDate(LocalDateTime paymentDate);
	Optional<Payment> findByTransactionId(String transactionId);
	List<Payment> findByAmount(BigDecimal amount);
	List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
	Optional<Payment> findByOrder(Order order);
}
