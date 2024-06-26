package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.Staff_Shift;
import com.project.JewelryMS.model.Shift.AssignStaffByDayOfWeekRequest;
import com.project.JewelryMS.model.Shift.AssignStaffToMultipleDaysRequest;
import com.project.JewelryMS.model.StaffShift.IdWrapper;
import com.project.JewelryMS.model.StaffShift.StaffShiftResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.project.JewelryMS.service.SchedulingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("scheduling")
@SecurityRequirement(name = "api")
public class SchedulingController {

    @Autowired
    private SchedulingService schedulingService;

    @PostMapping("/assignStaffToShift")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<Staff_Shift> assignStaffToShift(@RequestParam int staffId, @RequestParam int shiftId) {
        Staff_Shift staffShift = schedulingService.assignStaffToShift(staffId, shiftId);
        return ResponseEntity.ok(staffShift);
    }

    @PostMapping("/assignShiftToStaff")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<Staff_Shift> assignShiftToStaff(@RequestParam int shiftId, @RequestParam int staffId) {
        Staff_Shift staffShift = schedulingService.assignShiftToStaff(shiftId, staffId);
        return ResponseEntity.ok(staffShift);
    }

    @GetMapping("/scheduleMatrix")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<Map<String, Map<String, List<StaffShiftResponse>>>> getScheduleMatrix(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                                                @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Map<String, List<StaffShiftResponse>>> matrix = schedulingService.getScheduleMatrix(startDate, endDate);
        return ResponseEntity.ok(matrix);
    }

    @PostMapping("/assignMultipleStaffToShift")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<Staff_Shift>> assignMultipleStaffToShift(@RequestBody List<IdWrapper> staffIds, @RequestParam int shiftId) {
        List<Staff_Shift> staffShifts = schedulingService.assignMultipleStaffToShift(staffIds.stream().map(IdWrapper::getId).collect(Collectors.toList()), shiftId);
        return ResponseEntity.ok(staffShifts);
    }

    @PostMapping("/assignMultipleShiftsToStaff")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<Staff_Shift>> assignMultipleShiftsToStaff(@RequestParam int staffId, @RequestBody List<IdWrapper> shiftIds) {
        List<Staff_Shift> staffShifts = schedulingService.assignMultipleShiftsToStaff(staffId, shiftIds.stream().map(IdWrapper::getId).collect(Collectors.toList()));
        return ResponseEntity.ok(staffShifts);
    }


    @DeleteMapping("/removeStaffFromShift")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<String> removeStaffFromShift(@RequestParam int staffId, @RequestParam int shiftId) {
        schedulingService.removeStaffFromShift(staffId, shiftId);
        return ResponseEntity.ok("Unassigning staff from shift is complete.");
    }

    @DeleteMapping("/removeShiftFromStaff")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<String> removeShiftFromStaff(@RequestParam int shiftId, @RequestParam int staffId) {
        schedulingService.removeShiftFromStaff(shiftId, staffId);
        return ResponseEntity.ok("Unassigning shift from staff is complete.");
    }

    @PostMapping("/assignStaffToDay")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<StaffShiftResponse> assignStaffToDay(@RequestParam int staffId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam String shiftType) {
        StaffShiftResponse staffShift = schedulingService.assignStaffToDay(staffId, date, shiftType);
        return ResponseEntity.ok(staffShift);
    }

    @PutMapping("/updateShiftWorkArea")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Shift> updateShiftWorkArea(@RequestParam long shiftId, @RequestParam String newWorkArea) {
        Shift updatedShift = schedulingService.updateShiftWorkArea(shiftId, newWorkArea);
        return ResponseEntity.ok(updatedShift);
    }

    @PostMapping("/assignStaffToDateRange")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<StaffShiftResponse>> assignStaffToDateRange(
            @RequestBody AssignStaffToMultipleDaysRequest request) {
        List<StaffShiftResponse> staffShiftResponses = schedulingService.assignStaffToDateRange(
                request.getStaffIds(),
                request.getStartDate(),
                request.getEndDate(),
                request.getShiftTypes()
        );
        return ResponseEntity.ok(staffShiftResponses);
    }

    // New endpoint for assigning staff by day of the week with their specific preferences
    @PostMapping("/assignStaffByDayOfWeek")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<StaffShiftResponse>> assignStaffByDayOfWeek(
            @RequestBody AssignStaffByDayOfWeekRequest request) {
        List<StaffShiftResponse> staffShiftResponses = schedulingService.assignStaffByDayOfWeek(
                request.getStaffAvailability(),
                request.getStartDate(),
                request.getEndDate()
        );
        return ResponseEntity.ok(staffShiftResponses);
    }
}
