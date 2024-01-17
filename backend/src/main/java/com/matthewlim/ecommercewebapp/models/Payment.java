package com.matthewlim.ecommercewebapp.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matthewlim.ecommercewebapp.enums.PaymentMethod;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@NoArgsConstructor
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;
	
	private LocalDateTime paymentDate;
	private String transactionId;
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	public Payment(LocalDateTime paymentDate, String transactionId, BigDecimal amount, PaymentMethod paymentMethod,
			Order order) {
		super();
		this.paymentDate = paymentDate;
		this.transactionId = transactionId;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.order = order;
	}
}
