package com.matthewlim.ecommercewebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartItemNotFoundException extends RuntimeException {

	public CartItemNotFoundException() {
		super();
	}
	
	public CartItemNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public CartItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CartItemNotFoundException(String message) {
		super(message);
	}
	
	public CartItemNotFoundException(Throwable cause) {
		super(cause);
	}
}