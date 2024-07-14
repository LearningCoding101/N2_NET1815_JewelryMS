package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.StaffAccountController;
import com.project.JewelryMS.model.Staff.StaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.service.StaffAccountService;
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

class StaffAccountControllerTest {

    @Mock
    private StaffAccountService staffAccountService;

    @InjectMocks
    private StaffAccountController staffAccountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStaffAccounts() {
        List<StaffAccountResponse> staffAccounts = Arrays.asList(new StaffAccountResponse(), new StaffAccountResponse());
        when(staffAccountService.readAllStaffAccounts()).thenReturn(staffAccounts);

        ResponseEntity<List<StaffAccountResponse>> response = staffAccountController.getAllStaffAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(staffAccounts, response.getBody());
    }

    @Test
    void getStaffAccountById() {
        Integer id = 1;
        StaffAccountResponse staffAccount = new StaffAccountResponse();
        when(staffAccountService.getStaffAccountById(id)).thenReturn(staffAccount);

        ResponseEntity<StaffAccountResponse> response = staffAccountController.getStaffAccountById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(staffAccount, response.getBody());
    }

    @Test
    void updateStaffAccount() {
        Integer id = 1;
        StaffAccountRequest request = new StaffAccountRequest();
        String updatedMessage = "Staff account updated successfully";
        when(staffAccountService.updateStaffAccount(id, request)).thenReturn(updatedMessage);

        ResponseEntity<String> response = staffAccountController.updateStaffAccount(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMessage, response.getBody());
    }

    @Test
    void deactivateStaffAccount() {
        Integer id = 1;

        ResponseEntity<Void> response = staffAccountController.deactivateStaffAccount(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(staffAccountService).deactivateStaffAccount(id);
    }

    @Test
    void getStaffWithoutShift() {
        List<StaffAccountResponse> staffWithoutShift = Arrays.asList(new StaffAccountResponse(), new StaffAccountResponse());
        when(staffAccountService.getStaffWithoutShift()).thenReturn(staffWithoutShift);

        ResponseEntity<List<StaffAccountResponse>> response = staffAccountController.getStaffWithoutShift();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(staffWithoutShift, response.getBody());
    }
}