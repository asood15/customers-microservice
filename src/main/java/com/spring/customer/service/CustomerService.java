package com.spring.customer.service;

import java.util.List;

import com.spring.customer.dto.CustomerDto;
import com.spring.customer.dto.CustomerLoginDetails;
import com.spring.customer.dto.CustomerRequestDto;

public interface CustomerService {

	public CustomerDto authenticateCustomer(CustomerLoginDetails loginDetails);
	public CustomerDto getCustomer(String customerUuid);
	public CustomerDto updateCustomer(String customerUuid, CustomerRequestDto customerRequestDto);
	public CustomerDto updatePassword(String customerUuid, String password);
	public List<CustomerDto> getSortedByDateCustomers();
}
