package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.model.Shift.WorkAreaRequest;
import com.project.JewelryMS.service.Shift.ShiftService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shift")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

//    // Create Shift
//    @PostMapping("/create")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
//    public ResponseEntity<ShiftRequest> createShift(@RequestBody CreateShiftRequest createShiftRequest) {
//        try {
//            ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdShift);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
        // Create Shift
        @PostMapping("/create")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
        public ResponseEntity<ShiftRequest> createShift(@RequestBody CreateShiftRequest createShiftRequest) {
            try {
                ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdShift);
            } catch (RuntimeException e) {
                // Print the error message to console for debugging
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
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
    public ResponseEntity<ShiftRequest> getShiftById(@PathVariable("id") long id) {
        try {
            ShiftRequest shift = shiftService.getShiftById(id);
            return ResponseEntity.ok(shift);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update Shift by ID
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updateShift(@PathVariable("id") Integer id, @RequestBody ShiftRequest shiftRequest) {
        try {
            shiftRequest.setShiftID(id); // Ensure the ID from the path is set in the request
            shiftService.updateShiftDetails(shiftRequest);
            return ResponseEntity.ok("Shift details updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shift with ID " + id + " not found");
        }
    }

    // Delete Shift by ID (Change status to inactive)
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deleteShiftById(@PathVariable("id") long id) {
        try {
            shiftService.updateShiftStatusToInactiveById(id);
            return ResponseEntity.ok("Shift with ID " + id + " deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shift with ID " + id + " not found");
        }
    }

    // Delete shifts with no staff and older than 2 months
    @DeleteMapping("/deleteOldShifts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> deleteShiftsWithCriteria() {
        try {
            shiftService.deleteShiftsWithCriteria();
            return ResponseEntity.ok("Shifts deleted successfully based on criteria.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete shifts: " + e.getMessage());
        }
    }

    // Read Shifts by start time
    @GetMapping("/by-start-time")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByStartTime(@RequestParam("startTime") String startTime) {
        try {
            List<ShiftRequest> shifts = shiftService.getShiftsByStartTime(startTime);
            return ResponseEntity.ok(shifts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Read Shifts by end time
    @GetMapping("/by-end-time")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByEndTime(@RequestParam("endTime") String endTime) {
        try {
            List<ShiftRequest> shifts = shiftService.getShiftsByEndTime(endTime);
            return ResponseEntity.ok(shifts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Read Shifts by shift type
    @GetMapping("/by-shift-type")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByShiftType(@RequestParam("shiftType") String shiftType) {
        try {
            List<ShiftRequest> shifts = shiftService.getShiftsByShiftType(shiftType);
            return ResponseEntity.ok(shifts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Read Shifts by status
    @GetMapping("/by-status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByStatus(@RequestParam("status") String status) {
        try {
            List<ShiftRequest> shifts = shiftService.getShiftsByStatus(status);
            return ResponseEntity.ok(shifts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

//    // Read Shifts by work area
//    @GetMapping("/by-work-area")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
//    public ResponseEntity<List<ShiftRequest>> getShiftsByWorkArea(@RequestParam("workArea") String workArea) {
//        try {
//            List<ShiftRequest> shifts = shiftService.getShiftsByWorkArea(workArea);
//            return ResponseEntity.ok(shifts);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }

}
