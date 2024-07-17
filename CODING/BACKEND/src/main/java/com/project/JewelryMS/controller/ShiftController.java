package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.DeleteShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.service.ShiftService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/shift")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class ShiftController {
    @Autowired
    private ShiftService shiftService;

    // Create Shift
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<ShiftRequest> createShift(@RequestBody CreateShiftRequest createShiftRequest) {
        ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
        return ResponseEntity.ok(createdShift);
    }

    // Read All Shifts
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getAllShifts() {
        List<ShiftRequest> allShifts = shiftService.readAllShifts();
        return ResponseEntity.ok(allShifts);
    }

    // Read Shift by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<ShiftRequest> getShiftById(@PathVariable("id") int id) {
        ShiftRequest shift = shiftService.getShiftById(id);
        return ResponseEntity.ok(shift);
    }

    // Read Shifts by start time
    @GetMapping("/by-start-time")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByStartTime(@RequestParam("startTime") String startTime) {
        List<ShiftRequest> shifts = shiftService.getShiftsByStartTime(startTime);
        return ResponseEntity.ok(shifts);
    }

    // Read Shifts by end time
    @GetMapping("/by-end-time")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByEndTime(@RequestParam("endTime") String endTime) {
        List<ShiftRequest> shifts = shiftService.getShiftsByEndTime(endTime);
        return ResponseEntity.ok(shifts);
    }

    // Read Shifts by shift type
    @GetMapping("/by-shift-type")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByShiftType(@RequestParam("shiftType") String shiftType) {
        List<ShiftRequest> shifts = shiftService.getShiftsByShiftType(shiftType);
        return ResponseEntity.ok(shifts);
    }

    // Read Shifts by status
    @GetMapping("/by-status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByStatus(@RequestParam("status") String status) {
        List<ShiftRequest> shifts = shiftService.getShiftsByStatus(status);
        return ResponseEntity.ok(shifts);
    }

    // Read Shifts by register
    @GetMapping("/by-register")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByRegister(@RequestParam("register") int register) {
        List<ShiftRequest> shifts = shiftService.getShiftsByRegister(register);
        return ResponseEntity.ok(shifts);
    }

    // Read Shifts by work area
    @GetMapping("/by-work-area")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByWorkArea(@RequestParam("workArea") String workArea) {
        List<ShiftRequest> shifts = shiftService.getShiftsByWorkArea(workArea);
        return ResponseEntity.ok(shifts);
    }

    // Delete Shift by ID
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deleteShiftById(@PathVariable("id") int id) {
        shiftService.deleteShiftById((long)id);
        return ResponseEntity.ok("Shift details marked as invalid successfully");
    }

    // Update Shift by ID
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updateShift(@PathVariable("id") int id, @RequestBody ShiftRequest shiftRequest) {
        shiftRequest.setShiftID(id); // Ensure the ID from the path is set in the request
        shiftService.updateShiftDetails(shiftRequest);
        return ResponseEntity.ok("Shift Details updated successfully");
    }

    // Endpoint to delete shifts with no staff and older than 2 months
    @DeleteMapping("/deleteOldShifts")
    public ResponseEntity<String> deleteShiftsWithCriteria() {
        try {
            shiftService.deleteShiftsWithCriteria();
            return ResponseEntity.ok("Shifts deleted successfully based on criteria.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete shifts: " + e.getMessage());
        }
    }
}
