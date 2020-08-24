package com.spring.customer.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CustomerDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String lastName;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;
	private String customerUuid;
	private String email;
	
	public CustomerDto() {
		super();
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getCustomerUuid() {
		return customerUuid;
	}
	public void setCustomerUuid(String customerUuid) {
		this.customerUuid = customerUuid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
