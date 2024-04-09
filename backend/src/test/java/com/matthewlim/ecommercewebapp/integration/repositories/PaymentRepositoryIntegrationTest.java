package com.matthewlim.ecommercewebapp.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.matthewlim.ecommercewebapp.enums.PaymentMethod;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.repositories.OrderRepository;
import com.matthewlim.ecommercewebapp.repositories.PaymentRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PaymentRepositoryIntegrationTest {

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
    @Test
    public void testFindByPaymentDate() {
        LocalDateTime paymentDate = LocalDateTime.now();
    	Payment payment = new Payment();
        payment.setPaymentDate(paymentDate);
        paymentRepository.save(payment);

        Optional<Payment> result = paymentRepository.findByPaymentDate(paymentDate);

        assertTrue(result.isPresent());
        assertEquals(payment, result.get());
    }
    
    @Test
    public void testFindByTransactionId() {
        String transactionId = "123456";
    	Payment payment = new Payment();
        payment.setTransactionId(transactionId);
        paymentRepository.save(payment);

        Optional<Payment> result = paymentRepository.findByTransactionId(transactionId);

        assertTrue(result.isPresent());
        assertEquals(payment, result.get());
    }

    @Test
    public void testFindByAmount() {
    	BigDecimal amount = BigDecimal.valueOf(119.00);
        Payment payment = new Payment();
        payment.setAmount(amount);
        paymentRepository.save(payment);

        List<Payment> result = paymentRepository.findByAmount(amount);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
    }
    
    @Test
    public void testFindByPaymentMethod() {
    	PaymentMethod paymentMethod = PaymentMethod.PAYNOW;
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);
        paymentRepository.save(payment);

        List<Payment> result = paymentRepository.findByPaymentMethod(paymentMethod);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());	
        assertEquals(payment, result.get(0));
    }
    
    @Test
    public void testFindByOrder() {
        Order order = new Order();
    	Payment payment = new Payment();
        payment.setOrder(order);
        order.setPayment(payment);
        orderRepository.save(order);
        paymentRepository.save(payment);

        Optional<Payment> result = paymentRepository.findByOrder(order);

        assertTrue(result.isPresent());
        assertEquals(payment, result.get());
    }
}