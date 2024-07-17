package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.ProfileController;
import com.project.JewelryMS.model.Profile.*;
import com.project.JewelryMS.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateManagerProfile() {
        Long managerId = 1L;
        UpdateManager request = new UpdateManager();
        UpdateManager updatedProfile = new UpdateManager();
        when(profileService.updateManagerProfile(managerId, request)).thenReturn(updatedProfile);

        ResponseEntity<UpdateManager> response = profileController.updateManagerProfile(managerId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProfile, response.getBody());
    }

    @Test
    void viewAdminProfile() {
        Long adminId = 1L;
        AdminProfileResponse profile = new AdminProfileResponse();
        when(profileService.viewAdminProfile(adminId)).thenReturn(profile);

        ResponseEntity<AdminProfileResponse> response = profileController.viewAdminProfile(adminId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profile, response.getBody());
    }

    @Test
    void updateAdminProfile() {
        Long adminId = 1L;
        UpdateAdmin request = new UpdateAdmin();
        UpdateAdmin updatedProfile = new UpdateAdmin();
        when(profileService.updateAdminProfile(adminId, request)).thenReturn(updatedProfile);

        ResponseEntity<UpdateAdmin> response = profileController.updateAdminProfile(adminId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProfile, response.getBody());
    }

    @Test
    void updateStaffProfile() {
        Integer staffId = 1;
        UpdateStaff request = new UpdateStaff();
        UpdateStaff updatedProfile = new UpdateStaff();
        when(profileService.updateStaffProfile(staffId, request)).thenReturn(updatedProfile);

        ResponseEntity<UpdateStaff> response = profileController.updateStaffProfile(staffId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProfile, response.getBody());
    }

    @Test
    void getAllAccounts() {
        List<StaffListResponse> accounts = Arrays.asList(new StaffListResponse(), new StaffListResponse());
        when(profileService.getAllAccounts()).thenReturn(accounts);

        ResponseEntity<List<StaffListResponse>> response = profileController.getAllAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
    }
}