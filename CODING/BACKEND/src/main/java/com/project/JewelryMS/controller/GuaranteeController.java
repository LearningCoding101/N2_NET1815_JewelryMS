package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Guarantee.CreateGuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeResponse;
import com.project.JewelryMS.service.GuaranteeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guarantee")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class GuaranteeController {

    @Autowired
    private GuaranteeService guaranteeService;

    // Create a new guarantee
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<GuaranteeResponse> createGuarantee(@RequestBody CreateGuaranteeRequest createGuaranteeRequest) {
        GuaranteeResponse guarantee = guaranteeService.createGuarantee(createGuaranteeRequest);
        return ResponseEntity.ok(guarantee);
    }

    // Get all guarantees
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeResponse>> readAllGuarantees() {
        List<GuaranteeResponse> guaranteeList = guaranteeService.readAllGuaranteeResponses();
        return ResponseEntity.ok(guaranteeList);
    }

    // Get a single guarantee by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<GuaranteeResponse> readGuaranteeById(@PathVariable Long id) {
        GuaranteeResponse guarantee = guaranteeService.getGuaranteeResponseById(id);
        return ResponseEntity.ok(guarantee);
    }

    // Get all active guarantees
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeResponse>> readAllActiveGuarantees() {
        List<GuaranteeResponse> activeGuarantees = guaranteeService.readAllActiveGuaranteePolicies();
        return ResponseEntity.ok(activeGuarantees);
    }

    // Get all inactive guarantees
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeResponse>> readAllInactiveGuarantees() {
        List<GuaranteeResponse> inactiveGuarantees = guaranteeService.readAllInactiveGuaranteePolicies();
        return ResponseEntity.ok(inactiveGuarantees);
    }

    // Get guarantees by policy type
    @GetMapping("/by-policy-type")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeResponse>> readGuaranteesByPolicyType(@RequestParam String policyType) {
        List<GuaranteeResponse> guaranteesByPolicyType = guaranteeService.readAllGuaranteesByPolicyType(policyType);
        return ResponseEntity.ok(guaranteesByPolicyType);
    }

    // Get guarantees by coverage
    @GetMapping("/by-coverage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeResponse>> readGuaranteesByCoverage(@RequestParam String coverage) {
        List<GuaranteeResponse> guaranteesByCoverage = guaranteeService.readAllGuaranteesByCoverage(coverage);
        return ResponseEntity.ok(guaranteesByCoverage);
    }

    // Get guarantee by product ID
    @GetMapping("/by-product-id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<GuaranteeResponse> readGuaranteeByProductId(@RequestParam Long productId) {
        GuaranteeResponse guarantee = guaranteeService.readGuaranteeByProductId(productId);
        return ResponseEntity.ok(guarantee);
    }

    // Mark a guarantee as inactive
    @PatchMapping("/{id}/delete-status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deleteGuarantee(@PathVariable Long id) {
        guaranteeService.deleteGuaranteePolicyById(id);
        return ResponseEntity.ok("Guarantee policy details marked as inactive successfully");
    }

    // Update guarantee details
    @PutMapping("/update-details")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updateGuaranteeDetails(@RequestBody GuaranteeRequest guaranteeRequest) {
        guaranteeService.updateGuaranteeDetails(guaranteeRequest);
        return ResponseEntity.ok("Guarantee policy details updated successfully");
    }

    // Apply a guarantee to a product
//    @PostMapping("/{id}/apply-to-product")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
//    public ResponseEntity<String> applyGuaranteeToProduct(@PathVariable Long id, @RequestParam Long productId) {
//        guaranteeService.applyGuaranteeToProduct(id, productId);
//        return ResponseEntity.ok("Guarantee applied to product successfully");
//    }

}