package com.project.JewelryMS.UnitTest;

import com.project.JewelryMS.entity.Customer;
import com.project.JewelryMS.model.Customer.*;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerDemographics;
import com.project.JewelryMS.repository.CustomerRepository;
import com.project.JewelryMS.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

public class CustomerServiceUnitTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCustomer() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("test@example.com");
        request.setPhoneNumber("1234567890");
        request.setGender("Male");
        request.setCusName("Test Customer");

        Customer createdCustomer = new Customer();
        createdCustomer.setPK_CustomerID(1L);
        createdCustomer.setEmail(request.getEmail());
        createdCustomer.setPhoneNumber(request.getPhoneNumber());
        createdCustomer.setGender(request.getGender());
        createdCustomer.setCusName(request.getCusName());
        createdCustomer.setCreateDate(new Date()); // Add date as necessary
        createdCustomer.setPointAmount(0); // Default points
        createdCustomer.setStatus(true); // Default status

        when(customerRepository.save(any(Customer.class))).thenReturn(createdCustomer);

        CustomerResponse response = customerService.createCustomer(request);

        assertEquals(createdCustomer.getPK_CustomerID(), response.getPK_CustomerID());
        assertEquals(createdCustomer.getEmail(), response.getEmail());
        assertEquals(createdCustomer.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(createdCustomer.getGender(), response.getGender());
        assertEquals(createdCustomer.getCreateDate(), response.getCreateDate());
        assertEquals(createdCustomer.getPointAmount(), response.getPointAmount());
        assertEquals(createdCustomer.isStatus(), response.isStatus());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testGetCustomerRank() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setPK_CustomerID(customerId);
        customer.setPointAmount(500); // Example point amount for testing

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        String expectedRank = "Companion";
        String actualRank = customerService.getCustomerRank(customerId);

        assertEquals(expectedRank, actualRank);

        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    public void testUpdateCustomerDetails() {
        Long customerId = 1L;
        CustomerRequest request = new CustomerRequest();
        request.setPK_CustomerID(customerId);
        request.setEmail("updated@example.com");
        request.setPhoneNumber("9876543210");
        request.setGender("Female");
        request.setCusName("Updated Customer");

        Customer existingCustomer = new Customer();
        existingCustomer.setPK_CustomerID(customerId);
        existingCustomer.setEmail("old@example.com");
        existingCustomer.setPhoneNumber("1234567890");
        existingCustomer.setGender("Male");
        existingCustomer.setCusName("Old Customer");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        customerService.updateCustomerDetails(request);

        assertEquals(request.getEmail(), existingCustomer.getEmail());
        assertEquals(request.getPhoneNumber(), existingCustomer.getPhoneNumber());
        assertEquals(request.getGender(), existingCustomer.getGender());
        assertEquals(request.getCusName(), existingCustomer.getCusName());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    public void testDeleteCustomerById() {
        Long customerId = 1L;
        Customer existingCustomer = new Customer();
        existingCustomer.setPK_CustomerID(customerId);
        existingCustomer.setEmail("test@example.com");
        existingCustomer.setPhoneNumber("1234567890");
        existingCustomer.setGender("Male");
        existingCustomer.setCusName("Test Customer");
        existingCustomer.setStatus(true);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        customerService.deleteCustomerById(customerId);

        assertFalse(existingCustomer.isStatus());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    public void testReadAllCustomers() {
        List<Customer> customers = Arrays.asList(
                new Customer(1L, "test1@example.com", "1234567890", 0, true, "Male", new Date(), "Customer1", null, new HashSet<>(), null),
                new Customer(2L, "test2@example.com", "9876543210", 100, true, "Female", new Date(), "Customer2", null, new HashSet<>(), null)
        );

        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerResponse> responseList = customerService.readAllCustomers();

        assertEquals(customers.size(), responseList.size());
        assertEquals(customers.get(0).getEmail(), responseList.get(0).getEmail());
        assertEquals(customers.get(1).getPhoneNumber(), responseList.get(1).getPhoneNumber());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void testReadAllActiveCustomers() {
        List<Customer> activeCustomers = Arrays.asList(
                new Customer(1L, "test1@example.com", "1234567890", 0, true, "Male", new Date(), "Customer1", null, new HashSet<>(), null),
                new Customer(2L, "test2@example.com", "9876543210", 100, true, "Female", new Date(), "Customer2", null, new HashSet<>(), null)
        );

        when(customerRepository.findByStatus(true)).thenReturn(activeCustomers);

        List<CustomerResponse> responseList = customerService.readAllActiveCustomers();

        assertEquals(activeCustomers.size(), responseList.size());
        assertEquals(activeCustomers.get(0).getEmail(), responseList.get(0).getEmail());
        assertEquals(activeCustomers.get(1).getPhoneNumber(), responseList.get(1).getPhoneNumber());

        verify(customerRepository, times(1)).findByStatus(true);
    }

    @Test
    public void testUpdateCustomerPoints() {
        Long customerId = 1L;
        ViewCustomerPointRequest request = new ViewCustomerPointRequest();
        request.setPK_CustomerID(customerId);
        request.setPointAmount(500);

        Customer existingCustomer = new Customer();
        existingCustomer.setPK_CustomerID(customerId);
        existingCustomer.setPointAmount(300); // Existing points

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        customerService.updateCustomerPoints(request);

        assertEquals(request.getPointAmount(), existingCustomer.getPointAmount());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    public void testFindCustomersByKeyword_Id() {
        String keyword = "123";
        Long id = Long.parseLong(keyword);

        List<Customer> customers = Arrays.asList(
                new Customer(1L, "test1@example.com", "1234567890", 0, true, "Male", new Date(), "Customer1", null, new HashSet<>(), null),
                new Customer(2L, "test2@example.com", "9876543210", 100, true, "Female", new Date(), "Customer2", null, new HashSet<>(), null)
        );

        when(customerRepository.findCustomersByLongKeyword(id)).thenReturn(customers);

        List<CustomerSearchResponse> responseList = customerService.findCustomersByKeyword(keyword);

        assertEquals(customers.size(), responseList.size());
        assertEquals(customers.get(0).getEmail(), responseList.get(0).getEmail());
        assertEquals(customers.get(1).getPhoneNumber(), responseList.get(1).getPhoneNumber());

        verify(customerRepository, times(1)).findCustomersByLongKeyword(id);
    }

    @Test
    public void testFindCustomersByKeyword_String() {
        String keyword = "example";

        List<Customer> customers = Arrays.asList(
                new Customer(1L, "test1@example.com", "1234567890", 0, true, "Male", new Date(), "Customer1", null, new HashSet<>(), null),
                new Customer(2L, "test2@example.com", "9876543210", 100, true, "Female", new Date(), "Customer2", null, new HashSet<>(), null)
        );

        when(customerRepository.findCustomersByStringKeyword(keyword)).thenReturn(customers);

        List<CustomerSearchResponse> responseList = customerService.findCustomersByKeyword(keyword);

        assertEquals(customers.size(), responseList.size());
        assertEquals(customers.get(0).getEmail(), responseList.get(0).getEmail());
        assertEquals(customers.get(1).getPhoneNumber(), responseList.get(1).getPhoneNumber());

        verify(customerRepository, times(1)).findCustomersByStringKeyword(keyword);
    }
}