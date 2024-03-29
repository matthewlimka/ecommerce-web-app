package com.matthewlim.ecommercewebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException() {
		super();
	}
	
	public ProductNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public ProductNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ProductNotFoundException(String message) {
		super(message);
	}
	
	public ProductNotFoundException(Throwable cause) {
		super(cause);
	}
}
