package com.spring.customer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.spring.customer.dto.CustomerDto;
import com.spring.customer.dto.CustomerLoginDetails;
import com.spring.customer.dto.CustomerRequestDto;
import com.spring.customer.entity.Customer;
import com.spring.customer.exception.CustomerAuthenticationException;
import com.spring.customer.service.CustomerService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CustomerService customerService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Gson gson;

	static List<Customer> customers = new ArrayList<>();
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@BeforeAll
	public static void initTests() throws ParseException {
		// Setup customer mock repository
		customers.add(
				new Customer("2bf1e766-7f49-431b-8efa-713c27cdb107", "Ashima", "Sood", formatter.parse("1987-11-15"),
						"$2a$10$SgaWXc7v93U5ZlKS5f.w3O5X7dt2JtUjWR8pQrSWHs9Kzt1OyXuYO", "ashima@gmail.com"));
		customers.add(
				new Customer("d9119d47-cdf4-11e8-8d6f-0242ac11002b", "Richard", "Carson", formatter.parse("1990-02-25"),
						"$2a$10$i9I74UF46IQ2sygxUR6GR.45upY3YrmtJs9cD5pA5ptpf7/qOy21i", "richard@gmail.com"));
		customers.add(
				new Customer("d0229d47-cdf4-11e8-8d6f-0242ac11013c", "Tony", "Roggers", formatter.parse("1984-11-04"),
						"$2a$10$3UIQC7J/m2w4ArDX0yiM1uYndB/swq3VZZDh7yiZPDBk4odhAi3zS", "tony@gmail.com"));
		customers.add(
				new Customer("d3309d47-cdf4-11e8-8d6f-0242ac11459d", "Peter", "Henley", formatter.parse("1985-07-24"),
						"$2a$10$29DSUuK1TDrw8h0qfIPTneuhSuQYqabGt7DLwNIpKFsCBlNu1Clj.", "peter@gmail.com"));
		customers.add(
				new Customer("d0669d47-cdf4-11e8-8d6f-0242ac11896e", "Sarah", "Palmer", formatter.parse("1991-09-21"),
						"$2a$10$PQ/dEk.Zm6/RF7Az9RvPpuDQnUQt.JfFv7Bq60BWtQhgsjOQXRdem", "sarah@gmail.com"));
		System.out.println("Running before all to initialize the data with " + customers.size() + " records");
	}

	@Test
	public void authenticateCustomerApiTest() throws Exception {
		CustomerLoginDetails validLoginDetails = new CustomerLoginDetails("ashima@gmail.com", "ashima321");

		Mockito.when(customerService.authenticateCustomer(Mockito.any(CustomerLoginDetails.class)))
				.thenReturn(modelMapper.map(customers.get(0), CustomerDto.class));

		MockHttpServletResponse result = mockMvc.perform(get("/api/customers/login").accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(validLoginDetails)).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(HttpStatus.OK.value(), result.getStatus());
		CustomerDto actualCustomer = gson.fromJson(result.getContentAsString(), CustomerDto.class);

		ArgumentCaptor<CustomerLoginDetails> customerCaptor = ArgumentCaptor.forClass(CustomerLoginDetails.class);
		verify(customerService, times(1)).authenticateCustomer(customerCaptor.capture());
		assertEquals("ashima@gmail.com", customerCaptor.getValue().getEmail());

		assertEquals("Ashima", actualCustomer.getFirstName());
		assertEquals("Sood", actualCustomer.getLastName());

	}

	@Test
	public void authenticateCustomerApiTest_invalidDetails() throws Exception {
		CustomerLoginDetails invalidLoginDetails = new CustomerLoginDetails("ashima@gmail.com", "ashima");
		Mockito.when(customerService.authenticateCustomer(Mockito.any(CustomerLoginDetails.class)))
				.thenThrow(CustomerAuthenticationException.class);

		MockHttpServletResponse result = mockMvc.perform(get("/api/customers/login").accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidLoginDetails)).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getStatus());
		assertThrows(CustomerAuthenticationException.class, () -> {
			customerService.authenticateCustomer(invalidLoginDetails);
		});
	}

	@Test
	public void getCustomerDetailsApiTest() throws Exception {
		Mockito.when(customerService.getCustomer("d9119d47-cdf4-11e8-8d6f-0242ac11002b"))
				.thenReturn(modelMapper.map(customers.get(1), CustomerDto.class));

		MockHttpServletResponse result = mockMvc.perform(get("/api/customers/d9119d47-cdf4-11e8-8d6f-0242ac11002b")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), result.getStatus());
		CustomerDto actualCustomer = gson.fromJson(result.getContentAsString(), CustomerDto.class);

		ArgumentCaptor<String> customerUuidCaptor = ArgumentCaptor.forClass(String.class);
		verify(customerService, times(1)).getCustomer(customerUuidCaptor.capture());
		assertEquals("d9119d47-cdf4-11e8-8d6f-0242ac11002b", customerUuidCaptor.getValue());

		assertEquals("Richard", actualCustomer.getFirstName());
		assertEquals("Carson", actualCustomer.getLastName());
		assertEquals("richard@gmail.com", actualCustomer.getEmail());
		assertEquals("1990-02-25", formatter.format(actualCustomer.getDateOfBirth()));
	}

	@Test
	public void updateCustomerDetailsApiTest() throws Exception {
		CustomerRequestDto customerRequestDto = new CustomerRequestDto("Julie", "Weber", formatter.parse("1985-03-28"));

		CustomerDto expectedCustomerDto = modelMapper.map(customerRequestDto, CustomerDto.class);
		expectedCustomerDto.setCustomerUuid(customers.get(0).getCustomerUuid());
		expectedCustomerDto.setEmail(customers.get(0).getEmail());

		Mockito.when(customerService.updateCustomer(Mockito.anyString(), Mockito.any(CustomerRequestDto.class)))
				.thenReturn(expectedCustomerDto);

		MockHttpServletResponse result = mockMvc.perform(put("/api/customers/2bf1e766-7f49-431b-8efa-713c27cdb109")
				.content(objectMapper.writeValueAsString(customerRequestDto)).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), result.getStatus());
		CustomerDto actualCustomer = gson.fromJson(result.getContentAsString(), CustomerDto.class);

		ArgumentCaptor<String> customerUuidCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<CustomerRequestDto> customerRequestDtoCaptor = ArgumentCaptor.forClass(CustomerRequestDto.class);
		verify(customerService, times(1)).updateCustomer(customerUuidCaptor.capture(),
				customerRequestDtoCaptor.capture());
		assertEquals("2bf1e766-7f49-431b-8efa-713c27cdb109", customerUuidCaptor.getValue());
		assertEquals("Julie", customerRequestDtoCaptor.getValue().getFirstName());

		assertEquals("Julie", actualCustomer.getFirstName());
		assertEquals("Weber", actualCustomer.getLastName());
		assertEquals("ashima@gmail.com", actualCustomer.getEmail());
		assertEquals("1985-03-28", formatter.format(actualCustomer.getDateOfBirth()));
	}

	@Test
	public void getSortedByDateCustomersApiTest() throws Exception {
		List<CustomerDto> expectedCustomers = new ArrayList<>();
		expectedCustomers.add(modelMapper.map(customers.get(4), CustomerDto.class));
		expectedCustomers.add(modelMapper.map(customers.get(1), CustomerDto.class));
		expectedCustomers.add(modelMapper.map(customers.get(0), CustomerDto.class));

		Mockito.when(customerService.getSortedByDateCustomers()).thenReturn(expectedCustomers);

		MockHttpServletResponse result = mockMvc.perform(get("/api/customers/sortandlimit")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), result.getStatus());
		CustomerDto[] customersArray = gson.fromJson(result.getContentAsString(), CustomerDto[].class);
		List<CustomerDto> actualCustomers = new ArrayList<>(Arrays.asList(customersArray));

		verify(customerService, times(1)).getSortedByDateCustomers();

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
