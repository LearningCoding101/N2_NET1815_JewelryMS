package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.Staff_Shift;
import com.project.JewelryMS.model.Shift.AssignStaffByShiftTypePatternRequest;
import com.project.JewelryMS.model.Shift.AssignStaffToMultipleDaysRequest;
import com.project.JewelryMS.model.Shift.UpdateStaffWorkAreaRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.model.StaffShift.IdWrapper;
import com.project.JewelryMS.model.StaffShift.StaffShiftResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.project.JewelryMS.service.Shift.SchedulingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scheduling")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class SchedulingController {

    @Autowired
    private SchedulingService schedulingService;

    @PostMapping("/assignStaffToShift")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> assignStaffToShift(@RequestParam int staffId, @RequestParam int shiftId) {
        try {
            Staff_Shift staffShift = schedulingService.assignStaffToShift(staffId, shiftId);
            return ResponseEntity.ok("Staff assigned to shift successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign staff to shift: " + e.getMessage());
        }
    }

    @PostMapping("/assignShiftToStaff")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> assignShiftToStaff(@RequestParam int shiftId, @RequestParam int staffId) {
        try {
            Staff_Shift staffShift = schedulingService.assignShiftToStaff(shiftId, staffId);
            return ResponseEntity.ok("Shift assigned to staff successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign shift to staff: " + e.getMessage());
        }
    }

    @GetMapping("/scheduleMatrix")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> getScheduleMatrix(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                               @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            Map<String, Map<String, List<StaffShiftResponse>>> matrix = schedulingService.getScheduleMatrix(startDate, endDate);
            return ResponseEntity.ok(matrix);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch schedule matrix: " + e.getMessage());
        }
    }

    @PostMapping("/assignMultipleStaffToShift")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> assignMultipleStaffToShift(@RequestBody List<IdWrapper> staffIds, @RequestParam int shiftId) {
        try {
            List<Staff_Shift> staffShifts = schedulingService.assignMultipleStaffToShift(
                    staffIds.stream().map(IdWrapper::getId).collect(Collectors.toList()), shiftId);
            return ResponseEntity.ok("Staff assigned to shift successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign staff to shift: " + e.getMessage());
        }
    }

    @PostMapping("/assignMultipleShiftsToStaff")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> assignMultipleShiftsToStaff(@RequestParam int staffId, @RequestBody List<IdWrapper> shiftIds) {
        try {
            List<Staff_Shift> staffShifts = schedulingService.assignMultipleShiftsToStaff(
                    staffId, shiftIds.stream().map(IdWrapper::getId).collect(Collectors.toList()));
            return ResponseEntity.ok("Shifts assigned to staff successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign shifts to staff: " + e.getMessage());
        }
    }

    @DeleteMapping("/removeStaffFromShift")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> removeStaffFromShift(@RequestParam int staffId, @RequestParam int shiftId) {
        try {
            schedulingService.removeStaffFromShift(staffId, shiftId);
            return ResponseEntity.ok("Unassigning staff from shift is complete.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove staff from shift: " + e.getMessage());
        }
    }

    @DeleteMapping("/removeShiftFromStaff")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> removeShiftFromStaff(@RequestParam int shiftId, @RequestParam int staffId) {
        try {
            schedulingService.removeShiftFromStaff(shiftId, staffId);
            return ResponseEntity.ok("Unassigning shift from staff is complete.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove shift from staff: " + e.getMessage());
        }
    }

    @PostMapping("/assignStaffToDay")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> assignStaffToDay(@RequestParam int staffId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam String shiftType) {
        try {
            StaffShiftResponse staffShift = schedulingService.assignStaffToDay(staffId, date, shiftType);
            return ResponseEntity.ok("Staff assigned to day successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign staff to day: " + e.getMessage());
        }
    }

    @PostMapping("/assignStaffToDateRange")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> assignStaffToDateRange(@RequestBody AssignStaffToMultipleDaysRequest request) {
        try {
            List<StaffShiftResponse> staffShiftResponses = schedulingService.assignStaffToDateRange(
                    request.getStaffIds(),
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getShiftTypes());
            return ResponseEntity.ok("Staff assigned to date range successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign staff to date range: " + e.getMessage());
        }
    }

    @PostMapping("/assignStaffByShiftTypePattern")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> assignStaffByShiftTypePattern(
            @RequestBody AssignStaffByShiftTypePatternRequest request,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            List<StaffAccountResponse> staffAccountResponses = schedulingService.assignStaffByShiftTypePattern(
                    request.getStaffShiftPatterns(), startDate, endDate);
            return ResponseEntity.ok(staffAccountResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign staff by shift type pattern: " + e.getMessage());
        }
    }


    @DeleteMapping("/removeStaffFromShiftsInRange")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> removeStaffFromShiftsInRange(@RequestParam int staffId,
                                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            schedulingService.removeStaffFromShiftsInRange(staffId, startDate, endDate);
            return ResponseEntity.ok("Unassigning staff from shifts in the specified range is complete.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove staff from shifts in range: " + e.getMessage());
        }
    }

    //    // New endpoint for assigning staff by day of the week with their specific preferences
//    @PostMapping("/assignStaffByDayOfWeek")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
//    public ResponseEntity<List<StaffShiftResponse>> assignStaffByDayOfWeek(
//            @RequestBody AssignStaffByDayOfWeekRequest request) {
//        List<StaffShiftResponse> staffShiftResponses = schedulingService.assignStaffByDayOfWeek(
//                request.getStaffAvailability(),
//                request.getStartDate(),
//                request.getEndDate()
//        );
//        return ResponseEntity.ok(staffShiftResponses);
//    }

    @PostMapping("/assignRandomStaffShiftPattern")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<?> assignRandomStaffShiftPattern(
            @RequestBody List<Integer> staffIds,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            List<StaffAccountResponse> staffAccountResponses = schedulingService.assignRandomStaffShiftPattern(staffIds, startDate, endDate);
            return ResponseEntity.ok(staffAccountResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign random staff shift pattern: " + e.getMessage());
        }
    }

    @DeleteMapping("/removeAllStaffFromShiftsInRange")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> removeAllStaffFromShiftsInRange(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            schedulingService.removeAllStaffFromShiftsInRange(startDate, endDate);
            return ResponseEntity.ok("All staff removed from shifts in the specified range.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove all staff from shifts in range: " + e.getMessage());
        }
    }

    @PutMapping("/update-work-area")
    public ResponseEntity<UpdateStaffWorkAreaRequest> updateStaffWorkArea(@RequestBody UpdateStaffWorkAreaRequest request) {
        UpdateStaffWorkAreaRequest response = schedulingService.updateStaffWorkArea(request);
        return ResponseEntity.ok(response);
    }
}