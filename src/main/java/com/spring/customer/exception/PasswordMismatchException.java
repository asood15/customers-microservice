package com.spring.customer.exception;

public class PasswordMismatchException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String errorMessage;

	public PasswordMismatchException() {
		super();
	}
	
	public PasswordMismatchException(String errorMessage) {
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
