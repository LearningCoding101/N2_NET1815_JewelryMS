package com.project.JewelryMS;


import com.project.JewelryMS.entity.Customer;
import com.project.JewelryMS.model.Customer.CreateCustomerRequest;
import com.project.JewelryMS.model.Customer.CustomerRequest;
import com.project.JewelryMS.model.OrderDetail.CalculatePointsRequest;
import com.project.JewelryMS.model.Customer.ViewCustomerPointRequest;
import com.project.JewelryMS.repository.CustomerRepository;
import com.project.JewelryMS.repository.OrderDetailRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductSellRepository productSellRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CreateCustomerRequest createCustomerRequest;
    private CustomerRequest customerRequest;
    private CalculatePointsRequest calculatePointsRequest;
    private ViewCustomerPointRequest viewCustomerPointRequest;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPhoneNumber("1234567890");
        customer.setGender("Male");
        customer.setPointAmount(100);
        customer.setStatus(true);

        createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setEmail("new@example.com");
        createCustomerRequest.setPhoneNumber("0987654321");
        createCustomerRequest.setGender("Female");

        customerRequest = new CustomerRequest();
        customerRequest.setPK_CustomerID(1L);
        customerRequest.setEmail("updated@example.com");
        customerRequest.setPhoneNumber("1112223333");
        customerRequest.setGender("Female");

        calculatePointsRequest = new CalculatePointsRequest();
        calculatePointsRequest.setTotal(2000000F);

        viewCustomerPointRequest = new ViewCustomerPointRequest();
        viewCustomerPointRequest.setPK_CustomerID(1L);
        viewCustomerPointRequest.setPointAmount(500);
    }

    @Test
    public void testCalculateAndUpdatePoints() {
        Integer points = customerService.calculateAndUpdatePoints(calculatePointsRequest);
        assertEquals(2, points);
    }

    @Test
    public void testGetCustomerRank() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        String rank = customerService.getCustomerRank(1L);
        assertEquals("Member", rank);

        customer.setPointAmount(50);
        rank = customerService.getCustomerRank(1L);
        assertEquals("Connect", rank);

        customer.setPointAmount(500);
        rank = customerService.getCustomerRank(1L);
        assertEquals("Companion", rank);

        customer.setPointAmount(1000);
        rank = customerService.getCustomerRank(1L);
        assertEquals("Intimate", rank);

        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        rank = customerService.getCustomerRank(2L);
        assertEquals("Not Found Customer ID", rank);
    }

    @Test
    public void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer createdCustomer = customerService.createCustomer(createCustomerRequest);

        assertNotNull(createdCustomer);
        assertEquals(customer.getEmail(), createdCustomer.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testGetCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getCustomerById(1L);

        assertNotNull(foundCustomer);
        assertEquals(customer.getEmail(), foundCustomer.getEmail());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCustomerByPhoneNumber() {
        when(customerRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getCustomerByPhoneNumber("1234567890");

        assertNotNull(foundCustomer);
        assertEquals(customer.getEmail(), foundCustomer.getEmail());
        verify(customerRepository, times(1)).findByPhoneNumber("1234567890");
    }

    @Test
    public void testReadAllCustomer() {
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> foundCustomers = customerService.readAllCustomer();

        assertNotNull(foundCustomers);
        assertFalse(foundCustomers.isEmpty());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateCustomerDetails() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.updateCustomerDetails(customerRequest);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(customer);
        assertEquals(customerRequest.getEmail(), customer.getEmail());
        assertEquals(customerRequest.getPhoneNumber(), customer.getPhoneNumber());
        assertEquals(customerRequest.getGender(), customer.getGender());
    }

    @Test
    public void testDeleteCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomerById(1L);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(customer);
        assertFalse(customer.isStatus());
    }

    @Test
    public void testUpdateCustomerPoints() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.updateCustomerPoints(viewCustomerPointRequest);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(customer);
        assertEquals(viewCustomerPointRequest.getPointAmount(), customer.getPointAmount());
    }

    @Test
    public void testReadAllActiveCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerRepository.findByStatus(true)).thenReturn(customers);

        List<Customer> activeCustomers = customerService.readAllActiveCustomers();

        assertNotNull(activeCustomers);
        assertFalse(activeCustomers.isEmpty());
        verify(customerRepository, times(1)).findByStatus(true);
    }


}