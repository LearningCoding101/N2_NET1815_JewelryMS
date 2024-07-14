package com.project.JewelryMS.SystemTest;


import com.project.JewelryMS.controller.CustomerController;
import com.project.JewelryMS.model.Customer.CreateCustomerRequest;
import com.project.JewelryMS.model.Customer.CustomerResponse;
import com.project.JewelryMS.model.Customer.CustomerSearchResponse;
import com.project.JewelryMS.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        CustomerResponse mockResponse = new CustomerResponse();
        when(customerService.createCustomer(any(CreateCustomerRequest.class))).thenReturn(mockResponse);

        ResponseEntity<CustomerResponse> response = customerController.createCustomer(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testReadAllCustomer() {
        List<CustomerResponse> mockResponse = new ArrayList<>();
        when(customerService.readAllCustomers()).thenReturn(mockResponse);

        ResponseEntity<List<CustomerResponse>> response = customerController.readAllCustomer();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetCustomerRank() {
        String mockRank = "Gold";
        when(customerService.getCustomerRank(anyLong())).thenReturn(mockRank);

        ResponseEntity<String> response = customerController.getCustomerRank(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRank, response.getBody());
    }

    @Test
    void testFindCustomersByCriteria() {
        List<CustomerSearchResponse> mockResponse = new ArrayList<>();
        when(customerService.findCustomersByKeyword(anyString())).thenReturn(mockResponse);

        ResponseEntity<List<CustomerSearchResponse>> response = customerController.findCustomersByCriteria("John");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }
}