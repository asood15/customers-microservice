package com.spring.customer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.spring.customer.dto.CustomerDto;
import com.spring.customer.dto.CustomerLoginDetails;
import com.spring.customer.dto.CustomerRequestDto;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
// for running some sql files before and after test execution, located in src/test/resources folder
@SqlGroup({
		@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:drop-test.sql",
				"classpath:schema-test.sql", "classpath:data-test.sql" }),
		@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:drop-test.sql") })
public class CustomerIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Gson gson;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void ifValidUserAuthenticated_thenSuccess() throws Exception {
		CustomerLoginDetails loginDetails = new CustomerLoginDetails("ashima@gmail.com", "ashima321");
		MockHttpServletResponse result = mockMvc
				.perform(post("/api/customers/login").accept(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginDetails)).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		CustomerDto actualCustomer = gson.fromJson(result.getContentAsString(), CustomerDto.class);
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		assertEquals("Ashima", actualCustomer.getFirstName());
		assertEquals("Sood", actualCustomer.getLastName());
	}

	@Test
	public void ifInvalidUserAuthenticated_thenFailure() throws Exception {
		CustomerLoginDetails loginDetails = new CustomerLoginDetails("ashima@gmail.com", "ashima");
		MockHttpServletResponse result = mockMvc
				.perform(post("/api/customers/login").accept(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginDetails)).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getStatus());
	}

	@Test
	public void getValidCustomerDetails_thenSucess() throws Exception {
		MockHttpServletResponse result = mockMvc.perform(get("/api/customers/d9119d47-cdf4-11e8-8d6f-0242ac11002b")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), result.getStatus());
		CustomerDto actualCustomer = gson.fromJson(result.getContentAsString(), CustomerDto.class);

		assertEquals("Richard", actualCustomer.getFirstName());
		assertEquals("Carson", actualCustomer.getLastName());
		assertEquals("richard@gmail.com", actualCustomer.getEmail());
		assertEquals("1990-02-25", formatter.format(actualCustomer.getDateOfBirth()));

	}

	@Test
	public void updateCustomerDetails_thenSucess() throws Exception {
		CustomerRequestDto customerRequestDto = new CustomerRequestDto("Julie", "Weber", formatter.parse("1985-03-28"));

		MockHttpServletResponse result = mockMvc.perform(put("/api/customers/2bf1e766-7f49-431b-8efa-713c27cdb109")
				.content(objectMapper.writeValueAsString(customerRequestDto)).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), result.getStatus());
		CustomerDto actualCustomer = gson.fromJson(result.getContentAsString(), CustomerDto.class);

		assertEquals("Julie", actualCustomer.getFirstName());
		assertEquals("Weber", actualCustomer.getLastName());
		assertEquals("ashima@gmail.com", actualCustomer.getEmail());
		assertEquals("1985-03-28", formatter.format(actualCustomer.getDateOfBirth()));

		// Validate the same uuid again and verify if details have changed
		result = mockMvc.perform(get("/api/customers/2bf1e766-7f49-431b-8efa-713c27cdb109")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), result.getStatus());
		actualCustomer = gson.fromJson(result.getContentAsString(), CustomerDto.class);
		assertEquals("Julie", actualCustomer.getFirstName());
	}

	@Test
	public void getSortedByDateCustomers() throws Exception {
		MockHttpServletResponse result = mockMvc.perform(get("/api/customers/sortandlimit")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), result.getStatus());
		CustomerDto[] customersArray = gson.fromJson(result.getContentAsString(), CustomerDto[].class);
		List<CustomerDto> actualCustomers = new ArrayList<>(Arrays.asList(customersArray));

		// Assert size of list returned
		assertEquals(3, actualCustomers.size(), "3 customers retrieved");

		// Assert first customer details
		assertEquals("Sarah", actualCustomers.get(0).getFirstName(), "First customers's first name matches");
		assertEquals("Palmer", actualCustomers.get(0).getLastName(), "First customer's last name matches");
		assertEquals("1991-09-21", formatter.format(actualCustomers.get(0).getDateOfBirth()),
				"First customer's date of birth matches");

		// Assert second customer details
		assertEquals("Richard", actualCustomers.get(1).getFirstName(), "Second customers's first name matches");
		assertEquals("Carson", actualCustomers.get(1).getLastName(), "Second customer's last name matches");
		assertEquals("1990-02-25", formatter.format(actualCustomers.get(1).getDateOfBirth()),
				"Second customer's date of birth matches");

		// Assert third customer details
		assertEquals("Ashima", actualCustomers.get(2).getFirstName(), "Third customers's first name matches");
		assertEquals("Sood", actualCustomers.get(2).getLastName(), "Third customer's last name matches");
		assertEquals("1987-11-15", formatter.format(actualCustomers.get(2).getDateOfBirth()),
				"Third customer's date of birth matches");
	}
}
