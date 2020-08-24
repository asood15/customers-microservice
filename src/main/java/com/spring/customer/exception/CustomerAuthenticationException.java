package com.spring.customer.exception;

public class CustomerAuthenticationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String errorMessage;

	public CustomerAuthenticationException() {
		super();
	}
	
	public CustomerAuthenticationException(String errorMessage) {
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
