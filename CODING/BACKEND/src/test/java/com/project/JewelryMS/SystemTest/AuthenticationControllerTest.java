package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.AuthenticationAPI;
import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.model.AccountResponse;
import com.project.JewelryMS.model.LoginRequest;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationAPI authenticationAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByEmail() {
        String email = "test@example.com";
        Account mockAccount = new Account();
        mockAccount.setEmail(email);
        when(authenticationService.getAccount(email)).thenReturn(mockAccount);

        ResponseEntity<?> response = authenticationAPI.getByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccount, response.getBody());
    }

    @Test
    void testGetAll() {
        Account mockAccount = new Account();
        List<Account> mockAccounts = Collections.singletonList(mockAccount);
        when(authenticationService.getAllAccount()).thenReturn(mockAccounts);

        ResponseEntity<List<Account>> response = authenticationAPI.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccounts, response.getBody());
    }

    @Test
    void testRegister() {
        CreateStaffAccountRequest request = new CreateStaffAccountRequest();
        request.setEmail("test@example.com");
        when(authenticationService.handleRegisterCheckEmail(any())).thenReturn(true);
        when(authenticationService.register(request)).thenReturn(request);

        ResponseEntity<?> response = authenticationAPI.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account created", response.getBody());
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("phu");
        request.setPassword("phu");

        ResponseEntity<?> response = authenticationAPI.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
