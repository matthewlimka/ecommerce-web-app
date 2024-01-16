package com.matthewlim.ecommercewebapp.services;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.matthewlim.ecommercewebapp.enums.PaymentMethod;
import com.matthewlim.ecommercewebapp.exceptions.PaymentNotFoundException;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.repositories.PaymentRepository;

@Service
public class PaymentService {

	Logger logger = LogManager.getLogger(PaymentService.class);
	
	@Autowired
	private PaymentRepository paymentRepo;
	
	public Payment findByPaymentId(Long paymentId) {
		Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> {
					logger.error("No payment with payment ID " + paymentId + " found");
					return new PaymentNotFoundException("No payment with payment ID " + paymentId + " found");
				});
		
		logger.info("Successfully found payment with payment ID " + paymentId);
		return payment;
	}
	
	public Payment findByPaymentDate(LocalDateTime paymentDate) {
		Payment payment = paymentRepo.findByPaymentDate(paymentDate)
				.orElseThrow(() -> {
					logger.error("No payment with payment date " + paymentDate + " found");
					return new PaymentNotFoundException("No payment with payment date " + paymentDate + " found");
				});
		
		logger.info("Successfully found payment with payment date " + paymentDate);
		return payment;
	}
	
	public Payment findByTransactionId(String transactionId) {
		Payment payment = paymentRepo.findByTransactionId(transactionId)
				.orElseThrow(() -> {
					logger.error("No payment with transaction ID " + transactionId + " found");
					return new PaymentNotFoundException("No payment with transaction ID " + transactionId + " found");
				});
		
		logger.info("Successfully found payment with transaction ID " + transactionId);
		return payment;
	}
	
	public List<Payment> findByAmount(BigDecimal amount) {
		List<Payment> payments = paymentRepo.findByAmount(amount);
		
		logger.info("Successfully found " + payments.size() + " payment(s) with amount " + amount);
		return payments;
	}
	
	public List<Payment> findByPaymentMethod(PaymentMethod paymentMethod) {
		List<Payment> payments = paymentRepo.findByPaymentMethod(paymentMethod);
		
		logger.info("Successfully found " + payments.size() + " payment(s) with payment method " + paymentMethod);
		return payments;
	}
	
	public Payment findByOrder(Order order) {
		Payment payment = paymentRepo.findByOrder(order)
				.orElseThrow(() -> {
					logger.error("No payment with order ID " + order.getOrderId() + " found");
					return new PaymentNotFoundException("No payment with order ID " + order.getOrderId() + " found");
				});
		
		logger.info("Successfully found payment with order ID " + order.getOrderId());
		return payment;
	}
	
	public List<Payment> findAllPayments() {
		List<Payment> paymentList = paymentRepo.findAll();
		
		logger.info("Successfully found " + paymentList.size() + " payments");
		return paymentList;
	}
	
	public Payment addPayment(Payment payment) {
		logger.info("Successfully registered new payment with payment ID " + payment.getPaymentId());
		return paymentRepo.save(payment);
	}
	
	public Payment updatePayment(Long paymentId, Payment updatedPayment) {
		Payment existingPayment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> {
					logger.error("No payment with payment ID " + paymentId + " found");
					return new PaymentNotFoundException("No payment with payment ID " + paymentId + " found");
				});
		
		existingPayment.setPaymentDate(updatedPayment.getPaymentDate());
		existingPayment.setTransactionId(updatedPayment.getTransactionId());
		existingPayment.setAmount(updatedPayment.getAmount());
		existingPayment.setPaymentMethod(updatedPayment.getPaymentMethod());
		existingPayment.setOrder(updatedPayment.getOrder());
		
		logger.info("Successfully updated payment with payment ID " + paymentId);
		return paymentRepo.save(existingPayment);
	}
	
	public Payment partialUpdatePayment(Long paymentId, Map<String, Object> fields) {
		Payment existingPayment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> {
					logger.error("No payment with payment ID " + paymentId + " found");
					return new PaymentNotFoundException("No payment with payment ID " + paymentId + " found");
				});
		
		fields.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(Payment.class, key);
			field.setAccessible(true);
			ReflectionUtils.setField(field, existingPayment, value);
		});
		
		logger.info("Successfully updated payment with payment ID " + paymentId);
		return paymentRepo.save(existingPayment);
	}
	
	public void deletePayment(Long paymentId) {
		Payment existingPayment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> {
					logger.error("No payment with payment ID " + paymentId + " found");
					return new PaymentNotFoundException("No payment with payment ID " + paymentId + " found");
				});
		
		logger.info("Successfully deleted payment with payment ID " + paymentId);
		paymentRepo.delete(existingPayment);
	}
}
