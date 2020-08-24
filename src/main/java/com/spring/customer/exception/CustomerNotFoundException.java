package com.spring.customer.exception;

public class CustomerNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String errorMessage;

	public CustomerNotFoundException() {
		super();
	}
	
	public CustomerNotFoundException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
