package com.spring.customer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring.customer.entity.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>{

	public Customer findByEmail(String email);
	public Customer findByCustomerUuid(String customerUuid);
	public List<Customer> findFirst3ByOrderByDateOfBirthDesc();
}
