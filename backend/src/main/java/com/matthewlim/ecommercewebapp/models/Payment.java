package com.matthewlim.ecommercewebapp.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.matthewlim.ecommercewebapp.enums.PaymentMethod;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Component
@Entity
public class Payment {

	@Id
	@GeneratedValue
	private Long paymentId;
	
	private LocalDateTime paymentDate;
	private String transactionId;
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	public Payment() {
		super();
	}
	
	public Payment(LocalDateTime paymentDate, String transactionId, BigDecimal amount, PaymentMethod paymentMethod,
			Order order) {
		super();
		this.paymentDate = paymentDate;
		this.transactionId = transactionId;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.order = order;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
