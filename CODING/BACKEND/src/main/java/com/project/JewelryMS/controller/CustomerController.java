package com.project.JewelryMS.controller;


import com.project.JewelryMS.entity.Customer;
import com.project.JewelryMS.model.Customer.CreateCustomerRequest;
import com.project.JewelryMS.model.Customer.CustomerDeleteRequest;
import com.project.JewelryMS.model.Customer.CustomerRequest;
import com.project.JewelryMS.model.Customer.ViewCustomerPointRequest;
import com.project.JewelryMS.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/customer")
@SecurityRequirement(name = "api")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    // Create a new customer
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
        Customer customer = customerService.createCustomer(createCustomerRequest);
        return ResponseEntity.ok(customer);
    }

    // Get all customers
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<Customer>> readAllCustomer() {
        List<Customer> customerList = customerService.readAllCustomer();
        return ResponseEntity.ok(customerList);
    }

    // Get customer rank by ID
    @GetMapping("/{id}/rank")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> getCustomerRank(@PathVariable("id") Long id) {
        String rank = customerService.getCustomerRank(id);
        return ResponseEntity.ok(rank);
    }

    // Get a single customer by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Customer> readCustomerFromId(@RequestParam Long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // Get a single customer by phone number
    @GetMapping("/by-phone-number")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Customer> readCustomerFromPhoneNumber(@RequestParam String id) {
        Customer customer = customerService.getCustomerByPhoneNumber(id);
        return ResponseEntity.ok(customer);
    }

    // Get all active customers
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<Customer>> readAllActiveCustomer() {
        List<Customer> customerList = customerService.readAllActiveCustomers();
        return ResponseEntity.ok(customerList);
    }

    // Mark a customer as inactive
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer details marked as inactive successfully");
    }

    // Update customer details
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerRequest customerRequest) {
        customerService.updateCustomerDetails(customerRequest);
        return ResponseEntity.ok("Customer details updated successfully");
    }

    // Update customer points
    @PatchMapping("/{id}/points")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updateCustomerPoints(@RequestBody ViewCustomerPointRequest viewPointsRequest) {
        customerService.updateCustomerPoints(viewPointsRequest);
        return ResponseEntity.ok("Customer points updated successfully");
    }
}

