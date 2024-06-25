package com.project.JewelryMS;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.model.Manager.CreateManagerAccountRequest;
import com.project.JewelryMS.model.Manager.ManagerAccountRequest;
import com.project.JewelryMS.model.Manager.ManagerAccountResponse;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.ManagerAccountRepository;
import com.project.JewelryMS.service.ManagerAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManagerAccountServiceTest {

    @InjectMocks
    private ManagerAccountService managerAccountService;

    @Mock
    private ManagerAccountRepository managerAccountRepository;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setPK_userID(1);
        account.setEmail("manager@gmail.com");
        account.setAUsername("manager123");
        account.setAccountName("Manager Name");
        account.setRole(RoleEnum.valueOf("ROLE_MANAGER"));
        account.setStatus(1);
    }

    @Test
    public void testGetAllManagerAccounts() {
        when(managerAccountRepository.findAllManagerAccounts()).thenReturn(Collections.singletonList(account));

        List<ManagerAccountResponse> response = managerAccountService.getAllManagerAccounts();

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(managerAccountRepository, times(1)).findAllManagerAccounts();
    }

    @Test
    public void testGetManagerAccountById() {
        when(managerAccountRepository.findManagerAccountById(anyInt())).thenReturn(Optional.of(account));

        ManagerAccountResponse response = managerAccountService.getManagerAccountById(1);

        assertNotNull(response);
        assertEquals("manager@gmail.com", response.getEmail());
        verify(managerAccountRepository, times(1)).findManagerAccountById(anyInt());
    }

    @Test
    public void testCreateManagerAccount() {
        CreateManagerAccountRequest createManagerAccountRequest = new CreateManagerAccountRequest();
        createManagerAccountRequest.setAccountName("Manager Name");
        createManagerAccountRequest.setEmail("manager@gmail.com");
        createManagerAccountRequest.setAPassword("123");
        createManagerAccountRequest.setAUsername("manager123");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(authenticationRepository.save(any(Account.class))).thenReturn(account);
        when(managerAccountRepository.findManagerAccountById(anyInt())).thenReturn(Optional.of(account));

        ManagerAccountResponse response = managerAccountService.createManagerAccount(createManagerAccountRequest);

        assertNotNull(response);
        assertEquals("manager@gmail.com", response.getEmail());
        verify(authenticationRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testUpdateManagerAccount() {
        ManagerAccountRequest managerAccountRequest = new ManagerAccountRequest();
        managerAccountRequest.setAccountName("Updated Manager Name");
        managerAccountRequest.setEmail("updated@gmail.com");
        managerAccountRequest.setAPassword("123");
        managerAccountRequest.setAUsername("updatedManager123");
        managerAccountRequest.setRole(RoleEnum.valueOf("ROLE_MANAGER"));

        when(managerAccountRepository.findManagerAccountById(anyInt())).thenReturn(Optional.of(account));
        when(managerAccountRepository.findById(anyInt())).thenReturn(Optional.of(account));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
        when(authenticationRepository.save(any(Account.class))).thenReturn(account);

        ManagerAccountResponse response = managerAccountService.updateManagerAccount(1, managerAccountRequest);

        assertNotNull(response);
        assertEquals("updated@gmail.com", response.getEmail());
        verify(managerAccountRepository, times(1)).findById(anyInt());
        verify(authenticationRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testDeactivateManagerAccount() {
        when(managerAccountRepository.findManagerAccountById(anyInt())).thenReturn(Optional.of(account));
        when(managerAccountRepository.findById(anyInt())).thenReturn(Optional.of(account));
        when(authenticationRepository.save(any(Account.class))).thenReturn(account);

        String response = managerAccountService.deactivateManagerAccount(1);

        assertEquals("Delete Successfully", response);
        assertEquals(0, account.getStatus());
        verify(managerAccountRepository, times(1)).findById(anyInt());
        verify(authenticationRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testGetManagerAccountById_NotFound() {
        when(managerAccountRepository.findManagerAccountById(anyInt())).thenReturn(Optional.empty());

        ManagerAccountResponse response = managerAccountService.getManagerAccountById(1);

        assertNull(response);
    }

    @Test
    public void testUpdateManagerAccount_NotFound() {
        ManagerAccountRequest managerAccountRequest = new ManagerAccountRequest();
        managerAccountRequest.setAccountName("Updated Manager Name");
        managerAccountRequest.setEmail("updated@gmail.com");
        managerAccountRequest.setAPassword("123");
        managerAccountRequest.setAUsername("updatedManager123");
        managerAccountRequest.setRole(RoleEnum.valueOf("ROLE_MANAGER"));

        when(managerAccountRepository.findManagerAccountById(anyInt())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                managerAccountService.updateManagerAccount(1, managerAccountRequest));

        assertEquals("ManagerAccount with ID 1 not found", exception.getMessage());
    }

    @Test
    public void testDeactivateManagerAccount_NotFound() {
        when(managerAccountRepository.findManagerAccountById(anyInt())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                managerAccountService.deactivateManagerAccount(1));

        assertEquals("ManagerAccount with ID 1 not found", exception.getMessage());
    }
}
