//package com.project.JewelryMS.UnitTest;
//import com.project.JewelryMS.entity.Account;
//import com.project.JewelryMS.entity.Shift;
//import com.project.JewelryMS.entity.StaffAccount;
//import com.project.JewelryMS.enumClass.RoleEnum;
//import com.project.JewelryMS.model.Staff.StaffAccountRequest;
//import com.project.JewelryMS.model.Staff.StaffAccountResponse;
//import com.project.JewelryMS.repository.AuthenticationRepository;
//import com.project.JewelryMS.repository.ShiftRepository;
//import com.project.JewelryMS.repository.StaffAccountRepository;
//import com.project.JewelryMS.service.StaffAccountService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.LocalDate;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class StaffAccountServiceUnitTest {
//
//    @Mock
//    private StaffAccountRepository staffAccountRepository;
//
//    @Mock
//    private AuthenticationRepository authenticationRepository;
//
//    @Mock
//    private ShiftRepository shiftRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private StaffAccountService staffAccountService;
//
//    private StaffAccount staffAccount;
//    private Account account;
//    private StaffAccountRequest staffAccountRequest;
//
//    @BeforeEach
//    public void setUp() {
//        account = new Account();
//        account.setPK_userID(1);
//        account.setEmail("test@example.com");
//        account.setAUsername("testUser");
//        account.setAccountName("Test Account");
//        account.setRole(RoleEnum.valueOf("ROLE_STAFF"));
//        account.setStatus(1);
//
//        staffAccount = new StaffAccount();
//        staffAccount.setStaffID(1);
//        staffAccount.setPhoneNumber("1234567890");
//        staffAccount.setSalary(15000000);
//        staffAccount.setStartDate(LocalDate.parse("2024-07-10"));
//        staffAccount.setAccount(account);
//
//        staffAccountRequest = new StaffAccountRequest();
//        staffAccountRequest.setPhoneNumber("0987654321");
//        staffAccountRequest.setSalary(16000000);
//        staffAccountRequest.setStartDate(LocalDate.parse("2024-07-11"));
//        staffAccountRequest.setEmail("updated@example.com");
//        staffAccountRequest.setUsername("updatedUser");
//        staffAccountRequest.setAccountName("Updated Account");
//        staffAccountRequest.setRole(RoleEnum.valueOf("ROLE_MANAGER"));
//    }
//
//    @Test
//    public void testReadAllStaffAccounts() {
//        when(staffAccountRepository.findAllStaffAccountsByRoleStaff()).thenReturn(Collections.singletonList(staffAccount));
//
//        List<StaffAccountResponse> responses = staffAccountService.readAllStaffAccounts();
//
//        assertNotNull(responses);
//        assertEquals(1, responses.size());
//        assertEquals("testUser", responses.get(0).getUsername());
//        verify(staffAccountRepository, times(1)).findAllStaffAccountsByRoleStaff();
//    }
//
//    @Test
//    public void testGetStaffAccountById() {
//        when(staffAccountRepository.findIDStaffAccount(anyInt())).thenReturn(Optional.of(staffAccount));
//
//        StaffAccountResponse response = staffAccountService.getStaffAccountById(1);
//
//        assertNotNull(response);
//        assertEquals("testUser", response.getUsername());
//        verify(staffAccountRepository, times(1)).findIDStaffAccount(anyInt());
//    }
//
//    @Test
//    public void testUpdateStaffAccount() {
//        when(staffAccountRepository.findById(anyInt())).thenReturn(Optional.of(staffAccount));
//        when(authenticationRepository.findById(anyLong())).thenReturn(Optional.of(account));
//
//        String result = staffAccountService.updateStaffAccount(1, staffAccountRequest);
//
//        assertEquals("Update Staff Successfully", result);
//        verify(staffAccountRepository, times(1)).findById(anyInt());
//        verify(authenticationRepository, times(1)).findById(anyLong());
//        verify(authenticationRepository, times(1)).save(any(Account.class));
//        verify(staffAccountRepository, times(1)).save(any(StaffAccount.class));
//    }
//
//    @Test
//    public void testDeactivateStaffAccount() {
//        when(staffAccountRepository.findById(anyInt())).thenReturn(Optional.of(staffAccount));
//
//        boolean result = staffAccountService.deactivateStaffAccount(1);
//
//        assertTrue(result);
//        assertEquals(0, account.getStatus());
//        verify(staffAccountRepository, times(1)).findById(anyInt());
//        verify(authenticationRepository, times(1)).save(any(Account.class));
//    }
//
//    @Test
//    public void testGetStaffWithoutShift() {
//        when(staffAccountRepository.findStaffWithoutShift()).thenReturn(Collections.singletonList(staffAccount));
//
//        List<StaffAccountResponse> responses = staffAccountService.getStaffWithoutShift();
//
//        assertNotNull(responses);
//        assertEquals(1, responses.size());
//        assertEquals("testUser", responses.get(0).getUsername());
//        verify(staffAccountRepository, times(1)).findStaffWithoutShift();
//    }
//}
