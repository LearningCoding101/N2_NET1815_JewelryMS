//package com.project.JewelryMS.UnitTest;
//
//import com.project.JewelryMS.entity.*;
//import com.project.JewelryMS.model.Shift.CreateShiftRequest;
//import com.project.JewelryMS.model.Shift.ShiftRequest;
//import com.project.JewelryMS.model.Staff.StaffAccountResponse;
//import com.project.JewelryMS.model.StaffShift.StaffShiftResponse;
//import com.project.JewelryMS.repository.ShiftRepository;
//import com.project.JewelryMS.repository.StaffAccountRepository;
//import com.project.JewelryMS.repository.StaffShiftRepository;
//import com.project.JewelryMS.service.Shift.SchedulingService;
//import com.project.JewelryMS.service.Shift.ShiftService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//import java.util.concurrent.*;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class SchedulingServiceTest {
//
//    @Mock
//    private StaffAccountRepository staffAccountRepository;
//
//    @Mock
//    private ShiftRepository shiftRepository;
//
//    @Mock
//    private StaffShiftRepository staffShiftRepository;
//
//    @Mock
//    private ShiftService shiftService;
//
//    @InjectMocks
//    private SchedulingService schedulingService;
//
//    private StaffAccount mockStaffAccount;
//    private Shift mockShift;
//    private Staff_Shift mockStaffShift;
//    private List<StaffAccount> mockStaffAccounts;
//    private List<Shift> mockShifts;
//
//    @BeforeEach
//    void setUp() {
//        mockStaffAccount = new StaffAccount();
//        mockStaffAccount.setStaffID(1);
//
//        Account mockAccount = new Account();
//        mockAccount.setAccountName("John Doe");
//        mockAccount.setEmail("john.doe@example.com");
//        mockAccount.setAUsername("johndoe");
//        mockAccount.setPK_userID(14);
//        mockStaffAccount.setAccount(mockAccount);
//
//        WorkArea mockWorkArea = new WorkArea();
//        mockWorkArea.setWorkAreaID("CASH003");
//        mockWorkArea.setRegister(3);
//        mockWorkArea.setDescription("Cashier");
//
//        mockShift = new Shift();
//        mockShift.setShiftID(1);
//        mockShift.setStartTime(LocalDateTime.now());
//        mockShift.setEndTime(LocalDateTime.now().plusHours(8));
//        mockShift.set(mockWorkArea);
//        mockShift.setStaffShifts(new HashSet<>(Collections.singletonList(mockStaffShift)));
//
//        mockStaffShift = new Staff_Shift();
//        mockStaffShift.setStaffAccount(mockStaffAccount);
//        mockStaffShift.setShift(mockShift);
//
//        mockShifts = Arrays.asList(mockShift);
//        mockStaffAccounts = Arrays.asList(mockStaffAccount);
//    }
//    @Test
//    void testAssignStaffToShift() {
//        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(mockStaffAccount));
//        when(shiftRepository.findById(1L)).thenReturn(Optional.of(mockShift));
//        when(staffShiftRepository.save(any(Staff_Shift.class))).thenReturn(mockStaffShift);
//
//        Staff_Shift result = schedulingService.assignStaffToShift(1, 1L);
//
//        assertEquals(mockStaffShift, result);
//        verify(staffAccountRepository, times(1)).findById(1);
//        verify(shiftRepository, times(1)).findById(1L);
//        verify(staffShiftRepository, times(1)).save(any(Staff_Shift.class));
//    }
//
//    @Test
//    void testAssignShiftToStaff() {
//        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(mockStaffAccount));
//        when(shiftRepository.findById(1L)).thenReturn(Optional.of(mockShift));
//        when(staffShiftRepository.save(any(Staff_Shift.class))).thenReturn(mockStaffShift);
//
//        Staff_Shift result = schedulingService.assignShiftToStaff(1, 1);
//
//        assertEquals(mockStaffShift, result);
//        verify(staffAccountRepository, times(1)).findById(1);
//        verify(shiftRepository, times(1)).findById(1L);
//        verify(staffShiftRepository, times(1)).save(any(Staff_Shift.class));
//    }
//
//
////    @Test
////    void testGetScheduleMatrix() {
////        LocalDate startDate = LocalDate.now();
////        LocalDate endDate = startDate.plusDays(7);
////
////        when(shiftRepository.findAllByStartTimeBetween(any(), any())).thenReturn(mockShifts);
////        when(staffAccountRepository.findAllStaffAccountsByShifts(any(), any())).thenReturn(mockStaffAccounts);
////
////        Map<String, Map<String, List<StaffShiftResponse>>> result = schedulingService.getScheduleMatrix(startDate, endDate);
////
////        assertNotNull(result);
////        verify(shiftRepository, times(1)).findAllByStartTimeBetween(any(), any());
////        verify(staffAccountRepository, times(1)).findAllStaffAccountsByShifts(any(), any());
////    }
//
//
////    @Test
////    void testCleanupShifts() {
////        List<Shift> shifts = new ArrayList<>(mockShifts);
////
////        doAnswer(invocation -> {
////            shifts.clear();
////            return null;
////        }).when(shiftRepository).delete(any(Shift.class));
////
////        schedulingService.cleanupShifts(shifts);
////
////        verify(shiftRepository, times(1)).delete(any(Shift.class));
////        assertTrue(shifts.isEmpty());
////    }
//
////    @Test
////    void testUpdateStatusOfPastShifts() {
////        List<Shift> shifts = new ArrayList<>(mockShifts);
////
////        LocalDate today = LocalDate.now();
////        shifts.forEach(shift -> shift.setEndTime(shift.getEndTime().minusDays(1))); // Ensure the shifts are in the past
////
////        schedulingService.updateStatusOfPastShifts(shifts);
////
////        verify(shiftRepository, times(1)).save(any(Shift.class));
////        assertEquals("Inactive", mockShift.getStatus());
////    }
//
//
//
//    @Test
//    void testAssignMultipleStaffToShift() {
//        when(staffAccountRepository.findById(anyInt())).thenReturn(Optional.of(mockStaffAccount));
//        when(shiftRepository.findById(1L)).thenReturn(Optional.of(mockShift));
//        when(staffShiftRepository.save(any(Staff_Shift.class))).thenReturn(mockStaffShift);
//
//        List<Staff_Shift> result = schedulingService.assignMultipleStaffToShift(Arrays.asList(1, 2, 3), 1);
//
//        assertEquals(3, result.size());
//        verify(staffAccountRepository, times(3)).findById(anyInt());
//        verify(shiftRepository, times(3)).findById(1L);
//        verify(staffShiftRepository, times(3)).save(any(Staff_Shift.class));
//    }
//
////    @Test
////    void testAssignMultipleShiftsToStaff() {
////        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(mockStaffAccount));
////        when(shiftRepository.findById(anyLong())).thenReturn(Optional.of(mockShift));
////        when(staffShiftRepository.save(any(Staff_Shift.class))).thenReturn(mockStaffShift);
////
////        List<Staff_Shift> result = schedulingService.assignMultipleShiftsToStaff(1, Arrays.asList(1, 2, 3));
////
////        assertEquals(3, result.size());
////        verify(staffAccountRepository, times(1)).findById(1); // Ensure findById is called once
////        verify(shiftRepository, times(3)).findById(anyLong()); // Ensure findById is called thrice for each shift
////        verify(staffShiftRepository, times(3)).save(any(Staff_Shift.class)); // Ensure save is called thrice
////    }
//
//    @Test
//    void testRemoveStaffFromShift() {
//        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(mockStaffAccount));
//        when(shiftRepository.findById(1L)).thenReturn(Optional.of(mockShift));
//        when(staffShiftRepository.findByStaffAccountAndShift(any(StaffAccount.class), any(Shift.class))).thenReturn(Optional.of(mockStaffShift));
//
//        schedulingService.removeStaffFromShift(1, 1L);
//
//        verify(staffAccountRepository, times(1)).findById(1);
//        verify(shiftRepository, times(1)).findById(1L);
//        verify(staffShiftRepository, times(1)).findByStaffAccountAndShift(any(StaffAccount.class), any(Shift.class));
//        verify(staffShiftRepository, times(1)).delete(any(Staff_Shift.class));
//    }
//
//    @Test
//    void testRemoveShiftFromStaff() {
//        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(mockStaffAccount));
//        when(shiftRepository.findById(1L)).thenReturn(Optional.of(mockShift));
//        when(staffShiftRepository.findByStaffAccountAndShift(any(StaffAccount.class), any(Shift.class))).thenReturn(Optional.of(mockStaffShift));
//
//        schedulingService.removeShiftFromStaff(1, 1);
//
//        verify(staffAccountRepository, times(1)).findById(1);
//        verify(shiftRepository, times(1)).findById(1L);
//        verify(staffShiftRepository, times(1)).findByStaffAccountAndShift(any(StaffAccount.class), any(Shift.class));
//        verify(staffShiftRepository, times(1)).delete(any(Staff_Shift.class));
//    }
//
//    @Test
//    void testAssignStaffToDay() {
//        LocalDate today = LocalDate.now();
//        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(mockStaffAccount));
//        when(shiftRepository.findAllByDateAndType(eq(today), eq("Morning"))).thenReturn(Collections.singletonList(mockShift));
//        when(staffShiftRepository.save(any(Staff_Shift.class))).thenReturn(mockStaffShift);
//
//        StaffShiftResponse result = schedulingService.assignStaffToDay(1, today, "Morning");
//
//        assertNotNull(result);
//        assertEquals(1, result.getShiftID());
//        verify(staffAccountRepository, times(1)).findById(1);
//        verify(shiftRepository, times(1)).findAllByDateAndType(eq(today), eq("Morning"));
//        verify(staffShiftRepository, times(1)).save(any(Staff_Shift.class));
//    }
//
//    @Test
//    void testAssignStaffToDateRange() throws InterruptedException, ExecutionException {
//        List<Integer> staffIds = Arrays.asList(1, 2, 3);
//        LocalDate startDate = LocalDate.now();
//        LocalDate endDate = startDate.plusDays(7);
//        List<String> shiftTypes = Arrays.asList("Morning", "Afternoon", "Evening");
//
//        when(staffAccountRepository.findById(anyInt())).thenReturn(Optional.of(mockStaffAccount));
//        when(shiftRepository.findAllByDateAndType(any(LocalDate.class), anyString())).thenReturn(Collections.singletonList(mockShift));
//        when(staffShiftRepository.save(any(Staff_Shift.class))).thenReturn(mockStaffShift);
//
//        List<StaffShiftResponse> result = schedulingService.assignStaffToDateRange(staffIds, startDate, endDate, shiftTypes);
//
//        assertFalse(result.isEmpty());
//        assertEquals(staffIds.size() * shiftTypes.size() * (int) ChronoUnit.DAYS.between(startDate, endDate.plusDays(1)), result.size());
//        verify(staffAccountRepository, atLeast(staffIds.size() * shiftTypes.size())).findById(anyInt());
//        verify(shiftRepository, atLeast(staffIds.size() * shiftTypes.size())).findAllByDateAndType(any(LocalDate.class), anyString());
//        verify(staffShiftRepository, atLeast(staffIds.size() * shiftTypes.size())).save(any(Staff_Shift.class));
//    }
//
//    @Test
//    void testRemoveStaffFromShiftsInRange() {
//        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(mockStaffAccount));
//        when(staffShiftRepository.findAllByStaffAccountAndShift_StartTimeBetween(any(StaffAccount.class), any(), any())).thenReturn(Collections.singletonList(mockStaffShift));
//
//        schedulingService.removeStaffFromShiftsInRange(1, LocalDate.now(), LocalDate.now().plusDays(7));
//
//        verify(staffAccountRepository, times(1)).findById(1);
//        verify(staffShiftRepository, times(1)).findAllByStaffAccountAndShift_StartTimeBetween(any(StaffAccount.class), any(), any());
//        verify(staffShiftRepository, times(1)).deleteAll(any());
//    }
//
////    @Test
////    void testRemoveAllStaffFromShiftsInRange() {
////        when(shiftRepository.findAllByStartTimeBetween(any(), any())).thenReturn(mockShifts);
////        doAnswer(invocation -> null).when(staffShiftRepository).deleteAll(anyList());
////        doAnswer(invocation -> {
////            for (Shift shift : mockShifts) {
////                shift.setStaffShifts(new HashSet<>());
////            }
////            return null;
////        }).when(shiftRepository).saveAll(anyList());
////
////        schedulingService.removeAllStaffFromShiftsInRange(LocalDate.now(), LocalDate.now().plusDays(7));
////
////        verify(shiftRepository, times(1)).findAllByStartTimeBetween(any(), any());
////        verify(staffShiftRepository, times(1)).deleteAll(anyList());
////        verify(shiftRepository, times(1)).saveAll(anyList());
////    }
//
//    @Test
//    void testAssignStaffByShiftTypePattern() throws InterruptedException, ExecutionException {
//        Map<String, List<Integer>> staffShiftPatterns = new HashMap<>();
//        staffShiftPatterns.put("Morning", Arrays.asList(1, 2));
//        staffShiftPatterns.put("Afternoon", Arrays.asList(2, 3));
//        staffShiftPatterns.put("Evening", Arrays.asList(3, 1));
//
//        when(staffAccountRepository.findById(anyInt())).thenReturn(Optional.of(mockStaffAccount));
//        when(shiftRepository.findAllByDateAndType(any(LocalDate.class), anyString())).thenReturn(Collections.singletonList(mockShift));
//        when(staffShiftRepository.save(any(Staff_Shift.class))).thenReturn(mockStaffShift);
//
//        List<StaffAccountResponse> result = schedulingService.assignStaffByShiftTypePattern(staffShiftPatterns, LocalDate.now(), LocalDate.now().plusDays(7));
//
//        assertFalse(false);
//        verify(staffAccountRepository, atLeast(3)).findById(anyInt());
//        verify(shiftRepository, atLeast(3)).findAllByDateAndType(any(LocalDate.class), anyString());
//        verify(staffShiftRepository, atLeast(3)).save(any(Staff_Shift.class));
//    }
//
//    @Test
//    void testAssignRandomStaffShiftPattern() throws InterruptedException, ExecutionException {
//        List<Integer> staffIds = Arrays.asList(1, 2, 3);
//        LocalDate startDate = LocalDate.now();
//        LocalDate endDate = startDate.plusDays(7);
//
//        when(staffAccountRepository.findById(anyInt())).thenReturn(Optional.of(mockStaffAccount));
//        when(shiftRepository.findAllByDateAndType(any(LocalDate.class), anyString())).thenReturn(Collections.singletonList(mockShift));
//        when(staffShiftRepository.save(any(Staff_Shift.class))).thenReturn(mockStaffShift);
//
//        List<StaffAccountResponse> result = schedulingService.assignRandomStaffShiftPattern(staffIds, startDate, endDate);
//
//        assertFalse(false);
//        verify(staffAccountRepository, atLeast(3)).findById(anyInt());
//        verify(shiftRepository, atLeast(3)).findAllByDateAndType(any(LocalDate.class), anyString());
//        verify(staffShiftRepository, atLeast(3)).save(any(Staff_Shift.class));
//    }
//
//    @Test
//    void testScheduleShiftsAutomatically() {
//        when(staffAccountRepository.findAllStaffAccountsByRoleStaff()).thenReturn(mockStaffAccounts);
//
//        schedulingService.scheduleShiftsAutomatically();
//
//        verify(staffAccountRepository, times(1)).findAllStaffAccountsByRoleStaff();
//    }
//}
