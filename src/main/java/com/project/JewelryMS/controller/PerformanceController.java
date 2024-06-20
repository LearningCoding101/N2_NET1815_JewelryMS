package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Performance;
import com.project.JewelryMS.model.Performance.CreatePerformanceRequest;
import com.project.JewelryMS.model.Performance.DeletePerformanceRequest;
import com.project.JewelryMS.model.Performance.PerformanceRequest;
import com.project.JewelryMS.model.Performance.PerformanceResponse;
import com.project.JewelryMS.service.PerformanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/performance")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class PerformanceController {
    @Autowired
    PerformanceService performanceService;

    // Create section
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<PerformanceResponse> createPerformanceReport(@RequestBody CreatePerformanceRequest createPerformanceReport) {
        PerformanceResponse performance = performanceService.createPerformanceReport(createPerformanceReport);
        return ResponseEntity.ok(performance);
    }

    // Read section
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PerformanceResponse>> readAllPerformanceReports() {
        List<PerformanceResponse> performanceList = performanceService.readAllPerformanceReport();
        return ResponseEntity.ok(performanceList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<PerformanceResponse> readPerformanceFromId(@PathVariable Long id) {
        PerformanceResponse performance = performanceService.getPerformanceById(id);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/staff/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<PerformanceResponse> readPerformanceByStaff(@PathVariable Long id) {
        PerformanceResponse performance = performanceService.getPerformanceByStaffID(id);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/date")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PerformanceResponse>> readPerformanceByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        LocalDateTime targetDate = date.atStartOfDay();
        List<PerformanceResponse> performance = performanceService.getPerformanceByDate(targetDate);
        return ResponseEntity.ok(performance);
    }


    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PerformanceResponse>> readAllActiveReports() {
        List<PerformanceResponse> performanceList = performanceService.readAllConfirmedPerformanceReport();
        return ResponseEntity.ok(performanceList);
    }

    // Delete section
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deletePerformanceById(@PathVariable Long id) {
        performanceService.deletePerformanceById(id);
        return ResponseEntity.ok("Performance details marked as invalid successfully");
    }

    // Update section
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updatePerformance(@PathVariable Long id, @RequestBody PerformanceRequest performanceRequest) {
        performanceRequest.setPK_performanceID(id);
        performanceService.updatePerformanceReportDetails(performanceRequest);
        return ResponseEntity.ok("Performance details updated successfully");
    }

}
