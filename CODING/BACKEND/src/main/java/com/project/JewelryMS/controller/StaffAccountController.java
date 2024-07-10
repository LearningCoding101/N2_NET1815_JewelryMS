package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.model.Staff.DeleteStaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.service.StaffAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff-accounts") // Base URL path
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class StaffAccountController {

    @Autowired
    private StaffAccountService staffAccountService;

    // GET all staff accounts
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<StaffAccountResponse>> getAllStaffAccounts() {
        List<StaffAccountResponse> staffAccounts = staffAccountService.readAllStaffAccounts();
        return ResponseEntity.ok(staffAccounts);
    }

    // GET a staff account by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")

    public ResponseEntity<StaffAccountResponse> getStaffAccountById(@PathVariable Integer id) {
        StaffAccountResponse staffAccount = staffAccountService.getStaffAccountById(id);
        if (staffAccount != null) {
            return ResponseEntity.ok(staffAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT update an existing staff account
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> updateStaffAccount(@PathVariable Integer id, @RequestBody StaffAccountRequest staffAccountRequest) {
        String updatedStaffAccount = staffAccountService.updateStaffAccount(id, staffAccountRequest);
        return ResponseEntity.ok(updatedStaffAccount);
    }


    // DELETE deactivate a staff account
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> deactivateStaffAccount(@PathVariable Integer id) {
        staffAccountService.deactivateStaffAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/without-shift")
    public ResponseEntity<List<StaffAccountResponse>> getStaffWithoutShift() {
        List<StaffAccountResponse> staffWithoutShift = staffAccountService.getStaffWithoutShift();
        return ResponseEntity.ok(staffWithoutShift);
    }
}