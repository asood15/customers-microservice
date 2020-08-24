package com.spring.customer.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CustomerLoginDetails {

	@NotBlank(message = "*Email cannot be empty")
	@Email(message = "*Please provide a valid email")
	private String email;
	
	@NotBlank(message = "*Password cannot be empty")
	private String password;
	
	public CustomerLoginDetails() {
		
	}
	
	public CustomerLoginDetails(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
