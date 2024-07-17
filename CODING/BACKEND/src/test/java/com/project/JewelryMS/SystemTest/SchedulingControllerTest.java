package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.SchedulingController;
import com.project.JewelryMS.model.Shift.AssignStaffByShiftTypePatternRequest;
import com.project.JewelryMS.model.Shift.AssignStaffToMultipleDaysRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.model.StaffShift.StaffShiftResponse;
import com.project.JewelryMS.service.SchedulingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SchedulingControllerTest {

    @Mock
    private SchedulingService schedulingService;

    @InjectMocks
    private SchedulingController schedulingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignStaffToShift() {
        when(schedulingService.assignStaffToShift(anyInt(), anyInt())).thenReturn(null);

        ResponseEntity<?> response = schedulingController.assignStaffToShift(1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Staff assigned to shift successfully.", response.getBody());
    }

    @Test
    void testAssignStaffToDateRange() {
        AssignStaffToMultipleDaysRequest request = new AssignStaffToMultipleDaysRequest();
        when(schedulingService.assignStaffToDateRange(anyList(), any(), any(), anyList())).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = schedulingController.assignStaffToDateRange(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Staff assigned to date range successfully.", response.getBody());
    }

    @Test
    void testAssignStaffByShiftTypePattern() {
        AssignStaffByShiftTypePatternRequest request = new AssignStaffByShiftTypePatternRequest();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);
        List<StaffAccountResponse> mockResponse = new ArrayList<>();
        when(schedulingService.assignStaffByShiftTypePattern(anyMap(), any(), any())).thenReturn(mockResponse);

        ResponseEntity<?> response = schedulingController.assignStaffByShiftTypePattern(request, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }
}