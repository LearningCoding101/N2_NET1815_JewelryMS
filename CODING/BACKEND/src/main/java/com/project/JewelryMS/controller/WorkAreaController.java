package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Shift.GetWorkAreaRequest;
import com.project.JewelryMS.model.Shift.WorkAreaRequest;
import com.project.JewelryMS.service.Shift.WorkAreaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workareas")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class WorkAreaController {

    @Autowired
    private WorkAreaService workAreaService;

    // Get all work areas
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<GetWorkAreaRequest>> getAllWorkAreas() {
        List<GetWorkAreaRequest> workAreas = workAreaService.getAllWorkAreas();
        return ResponseEntity.ok(workAreas);
    }

    // Get work area by ID
    @GetMapping("/{workAreaID}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<WorkAreaRequest> getWorkAreaById(@PathVariable String workAreaID) {
        WorkAreaRequest workArea = workAreaService.getWorkAreaByWorkAreaID(workAreaID);
        return ResponseEntity.ok(workArea);
    }

    // Update work area
    @PutMapping("/{workAreaID}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<WorkAreaRequest> updateWorkArea(
            @PathVariable String workAreaID,
            @RequestBody WorkAreaRequest workAreaRequest) {
        WorkAreaRequest updatedWorkArea = workAreaService.updateWorkArea(workAreaID, workAreaRequest);
        return ResponseEntity.ok(updatedWorkArea);
    }

    // Delete work area(Change status)
    @DeleteMapping("/{workAreaID}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<Void> deleteWorkArea(@PathVariable String workAreaID) {
        workAreaService.deleteWorkArea(workAreaID);
        return ResponseEntity.noContent().build();
    }
}
