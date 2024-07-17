package com.project.JewelryMS.UnitTest;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.enumClass.RoleEnum;
import com.project.JewelryMS.model.Manager.CreateManagerAccountRequest;
import com.project.JewelryMS.model.Manager.ManagerAccountRequest;
import com.project.JewelryMS.model.Manager.ManagerAccountResponse;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.ManagerAccountRepository;
import com.project.JewelryMS.service.ManagerAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManagerAccountServiceTest {

    @Mock
    private ManagerAccountRepository managerAccountRepository;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ManagerAccountService managerAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllManagerAccounts() {
        // Arrange
        Account account1 = new Account();
        account1.setPK_userID(1);
        account1.setEmail("manager1@example.com");
        account1.setAUsername("manager1");
        account1.setAccountName("Manager One");
        account1.setRole(RoleEnum.ROLE_MANAGER);
        account1.setStatus(1);

        Account account2 = new Account();
        account2.setPK_userID(2);
        account2.setEmail("manager2@example.com");
        account2.setAUsername("manager2");
        account2.setAccountName("Manager Two");
        account2.setRole(RoleEnum.ROLE_MANAGER);
        account2.setStatus(1);

        when(managerAccountRepository.findAllManagerAccounts()).thenReturn(Arrays.asList(account1, account2));

        // Act
        List<ManagerAccountResponse> result = managerAccountService.getAllManagerAccounts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("manager1@example.com", result.get(0).getEmail());
        assertEquals("manager2@example.com", result.get(1).getEmail());
    }

    @Test
    void getManagerAccountById() {
        // Arrange
        Account account = new Account();
        account.setPK_userID(1);
        account.setEmail("manager@example.com");
        account.setAUsername("manager");
        account.setAccountName("Manager");
        account.setRole(RoleEnum.ROLE_MANAGER);
        account.setStatus(1);

        when(managerAccountRepository.findManagerAccountById(1)).thenReturn(Optional.of(account));

        // Act
        ManagerAccountResponse result = managerAccountService.getManagerAccountById(1);

        // Assert
        assertNotNull(result);
        assertEquals("manager@example.com", result.getEmail());
    }

    @Test
    void createManagerAccount() {
        // Arrange
        CreateManagerAccountRequest request = new CreateManagerAccountRequest();
        request.setAccountName("New Manager");
        request.setEmail("newmanager@example.com");
        request.setAPassword("password");
        request.setUsername("newmanager");

        Account savedAccount = new Account();
        savedAccount.setPK_userID(1);
        savedAccount.setAccountName("New Manager");
        savedAccount.setEmail("newmanager@example.com");
        savedAccount.setAUsername("newmanager");
        savedAccount.setRole(RoleEnum.ROLE_MANAGER);
        savedAccount.setStatus(1);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(authenticationRepository.save(any(Account.class))).thenReturn(savedAccount);
        when(managerAccountRepository.findManagerAccountById(1)).thenReturn(Optional.of(savedAccount));

        // Act
        ManagerAccountResponse result = managerAccountService.createManagerAccount(request);

        // Assert
        assertNotNull(result);
        assertEquals("newmanager@example.com", result.getEmail());
        assertEquals("New Manager", result.getAccountName());
    }

    @Test
    void updateManagerAccount() {
        // Arrange
        Integer id = 1;
        ManagerAccountRequest request = new ManagerAccountRequest();
        request.setAccountName("Updated Manager");
        request.setEmail("updatedmanager@example.com");
        request.setUsername("updatedmanager");
        request.setRole(RoleEnum.ROLE_MANAGER);

        Account existingAccount = new Account();
        existingAccount.setPK_userID(1);
        existingAccount.setAccountName("Old Manager");
        existingAccount.setEmail("oldmanager@example.com");
        existingAccount.setAUsername("oldmanager");
        existingAccount.setRole(RoleEnum.ROLE_MANAGER);
        existingAccount.setStatus(1);

        when(managerAccountRepository.findManagerAccountById(id)).thenReturn(Optional.of(existingAccount));
        when(managerAccountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(authenticationRepository.save(any(Account.class))).thenReturn(existingAccount);

        // Act
        ManagerAccountResponse result = managerAccountService.updateManagerAccount(id, request);

        // Assert
        assertNotNull(result);
        assertEquals("updatedmanager@example.com", result.getEmail());
        assertEquals("Updated Manager", result.getAccountName());
    }

    @Test
    void deactivateManagerAccount() {
        // Arrange
        Integer id = 1;
        Account existingAccount = new Account();
        existingAccount.setPK_userID(1);
        existingAccount.setStatus(1);

        when(managerAccountRepository.findManagerAccountById(id)).thenReturn(Optional.of(existingAccount));
        when(managerAccountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(authenticationRepository.save(any(Account.class))).thenReturn(existingAccount);

        // Act
        String result = managerAccountService.deactivateManagerAccount(id);

        // Assert
        assertEquals("Delete Successfully", result);
        assertEquals(0, existingAccount.getStatus());
    }
}