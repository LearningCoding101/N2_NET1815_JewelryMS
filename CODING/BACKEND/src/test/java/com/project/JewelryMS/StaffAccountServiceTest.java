package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StaffAccountServiceTest {

    @Mock
    private StaffAccountRepository staffAccountRepository;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StaffAccountService staffAccountService;

    private StaffAccount staffAccount;
    private Account account;

    @BeforeEach
    public void setup() {
        account = new Account();
        account.setPK_userID(1);
        account.setAccountName("John Doe");
        account.setEmail("john.doe@example.com");
        account.setAUsername("johndoe");
        account.setAPassword("password");
        account.setRole(RoleEnum.ROLE_STAFF);
        account.setStatus(1);

        staffAccount = new StaffAccount();
        staffAccount.setStaffID(1);
        staffAccount.setPhoneNumber("1234567890");
        staffAccount.setSalary(1000.0F);
        staffAccount.setStartDate( new Date());
        staffAccount.setAccount(account);
    }

    @Test
    public void testReadAllStaffAccounts() {
        when(staffAccountRepository.findAllStaffAccountsByRoleStaff()).thenReturn(Arrays.asList(staffAccount));

        List<StaffAccountResponse> response = staffAccountService.readAllStaffAccounts();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("1234567890", response.get(0).getPhoneNumber());

        verify(staffAccountRepository, times(1)).findAllStaffAccountsByRoleStaff();
    }

    @Test
    public void testGetStaffAccountById() {
        when(staffAccountRepository.findIDStaffAccount(1)).thenReturn(Optional.of(staffAccount));

        StaffAccountResponse response = staffAccountService.getStaffAccountById(1);

        assertNotNull(response);
        assertEquals("1234567890", response.getPhoneNumber());

        verify(staffAccountRepository, times(1)).findIDStaffAccount(1);
    }

    @Test
    public void testUpdateStaffAccount() {
        StaffAccountRequest request = new StaffAccountRequest();
        request.setPhoneNumber("0987654321");
        request.setSalary(2000.0F);
        request.setStartDate(new Date());
        request.setEmail("john.doe@newemail.com");
        request.setUsername("newjohndoe");
        request.setPassword("newpassword");
        request.setAccountName("John Doe Updated");
        request.setRole(RoleEnum.ROLE_ADMIN);

        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(staffAccount));
        when(authenticationRepository.findById(1L)).thenReturn(Optional.of(account));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        String result = staffAccountService.updateStaffAccount(1, request);

        assertEquals("Update Staff Successfully", result);
        assertEquals("0987654321", staffAccount.getPhoneNumber());
        assertEquals(2000.0, staffAccount.getSalary());

        verify(staffAccountRepository, times(1)).findById(1);
        verify(authenticationRepository, times(1)).findById(1L);
        verify(authenticationRepository, times(1)).save(any(Account.class));
        verify(staffAccountRepository, times(1)).save(any(StaffAccount.class));
    }

    @Test
    public void testDeactivateStaffAccount() {
        // Arrange
        StaffAccount staffAccount = new StaffAccount();
        Account account = new Account();
        staffAccount.setAccount(account);

        when(staffAccountRepository.findById(40)).thenReturn(Optional.of(staffAccount));

        // Act
        boolean deactivated = staffAccountService.deactivateStaffAccount(40);

        // Assert
        assertTrue(deactivated, "Expected the account to be successfully deactivated");

        verify(staffAccountRepository, times(1)).findById(40);
        verify(authenticationRepository, times(1)).save(account);
    }
    @Test
    public void testDeactivateStaffAccount_NotFound() {
        // Arrange
        when(staffAccountRepository.findById(1)).thenReturn(Optional.empty()); // Simulating not found scenario

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                staffAccountService.deactivateStaffAccount(1));

        assertEquals("StaffAccount with ID 1 not found", exception.getMessage());

        // Optionally, verify interaction with mocks
        verify(staffAccountRepository, times(1)).findById(1);
        verify(staffAccountRepository, never()).save(any()); // No save should be called if account not found
    }
}