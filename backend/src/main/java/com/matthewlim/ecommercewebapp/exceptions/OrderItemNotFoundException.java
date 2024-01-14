package com.matthewlim.ecommercewebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderItemNotFoundException extends RuntimeException {

	public OrderItemNotFoundException() {
		super();
	}
	
	public OrderItemNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public OrderItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public OrderItemNotFoundException(String message) {
		super(message);
	}
	
	public OrderItemNotFoundException(Throwable cause) {
		super(cause);
	}
}