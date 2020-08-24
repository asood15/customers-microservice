package com.spring.customer.service;

import java.lang.reflect.Type;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.customer.dto.CustomerDto;
import com.spring.customer.dto.CustomerLoginDetails;
import com.spring.customer.dto.CustomerRequestDto;
import com.spring.customer.entity.Customer;
import com.spring.customer.exception.CustomerAuthenticationException;
import com.spring.customer.exception.CustomerNotFoundException;
import com.spring.customer.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CustomerDto authenticateCustomer(CustomerLoginDetails loginDetails) {
		String email = loginDetails.getEmail();
		Customer customer = customerRepo.findByEmail(email);
		// check if this email exists in the database
		if (customer != null) {
			if (BCrypt.checkpw(loginDetails.getPassword(), customer.getPassword())) {
				return modelMapper.map(customer, CustomerDto.class);
			}
			throw new CustomerAuthenticationException("Invalid password for user " + email);
		} else {
			throw new CustomerNotFoundException("User with email " + email + " not found!");
		}
	}

	@Override
	public CustomerDto getCustomer(String customerUuid) {
		Customer customer = customerRepo.findByCustomerUuid(customerUuid);
		// check if this customer exists, if not throw an exception
		if (customer == null) {
			throw new CustomerNotFoundException("Customer with id " + customerUuid + " not found!");
		}
		CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
		return customerDto;
	}

	@Override
	public CustomerDto updateCustomer(String customerUuid, CustomerRequestDto customerRequestDto) {
		Customer customer = customerRepo.findByCustomerUuid(customerUuid);
		if (customer != null) {
			Customer updatedCustomer = modelMapper.map(customerRequestDto, Customer.class);
			updatedCustomer.setCustomerUuid(customerUuid);
			updatedCustomer.setId(customer.getId());
			updatedCustomer.setEmail(customer.getEmail());
			updatedCustomer.setPassword(customer.getPassword());
			customerRepo.save(updatedCustomer);
			return modelMapper.map(updatedCustomer, CustomerDto.class);
		} else {
			throw new CustomerNotFoundException("Customer with id " + customerUuid + " not found!");
		}
	}

	@Override
	public CustomerDto updatePassword(String customerUuid, String password) {
		Customer customer = customerRepo.findByCustomerUuid(customerUuid);
		if (customer != null) {
			customer.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
			Customer updatedCustomer = customerRepo.save(customer);
			return modelMapper.map(updatedCustomer, CustomerDto.class);
		} else {
			throw new CustomerNotFoundException("Customer with id " + customerUuid + " not found!");
		}
	}

	@Override
	public List<CustomerDto> getSortedByDateCustomers() {
		List<Customer> customers = customerRepo.findFirst3ByOrderByDateOfBirthDesc();
		Type listType = new TypeToken<List<CustomerDto>>() {
		}.getType();
		return modelMapper.map(customers, listType);
	}

}
