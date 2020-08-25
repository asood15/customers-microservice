package com.spring.customer.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false, unique = true)
	private String customerUuid;

	@Column(nullable = false, length = 50)
	@NotBlank(message = "First Name cannot be empty")
	private String firstName;

	@Column(nullable = false, length = 50)
	@NotBlank(message = "Last Name cannot be empty")
	private String lastName;

	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@NotNull(message = "Date cannot be empty")
	private Date dateOfBirth;

	@Column(nullable = false, length = 150)
	@NotBlank(message = "Password cannot be empty")
	private String password;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Please provide a valid email")
	private String email;

	public Customer() {
		super();
	}

	
	public Customer(String customerUuid, String firstName,	String lastName, Date dateOfBirth, 
			String password, String email) {
		super();
		this.customerUuid = customerUuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.password = password;
		this.email = email;
	}


	public String getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(String customerUuid) {
		this.customerUuid = customerUuid;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
