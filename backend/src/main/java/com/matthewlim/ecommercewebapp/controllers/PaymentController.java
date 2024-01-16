package com.matthewlim.ecommercewebapp.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matthewlim.ecommercewebapp.enums.PaymentMethod;
import com.matthewlim.ecommercewebapp.exceptions.PaymentNotFoundException;
import com.matthewlim.ecommercewebapp.models.Order;
import com.matthewlim.ecommercewebapp.models.Payment;
import com.matthewlim.ecommercewebapp.services.PaymentService;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;

	@GetMapping
	public List<Payment> getPayments(@RequestParam(required = false) LocalDateTime paymentDate, @RequestParam(required = false) String transactionId, @RequestParam(required = false) BigDecimal amount, @RequestParam(required = false) PaymentMethod paymentMethod, @RequestParam(required = false) Order order) {
		if ( paymentDate != null ) {
			return new ArrayList<Payment>(Arrays.asList(paymentService.findByPaymentDate(paymentDate)));
		} else if ( transactionId != null ) {
			return new ArrayList<Payment>(Arrays.asList(paymentService.findByTransactionId(transactionId)));
		} else if ( amount != null ) {
			return paymentService.findByAmount(amount);
		} else if ( paymentMethod != null ) {
			return paymentService.findByPaymentMethod(paymentMethod);
		} else if ( order != null ) {
			return new ArrayList<Payment>(Arrays.asList(paymentService.findByOrder(order)));
		} else {
			return paymentService.findAllPayments();	
		}
	}

	@GetMapping("/{id}")
	public Payment getPayment(@PathVariable Long paymentId) throws PaymentNotFoundException {
		return paymentService.findByPaymentId(paymentId);
	}
	
	@PostMapping
	public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
		Payment savedPayment = paymentService.addPayment(payment);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(payment.getPaymentId())
				.toUri();
		
		return ResponseEntity.created(location).body(savedPayment);
	}
	
	@PutMapping("/{id}")
	public Payment updatePayment(@PathVariable Long paymentId, @RequestBody Payment updatedPayment) {
		return paymentService.updatePayment(paymentId, updatedPayment);
	}
	
	@PatchMapping("/{id}")
	public Payment partialUpdatePayment(@PathVariable Long paymentId, @RequestBody Map<String, Object> fields) {
		return paymentService.partialUpdatePayment(paymentId, fields);
	}
	
	@DeleteMapping("/{id}")
	public void deletePayment(@PathVariable Long paymentId) {
		paymentService.deletePayment(paymentId);
	}
}
