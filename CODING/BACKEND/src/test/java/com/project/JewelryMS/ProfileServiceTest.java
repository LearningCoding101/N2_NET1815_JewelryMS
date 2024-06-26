package com.project.JewelryMS;


import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Profile.*;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import com.project.JewelryMS.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private StaffAccountRepository staffAccountRepository;

    private Account managerAccount;
    private Account adminAccount;
    private StaffAccount staffAccount;

    @BeforeEach
    void setUp() {
        managerAccount = new Account();
        managerAccount.setPK_userID(1);
        managerAccount.setRole(RoleEnum.ROLE_MANAGER);
        managerAccount.setEmail("manager@example.com");
        managerAccount.setAUsername("manager");
        managerAccount.setAccountName("Manager Name");
        managerAccount.setStatus(1);

        adminAccount = new Account();
        adminAccount.setPK_userID(2);
        adminAccount.setRole(RoleEnum.ROLE_ADMIN);
        adminAccount.setEmail("admin@example.com");
        adminAccount.setAUsername("admin");
        adminAccount.setAccountName("Admin Name");
        adminAccount.setStatus(1);

        staffAccount = new StaffAccount();
        staffAccount.setStaffID(1);
        staffAccount.setAccount(managerAccount);
        staffAccount.setStartDate(new Date(System.currentTimeMillis()));
        staffAccount.setPhoneNumber("1234567890");
        staffAccount.setSalary(50000);
    }

    @Test
    void testViewManagerProfile() {
        when(authenticationRepository.findById(1L)).thenReturn(Optional.of(managerAccount));

        ManagerProfileResponse response = profileService.viewManagerProfile(1L);

        assertNotNull(response);
        assertEquals(RoleEnum.ROLE_MANAGER, response.getRole());
        assertEquals("manager@example.com", response.getEmail());
        assertEquals("manager", response.getUsername());
        assertEquals("Manager Name", response.getAccountName());
        assertEquals(1, response.getStatus());
    }

    @Test
    void testUpdateManagerProfile() {
        UpdateManagerResponse updateRequest = new UpdateManagerResponse();
        updateRequest.setEmail("newmanager@example.com");
        updateRequest.setUsername("newmanager");
        updateRequest.setAccountName("New Manager Name");

        when(authenticationRepository.findById(1L)).thenReturn(Optional.of(managerAccount));

        UpdateManagerResponse response = profileService.updateManagerProfile(1L, updateRequest);

        assertNotNull(response);
        assertEquals("newmanager@example.com", response.getEmail());
        assertEquals("newmanager", response.getUsername());
        assertEquals("New Manager Name", response.getAccountName());

        verify(authenticationRepository, times(1)).save(managerAccount);
    }

    @Test
    void testViewAdminProfile() {
        when(authenticationRepository.findById(2L)).thenReturn(Optional.of(adminAccount));

        AdminProfileResponse response = profileService.viewAdminProfile(2L);

        assertNotNull(response);
        assertEquals(RoleEnum.ROLE_ADMIN, response.getRole());
        assertEquals("admin@example.com", response.getEmail());
        assertEquals("admin", response.getUsername());
        assertEquals("Admin Name", response.getAccountName());
        assertEquals(1, response.getStatus());
    }

    @Test
    void testUpdateAdminProfile() {
        UpdateAdminResponse updateRequest = new UpdateAdminResponse();
        updateRequest.setEmail("newadmin@example.com");
        updateRequest.setUsername("newadmin");
        updateRequest.setAccountname("New Admin Name");

        when(authenticationRepository.findById(2L)).thenReturn(Optional.of(adminAccount));

        UpdateAdminResponse response = profileService.updateAdminProfile(2L, updateRequest);

        assertNotNull(response);
        assertEquals("newadmin@example.com", response.getEmail());
        assertEquals("newadmin", response.getUsername());
        assertEquals("New Admin Name", response.getAccountname());

        verify(authenticationRepository, times(1)).save(adminAccount);
    }

    @Test
    void testViewStaffProfile() {
        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(staffAccount));

        StaffProfileResponse response = profileService.viewStaffProfile(1);

        assertNotNull(response);
        assertEquals(RoleEnum.ROLE_MANAGER, response.getRole());
        assertEquals("manager@example.com", response.getEmail());
        assertEquals("manager", response.getUsername());
        assertEquals("Manager Name", response.getAccountName());
        assertEquals(staffAccount.getStartDate(), response.getStartDate());
        assertEquals("1234567890", response.getPhone());
        assertEquals(50000, response.getSalary());
        assertEquals(1, response.getStatus());
    }

    @Test
    void testUpdateStaffProfile() {
        UpdateStaffResponse updateRequest = new UpdateStaffResponse();
        updateRequest.setEmail("newstaff@example.com");
        updateRequest.setUsername("newstaff");
        updateRequest.setAccountname("New Staff Name");
        updateRequest.setPhone("0987654321");

        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(staffAccount));

        UpdateStaffResponse response = profileService.updateStaffProfile(1, updateRequest);

        assertNotNull(response);
        assertEquals("newstaff@example.com", response.getEmail());
        assertEquals("newstaff", response.getUsername());
        assertEquals("New Staff Name", response.getAccountname());
        assertEquals("0987654321", response.getPhone());

        verify(staffAccountRepository, times(1)).save(staffAccount);
    }
}
