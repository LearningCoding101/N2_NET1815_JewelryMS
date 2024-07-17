package com.project.JewelryMS.controller;


import com.project.JewelryMS.entity.Customer;
import com.project.JewelryMS.model.Customer.*;
import com.project.JewelryMS.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/api/customer")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    // Create a new customer
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
        CustomerResponse customerResponse = customerService.createCustomer(createCustomerRequest);
        return ResponseEntity.ok(customerResponse);
    }

    // Get all customers
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<CustomerResponse>> readAllCustomer() {
        List<CustomerResponse> customerList = customerService.readAllCustomers();
        return ResponseEntity.ok(customerList);
    }

    // Get customer rank by ID
    @GetMapping("/{id}/rank")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> getCustomerRank(@PathVariable("id") Long id) {
        String rank = customerService.getCustomerRank(id);
        return ResponseEntity.ok(rank);
    }

    // Get all active customers
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<CustomerResponse>> readAllActiveCustomer() {
        List<CustomerResponse> customerList = customerService.readAllActiveCustomers();
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

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<CustomerSearchResponse>> findCustomersByCriteria(
            @RequestParam String keyword) {
        List<CustomerSearchResponse> customerList = customerService.findCustomersByKeyword(keyword);
        return ResponseEntity.ok(customerList);
    }
}

