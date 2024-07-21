package com.project.JewelryMS.service.Shift;

import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.WorkArea;
import com.project.JewelryMS.model.Shift.GetWorkAreaRequest;
import com.project.JewelryMS.model.Shift.StaffWorkAreaResponse;
import com.project.JewelryMS.model.Shift.WorkAreaRequest;
import com.project.JewelryMS.model.Shift.WorkAreaResponse;
import com.project.JewelryMS.repository.StaffAccountRepository;
import com.project.JewelryMS.repository.WorkAreaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkAreaService {

    @Autowired
    private WorkAreaRepository workAreaRepository;
    @Autowired
    private StaffAccountRepository staffAccountRepository;

    // Create a new work area
    public WorkAreaRequest createWorkArea(WorkAreaRequest workAreaRequest) {
        if (!isValidWorkAreaID(workAreaRequest.getWorkAreaCode())) {
            throw new IllegalArgumentException("Invalid workAreaCode format. Expected format: <4 Uppercase letters><3 numbers>.");
        }

        WorkArea workArea = new WorkArea();
        workArea.setWorkAreaCode(workAreaRequest.getWorkAreaCode());
        workArea.setRegister(workAreaRequest.getRegister());
        workArea.setDescription(workAreaRequest.getDescription());
        workArea = workAreaRepository.save(workArea);
        return toWorkAreaRequest(workArea);
    }

    // Get all work areas
    public List<GetWorkAreaRequest> getAllWorkAreas() {
        List<WorkArea> workAreas = workAreaRepository.findAll();
        return workAreas.stream()
                .map(this::toGetWorkAreaRequest)
                .collect(Collectors.toList());
    }

    // Get work area by ID
    public WorkAreaRequest getWorkAreaByWorkAreaID(String workAreaCode) {
        WorkArea workArea = workAreaRepository.findByWorkAreaCode(workAreaCode)
                .orElseThrow(() -> new RuntimeException("Work Area ID: " + workAreaCode + " not found"));
        return toWorkAreaRequest(workArea);
    }

    // Update work area
    public WorkAreaRequest updateWorkArea(String workAreaCode, WorkAreaRequest workAreaRequest) {
        WorkArea workArea = workAreaRepository.findByWorkAreaCode(workAreaCode)
                .orElseThrow(() -> new RuntimeException("Work Area ID: " + workAreaCode + " not found"));

        workArea.setRegister(workAreaRequest.getRegister());
        workArea.setDescription(workAreaRequest.getDescription());
        workArea = workAreaRepository.save(workArea);
        return toWorkAreaRequest(workArea);
    }

    // Delete work area
    @Transactional
    public void deleteWorkArea(String workAreaCode) {
        WorkArea workArea = workAreaRepository.findByWorkAreaCode(workAreaCode)
                .orElseThrow(() -> new RuntimeException("Work Area ID: " + workAreaCode + " not found"));

        workArea.setStatus("Inactive");  // Update status instead of deleting
        workAreaRepository.save(workArea);
    }

    // Convert WorkArea entity to WorkAreaRequest DTO
    private WorkAreaRequest toWorkAreaRequest(WorkArea workArea) {
        WorkAreaRequest workAreaRequest = new WorkAreaRequest();
        workAreaRequest.setWorkAreaCode(workArea.getWorkAreaCode());
        workAreaRequest.setRegister(workArea.getRegister());
        workAreaRequest.setDescription(workArea.getDescription());
        return workAreaRequest;
    }

    // Convert WorkArea entity to GetWorkAreaRequest DTO
    private GetWorkAreaRequest toGetWorkAreaRequest(WorkArea workArea) {
        return new GetWorkAreaRequest(
                workArea.getId(),
                workArea.getWorkAreaCode(),
                workArea.getRegister(),
                workArea.getDescription(),
                workArea.getStatus()
        );
    }

    // Method to validate the workAreaID format
    private boolean isValidWorkAreaID(String workAreaID) {
        // Check if the workAreaID matches the format: 4 letters followed by 3 numbers
        return workAreaID.matches("[A-Z]{4}\\d{3}");
    }

    @Transactional
    public List<StaffWorkAreaResponse> getAllStaffWithWorkAreas() {
        List<StaffAccount> staffAccounts = staffAccountRepository.findAll();
        return staffAccounts.stream()
                .map(staff -> new StaffWorkAreaResponse(
                        staff.getStaffID(),
                        staff.getAccount().getAccountName(),
                        staff.getWorkArea() != null ? staff.getWorkArea().getWorkAreaCode() : "No Work Area"
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<WorkAreaResponse> getWorkAreasByParameter(String parameter) {
        List<StaffAccount> staffList = new ArrayList<>();
        List<WorkAreaResponse> responses = new ArrayList<>();

        try {
            Integer staffId = Integer.parseInt(parameter);
            StaffAccount staff = staffAccountRepository.findById(staffId)
                    .orElse(null);
            if (staff != null) {
                WorkArea workArea = staff.getWorkArea();
                responses.add(new WorkAreaResponse(
                        workArea != null ? workArea.getWorkAreaCode() : "No Work Area",
                        workArea != null ? workArea.getDescription() : "No Description",
                        staff.getAccount().getAccountName() // Single staff name
                ));
            }
        } catch (NumberFormatException e) {
            staffList = staffAccountRepository.findByAccountNameContaining(parameter);
            if (staffList.isEmpty()) {
                return responses; // Return empty list if no staff found
            }

            Map<WorkArea, String> workAreaToStaffName = staffList.stream()
                    .collect(Collectors.toMap(
                            StaffAccount::getWorkArea,
                            s -> s.getAccount().getAccountName(), // Use one staff name
                            (existing, replacement) -> existing)); // In case of duplicates, keep the first one

            responses = workAreaToStaffName.entrySet().stream()
                    .map(entry -> new WorkAreaResponse(
                            entry.getKey().getWorkAreaCode(),
                            entry.getKey().getDescription(),
                            entry.getValue() // Single staff name
                    ))
                    .collect(Collectors.toList());
        }

        return responses;
    }
}
