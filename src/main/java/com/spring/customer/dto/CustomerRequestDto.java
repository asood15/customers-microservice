package com.spring.customer.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class CustomerRequestDto {

	@Size(max=50, min=2, message = "First name should be of 2-50 characters")
	@NotBlank(message = "*First name cannot be empty")
	private String firstName;
	
	@Size(max=50, min=2, message = "First name should be of 2-50 characters")
	@NotBlank(message = "Last name cannot be empty")
	private String lastName;
	

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@NotNull(message = "Date cannot be empty")
	private Date dateOfBirth;
	
	public CustomerRequestDto() {
		
	}
	
	
	public CustomerRequestDto(String firstName, String lastName, Date dateOfBirth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
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
	
	
}
