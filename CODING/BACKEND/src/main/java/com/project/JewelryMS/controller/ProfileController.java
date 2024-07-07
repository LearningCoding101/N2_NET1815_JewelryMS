package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Profile.*;
import com.project.JewelryMS.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @PutMapping("/managers/{managerId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<UpdateManagerResponse> updateManagerProfile(@PathVariable Long managerId,
                                                                      @RequestBody UpdateManagerResponse updateManagerRequest) {
        UpdateManagerResponse updatedProfile = profileService.updateManagerProfile(managerId, updateManagerRequest);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admins/{adminId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AdminProfileResponse> viewAdminProfile(@PathVariable Long adminId) {
        AdminProfileResponse profile = profileService.viewAdminProfile(adminId);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/admins/{adminId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateAdminResponse> updateAdminProfile(@PathVariable Long adminId,
                                                                  @RequestBody UpdateAdminResponse updateAdminRequest) {
        UpdateAdminResponse updatedProfile = profileService.updateAdminProfile(adminId, updateAdminRequest);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/staff/{staffId}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<UpdateStaffResponse> updateStaffProfile(@PathVariable Integer staffId,
                                                                  @RequestBody UpdateStaffResponse updateStaffRequest) {
        UpdateStaffResponse updatedProfile = profileService.updateStaffProfile(staffId, updateStaffRequest);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<StaffListResponse>> getAllAccounts() {
        List<StaffListResponse> accounts = profileService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}
