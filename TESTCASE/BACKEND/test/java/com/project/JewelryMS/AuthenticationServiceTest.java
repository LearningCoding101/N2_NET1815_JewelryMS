package com.project.JewelryMS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.model.*;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import com.project.JewelryMS.service.AuthenticationService;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.JWTservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
public class AuthenticationServiceTest {
    @Mock
    private EmailService emailService;

    @Mock
    private StaffAccountRepository staffAccountRepository;

    @Mock
    private JWTservice jwTservice;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        CreateStaffAccountRequest request = new CreateStaffAccountRequest();
        request.setAccountName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password");
        request.setUsername("john_doe");
        request.setSalary(5000.0F);
        request.setPhoneNumber("1234567890");
        request.setStartDate(new Date(2023, 06,20));


        when(passwordEncoder.encode(any(String.class))).thenReturn("encoded_password");

        CreateStaffAccountRequest result = authenticationService.register(request);

        assertEquals("John Doe", result.getAccountName());
        assertEquals("john@example.com", result.getEmail());
        verify(authenticationRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testHandleRegisterCheckEmail_Valid() {
        CreateStaffAccountRequest request = new CreateStaffAccountRequest();
        request.setEmail("valid@example.com");

        when(emailService.validEmail("valid@example.com")).thenReturn(true);

        boolean result = authenticationService.handleRegisterCheckEmail(request);

        assertTrue(result);
    }

    @Test
    void testHandleRegisterCheckEmail_Invalid() {
        CreateStaffAccountRequest request = new CreateStaffAccountRequest();
        request.setEmail("invalidexample.com");

        when(emailService.validEmail("invalidexample.com")).thenReturn(false);

        boolean result = authenticationService.handleRegisterCheckEmail(request);

        assertFalse(result);
    }

    @Test
    void testGetAllAccount() {
        authenticationService.getAllAccount();
        verify(authenticationRepository, times(1)).findAll();
    }

    @Test
    void testGetAccount() {
        String email = "hoanghtse181747@fpt.edu.vn";
        authenticationService.getAccount(email);
        verify(authenticationRepository, times(1)).findAccountByemail(email);
    }

    @Test
    void testGetAccountById() {
        Long id = 85L;
        authenticationService.getAccountById(id);
        verify(authenticationRepository, times(1)).findAccountById(id);
    }

    @Test
    void testSendForgotPasswordEmail_Success() {
        String email = "hoanghtse181747@fpt.edu.vn";
        Account account = new Account();
        account.setEmail(email);

        when(authenticationRepository.findAccountByemail(email)).thenReturn(account);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encoded_temp_password");

        String result = authenticationService.sendForgotPasswordEmail(email);

        assertEquals("Successfully sent new temporary password", result);
    }

    @Test
    void testSendForgotPasswordEmail_Fail() {
        String email = "unknown@example.com";

        when(authenticationRepository.findAccountByemail(email)).thenReturn(null);

        String result = authenticationService.sendForgotPasswordEmail(email);

        assertEquals("Is your email correct?", result);
    }

    @Test
    void testLogin_Success() {
        String username = "vy";
        String password = "vy";

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        Account account = new Account();
        account.setAUsername(username);
        account.setRole(RoleEnum.ROLE_STAFF);
        account.setPK_userID(1);

        when(authenticationRepository.findAccountByUsername(username)).thenReturn(account);
        when(jwTservice.generateToken(account)).thenReturn("jwt_token");

        AccountResponse response = authenticationService.login(request);

        assertEquals(username, response.getUsername());
        assertEquals("jwt_token", response.getToken());
        assertEquals(RoleEnum.ROLE_STAFF, response.getRole());
    }

    @Test
    void testChangePassword_Success() {
        Long id = 85L;
        String oldPassword = "vy";
        String newPassword = "string";

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(oldPassword);
        request.setNewPassword(newPassword);

        Account account = new Account();
        account.setAPassword("encoded_old_password");

        when(authenticationRepository.findById(id)).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(oldPassword, account.getAPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encoded_new_password");

        ResponseEntity<String> response = authenticationService.changePassword(request, id);

        assertEquals("Password changed successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testChangePassword_Fail_OldPasswordMismatch() {
        Long id = 1L;
        String oldPassword = "old_password";
        String newPassword = "new_password";

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(oldPassword);
        request.setNewPassword(newPassword);

        Account account = new Account();
        account.setAPassword("encoded_old_password");

        when(authenticationRepository.findById(id)).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(oldPassword, account.getAPassword())).thenReturn(false);

        ResponseEntity<String> response = authenticationService.changePassword(request, id);

        assertEquals("Old password does not match", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testLoadUserByUsername_Success() {
        String username = "vy";
        Account account = new Account();
        account.setAUsername(username);

        when(authenticationRepository.findAccountByUsername(username)).thenReturn(account);

        UserDetails userDetails = authenticationService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_Fail() {
        String username = "unknown_user";

        when(authenticationRepository.findAccountByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.loadUserByUsername(username);
        });
    }

    @Test
    void testGetAllManagerAccount() {
        authenticationService.getAllManagerAccount();
        verify(authenticationRepository, times(1)).findByRole(RoleEnum.ROLE_MANAGER);
    }
}
