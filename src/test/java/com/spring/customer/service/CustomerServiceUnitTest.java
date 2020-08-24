package com.spring.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.spring.customer.dto.CustomerDto;
import com.spring.customer.dto.CustomerLoginDetails;
import com.spring.customer.dto.CustomerRequestDto;
import com.spring.customer.entity.Customer;
import com.spring.customer.exception.CustomerAuthenticationException;
import com.spring.customer.repository.CustomerRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CustomerServiceUnitTest {

	@Autowired
	private CustomerService customerService;

	@MockBean
	private CustomerRepository customerRepo;

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
	@DisplayName("Authenticate Customer Test")
	public void authenticateCustomerTest() throws ParseException {
		Mockito.when(customerRepo.findByEmail("ashima@gmail.com")).thenReturn(customers.get(0));

		CustomerLoginDetails validLoginDetails = new CustomerLoginDetails("ashima@gmail.com", "ashima321");
		CustomerLoginDetails invalidLoginDetails = new CustomerLoginDetails("ashima@gmail.com", "ashima");

		// Assert correct details are authenticated
		assertNotNull(customerService.authenticateCustomer(validLoginDetails), "Customer authentication successfully");

		// Assert incorrect details are not authenticated
		assertThrows(CustomerAuthenticationException.class, () -> {
			customerService.authenticateCustomer(invalidLoginDetails);
		}, "Customer authentication failed");
	}

	@Test
	@DisplayName("Retrieve Customer")
	public void getCustomerTest() {

		Mockito.when(customerRepo.findByCustomerUuid("d9119d47-cdf4-11e8-8d6f-0242ac11002b"))
				.thenReturn(customers.get(1));

		CustomerDto actualCustomer = customerService.getCustomer("d9119d47-cdf4-11e8-8d6f-0242ac11002b");

		// Assert expected and actual data retrieved for the customer matching the uuid passed
		assertEquals("Richard", actualCustomer.getFirstName(), "First name matches");
		assertEquals("Carson", actualCustomer.getLastName(), "Last name matches");
		assertEquals("d9119d47-cdf4-11e8-8d6f-0242ac11002b", actualCustomer.getCustomerUuid(), "Uuid matches");
		assertEquals("richard@gmail.com", actualCustomer.getEmail(), "Email matches");
	}

	@Test
	@DisplayName("Update Customer details")
	public void updateCustomerTest() throws ParseException {

		Mockito.when(customerRepo.findByCustomerUuid(customers.get(0).getCustomerUuid())).thenReturn(customers.get(0));

		CustomerRequestDto updatedCustomer = new CustomerRequestDto("Julie", "Weber",
				customers.get(0).getDateOfBirth());

		CustomerDto actualCustomer = customerService.updateCustomer("2bf1e766-7f49-431b-8efa-713c27cdb107", updatedCustomer);

		// Assert updated customer details
		assertEquals("Julie", actualCustomer.getFirstName(), "First name matches");
		assertEquals("Weber", actualCustomer.getLastName(), "Last name matches");
		assertEquals("1987-11-15", formatter.format(actualCustomer.getDateOfBirth()), "Date of birth matches");
		assertEquals("2bf1e766-7f49-431b-8efa-713c27cdb107", actualCustomer.getCustomerUuid(), "Uuid matches");

	}

	@Test
	@DisplayName("Get Customers Sorted by Date")
	public void getSortedCustomersTest() throws ParseException {

		List<Customer> expectedCustomers = new ArrayList<>();
		expectedCustomers.add(customers.get(4));
		expectedCustomers.add(customers.get(1));
		expectedCustomers.add(customers.get(0));

		Mockito.when(customerRepo.findFirst3ByOrderByDateOfBirthDesc()).thenReturn(expectedCustomers);

		List<CustomerDto> actualCustomers = customerService.getSortedByDateCustomers();

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
