package com.project.JewelryMS.service.Shift;

import com.project.JewelryMS.entity.WorkArea;
import com.project.JewelryMS.model.Shift.GetWorkAreaRequest;
import com.project.JewelryMS.model.Shift.WorkAreaRequest;
import com.project.JewelryMS.repository.WorkAreaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkAreaService {

    @Autowired
    private WorkAreaRepository workAreaRepository;

    // Get all work areas
    public List<GetWorkAreaRequest> getAllWorkAreas() {
        List<WorkArea> workAreas = workAreaRepository.findAll();
        return workAreas.stream()
                .map(this::toGetWorkAreaRequest)
                .collect(Collectors.toList());
    }

    // Get work area by ID
    public WorkAreaRequest getWorkAreaByWorkAreaID(String workAreaID) {
        WorkArea workArea = workAreaRepository.findByWorkAreaID(workAreaID)
                .orElseThrow(() -> new RuntimeException("Work Area ID: " + workAreaID + " not found"));
        return toWorkAreaRequest(workArea);
    }

    // Update work area
    public WorkAreaRequest updateWorkArea(String workAreaID, WorkAreaRequest workAreaRequest) {
        WorkArea workArea = workAreaRepository.findByWorkAreaID(workAreaID)
                .orElseThrow(() -> new RuntimeException("Work Area ID: " + workAreaID + " not found"));

        workArea.setRegister(workAreaRequest.getRegister());
        workArea.setDescription(workAreaRequest.getDescription());
        workArea = workAreaRepository.save(workArea);
        return toWorkAreaRequest(workArea);
    }

    // Delete work area
    @Transactional
    public void deleteWorkArea(String workAreaID) {
        WorkArea workArea = workAreaRepository.findByWorkAreaID(workAreaID)
                .orElseThrow(() -> new RuntimeException("Work Area ID: " + workAreaID + " not found"));

        workArea.setStatus("Inactive");  // Update status instead of deleting
        workAreaRepository.save(workArea);
    }

    // Convert WorkArea entity to WorkAreaRequest DTO
    private WorkAreaRequest toWorkAreaRequest(WorkArea workArea) {
        WorkAreaRequest workAreaRequest = new WorkAreaRequest();
        workAreaRequest.setWorkAreaID(workArea.getWorkAreaID());
        workAreaRequest.setRegister(workArea.getRegister());
        workAreaRequest.setDescription(workArea.getDescription());
        return workAreaRequest;
    }

    // Convert WorkArea entity to GetWorkAreaRequest DTO
    private GetWorkAreaRequest toGetWorkAreaRequest(WorkArea workArea) {
        return new GetWorkAreaRequest(
                workArea.getId(),
                workArea.getWorkAreaID(),
                workArea.getRegister(),
                workArea.getDescription(),
                workArea.getStatus()
        );
    }


}

