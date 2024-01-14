package com.matthewlim.ecommercewebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PaymentNotFoundException extends RuntimeException {

	public PaymentNotFoundException() {
		super();
	}
	
	public PaymentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public PaymentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PaymentNotFoundException(String message) {
		super(message);
	}
	
	public PaymentNotFoundException(Throwable cause) {
		super(cause);
	}
}
