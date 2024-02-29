package com.matthewlim.ecommercewebapp.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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

import com.matthewlim.ecommercewebapp.enums.PaymentMethod;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.repositories.PaymentRepository;
import com.matthewlim.ecommercewebapp.services.PaymentService;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceUnitTest {

	@Mock
	private PaymentRepository paymentRepository;
	
	@InjectMocks
	private PaymentService paymentService;
	
	@Test
	public void testFindAllPayments() {
		List<Payment> paymentList = new ArrayList<Payment>(Arrays.asList(new Payment(), new Payment()));		
		when(paymentRepository.findAll()).thenReturn(paymentList);
		
		List<Payment> result = paymentService.findAllPayments();
		
		assertNotNull(result);
		assertThat(result).hasSize(paymentList.size()).containsExactlyElementsOf(paymentList);
	}
	
	@Test
	public void testFindByPaymentId() {
		Long paymentId = 1L;
		Payment payment = new Payment();
		payment.setPaymentId(paymentId);
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
		
		Payment result = paymentService.findByPaymentId(paymentId);
		
		assertNotNull(result);
		assertEquals(paymentId, result.getPaymentId());
	}
	
	@Test
	public void testFindByPaymentDate() {
		LocalDateTime paymentDate = LocalDateTime.now();
		Payment payment = new Payment();
		payment.setPaymentDate(paymentDate);
		when(paymentRepository.findByPaymentDate(paymentDate)).thenReturn(Optional.of(payment));
		
		Payment result = paymentService.findByPaymentDate(paymentDate);
		
		assertNotNull(result);
		assertEquals(paymentDate, result.getPaymentDate());
	}
	
	@Test
	public void testFindByTransactionId() {
		String transactionId = "transactionId";
		Payment payment = new Payment();
		payment.setTransactionId(transactionId);
		when(paymentRepository.findByTransactionId(transactionId)).thenReturn(Optional.of(payment));
		
		Payment result = paymentService.findByTransactionId(transactionId);
		
		assertNotNull(result);
		assertEquals(transactionId, result.getTransactionId());
	}
	
	@Test
	public void testFindByAmount() {
		BigDecimal amount = BigDecimal.valueOf(123.10);
		Payment payment = new Payment();
		payment.setAmount(amount);
		List<Payment> paymentList = new ArrayList<Payment>(Arrays.asList(payment));
		
		when(paymentRepository.findByAmount(amount)).thenReturn(paymentList);
		List<Payment> result = paymentService.findByAmount(amount);
		
		assertNotNull(result);
		assertThat(result).hasSize(paymentList.size()).containsExactlyElementsOf(paymentList);
	}
	
	@Test
	public void testFindByPaymentMethod() {
		PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD_MASTERCARD;
		Payment payment = new Payment();
		payment.setPaymentMethod(paymentMethod);
		List<Payment> paymentList = new ArrayList<Payment>(Arrays.asList(payment));
		
		when(paymentRepository.findByPaymentMethod(paymentMethod)).thenReturn(paymentList);
		List<Payment> result = paymentService.findByPaymentMethod(paymentMethod);
		
		assertNotNull(result);
		assertThat(result).hasSize(paymentList.size()).containsExactlyElementsOf(paymentList);
	}
	
	@Test
	public void testFindByOrder() {
		Order order = new Order();
		Payment payment = new Payment();
		payment.setOrder(order);
		when(paymentRepository.findByOrder(order)).thenReturn(Optional.of(payment));
		
		Payment result = paymentService.findByOrder(order);
		
		assertNotNull(result);
		assertEquals(order, result.getOrder());
	}
	
	@Test
	public void testAddPayment() {
		Payment payment = new Payment();
		when(paymentRepository.save(payment)).thenReturn(payment);
		
		Payment result = paymentService.addPayment(payment);
		
		assertNotNull(result);
		verify(paymentRepository, times(1)).save(payment);
	}
	
	@Test
	public void testUpdatePayment() {
		Long paymentId = 1L;
		Payment existingPayment = new Payment();
		existingPayment.setPaymentId(paymentId);
		
		Payment updatedPayment = new Payment();
		updatedPayment.setAmount(BigDecimal.valueOf(89.20));
		updatedPayment.setPaymentMethod(PaymentMethod.CREDIT_CARD_VISA);
		
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(existingPayment));
		when(paymentRepository.save(existingPayment)).thenReturn(updatedPayment);
		
		Payment result = paymentService.updatePayment(paymentId, updatedPayment);
		
		assertNotNull(result);
		assertEquals(updatedPayment.getAmount().setScale(2, RoundingMode.HALF_UP), result.getAmount().setScale(2, RoundingMode.HALF_UP));
		assertEquals(updatedPayment.getPaymentMethod(), result.getPaymentMethod());
	}
	
	@Test
	public void testPartialUpdatePayment() {
		Long paymentId = 1L;
		Payment existingPayment = new Payment();
		existingPayment.setPaymentId(paymentId);
		
		Map<String, Object> fieldsToUpdate = new HashMap<String, Object>();
		fieldsToUpdate.put("amount", BigDecimal.valueOf(51.20));
		fieldsToUpdate.put("paymentMethod", PaymentMethod.PAYNOW);
		
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(existingPayment));
		when(paymentRepository.save(existingPayment)).thenReturn(existingPayment);
		
		Payment result = paymentService.partialUpdatePayment(paymentId, fieldsToUpdate);
		
		assertNotNull(result);
		assertEquals(((BigDecimal) fieldsToUpdate.get("amount")).setScale(2, RoundingMode.HALF_UP), result.getAmount().setScale(2, RoundingMode.HALF_UP));
		assertEquals(fieldsToUpdate.get("paymentMethod"), result.getPaymentMethod());
	}
	
	@Test
	public void testDeletePayment() {
		Long paymentId = 1L;
		Payment payment = new Payment();
		payment.setPaymentId(paymentId);
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
		
		paymentService.deletePayment(paymentId);
		
		verify(paymentRepository, times(1)).delete(payment);
	}
}