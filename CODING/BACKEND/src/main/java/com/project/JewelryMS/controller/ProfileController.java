package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Profile.*;
import com.project.JewelryMS.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UpdateManager> updateManagerProfile(@PathVariable Long managerId,
                                                              @RequestBody UpdateManager updateManagerRequest) {
        UpdateManager updatedProfile = profileService.updateManagerProfile(managerId, updateManagerRequest);
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
    public ResponseEntity<UpdateAdmin> updateAdminProfile(@PathVariable Long adminId,
                                                          @RequestBody UpdateAdmin updateAdminRequest) {
        UpdateAdmin updatedProfile = profileService.updateAdminProfile(adminId, updateAdminRequest);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/staff/{staffId}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<UpdateStaff> updateStaffProfile(@PathVariable Integer staffId,
                                                          @RequestBody UpdateStaff updateStaffRequest) {
        UpdateStaff updatedProfile = profileService.updateStaffProfile(staffId, updateStaffRequest);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profile/all")
    public ResponseEntity<List<StaffListResponse>> getAllAccounts() {
        List<StaffListResponse> accounts = profileService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}
