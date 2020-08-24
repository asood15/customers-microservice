package com.spring.customer.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.customer.dto.CustomerDto;
import com.spring.customer.dto.CustomerLoginDetails;
import com.spring.customer.dto.CustomerRequestDto;
import com.spring.customer.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
	@GetMapping("/login")
	public ResponseEntity<CustomerDto> authenticateUser(@Valid @RequestBody CustomerLoginDetails loginDetails) {
		CustomerDto customer = customerService.authenticateCustomer(loginDetails);
		if (customer != null) {
			return ResponseEntity.status(HttpStatus.OK).body(customer);
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
	}
	
	@GetMapping("/{customerUuid}")
	public ResponseEntity<CustomerDto> getCustomer(@PathVariable String customerUuid) {
		CustomerDto customerDto = customerService.getCustomer(customerUuid);
		return ResponseEntity.status(HttpStatus.OK).body(customerDto);
	}
	
	@PutMapping("/{customerUuid}")
	public ResponseEntity<CustomerDto> updateCustomer(@PathVariable @NotBlank String customerUuid, 
			@Valid @RequestBody CustomerRequestDto customerRequestDto) {
		CustomerDto customerDto = customerService.updateCustomer(customerUuid, customerRequestDto);
		return ResponseEntity.status(HttpStatus.OK).body(customerDto);
	}
	
	@PatchMapping("/resetpassword/{customerUuid}")
	public ResponseEntity<CustomerDto> updatePassword(@PathVariable String customerUuid, 
			@NotBlank String password) {
		CustomerDto customerDto = customerService.updatePassword(customerUuid, password);
		return ResponseEntity.status(HttpStatus.OK).body(customerDto);
	}
	
	@GetMapping("/sortandlimit")
	public ResponseEntity<List<CustomerDto>> getSortedCustomers() {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.getSortedByDateCustomers());
	}
}
