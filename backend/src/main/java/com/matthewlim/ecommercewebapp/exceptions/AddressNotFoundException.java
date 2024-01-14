package com.matthewlim.ecommercewebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AddressNotFoundException extends RuntimeException {

	public AddressNotFoundException() {
		super();
	}
	
	public AddressNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public AddressNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AddressNotFoundException(String message) {
		super(message);
	}
	
	public AddressNotFoundException(Throwable cause) {
		super(cause);
	}
}
