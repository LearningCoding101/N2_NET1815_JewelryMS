package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.Staff_Shift;
import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.model.StaffShift.StaffShiftResponse;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import com.project.JewelryMS.repository.StaffShiftRepository;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
@Service
public class SchedulingService {

    @Autowired
    private StaffAccountRepository staffAccountRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private StaffShiftRepository staffShiftRepository;

    // Method to assign a staff member to a shift
    @Transactional
    public Staff_Shift assignStaffToShift(int staffId, long shiftId) {
        // Fetch the staff and shift entities from the database
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        Shift shift = shiftRepository.findById((long) shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        // Check if the staff member is already assigned to the shift
        if (staff.getStaffShifts().stream().anyMatch(ss -> ss.getShift().equals(shift))) {
            throw new RuntimeException("Staff is already assigned to this shift");
        }

        // Create a new Staff_Shift entity
        Staff_Shift staffShift = new Staff_Shift();
        staffShift.setStaffAccount(staff);
        staffShift.setShift(shift);

        // Save the new Staff_Shift entity to the database
        return staffShiftRepository.save(staffShift);
    }

    // Method to assign a shift to a staff member
    @Transactional
    public Staff_Shift assignShiftToStaff(int shiftId, int staffId) {
        return assignStaffToShift(staffId, shiftId);
    }
    public Map<String, Map<String, List<StaffShiftResponse>>> getScheduleMatrix(LocalDate startDate, LocalDate endDate) {
        // Define the shift types
        String[] shiftTypes = {"Morning", "Afternoon", "Evening"};

        // Initialize the matrix
        Map<String, Map<String, List<StaffShiftResponse>>> matrix = new LinkedHashMap<>();

        // Date and Time formatters
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd-MM-yyyy");
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        // Iterate over each date in the range
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            // Initialize the map for the current date
            String formattedDate = date.format(dateFormatter);
            matrix.put(formattedDate, new LinkedHashMap<>());

            // Initialize the list for each shift type
            for (String shiftType : shiftTypes) {
                matrix.get(formattedDate).put(shiftType, new ArrayList<>());

                // Get all shifts of the current type for the current date
                List<Shift> shifts = shiftRepository.findAllByDateAndType(date, shiftType);

                // For each shift, get the staff assigned to it and create a StaffShiftResponse
                for (Shift shift : shifts) {
                    List<StaffAccount> staffAccounts = staffAccountRepository.findAllByShift(shift);

                    // Format the start and end times
                    String formattedStartTime = shift.getStartTime().format(timeFormatter);
                    String formattedEndTime = shift.getEndTime().format(timeFormatter);

                    // Create a new StaffShiftResponse object and add it to the list for the current date and shift type
                    StaffShiftResponse staffShift = new StaffShiftResponse(shift.getShiftID(), formattedStartTime, formattedEndTime, shiftType, shift.getStatus(), shift.getWorkArea(), shift.getRegister(), staffAccounts.stream().map(s -> new StaffShiftResponse.StaffResponse(s.getStaffID(), s.getAccount().getAccountName(), s.getAccount().getEmail(), s.getAccount().getUsername())).collect(Collectors.toList()));
                    matrix.get(formattedDate).get(shiftType).add(staffShift);
                }
            }
        }

        return matrix;
    }

    // Method to assign multiple staff to a shift
    @Transactional
    public List<Staff_Shift> assignMultipleStaffToShift(List<Integer> staffIds, int shiftId) {
        List<Staff_Shift> staffShifts = new ArrayList<>();
        for (int staffId : staffIds) {
            Staff_Shift staffShift = assignStaffToShift(staffId, shiftId);
            staffShifts.add(staffShift);
        }
        return staffShifts;
    }

    // Method to assign multiple shifts to a staff
    @Transactional
    public List<Staff_Shift> assignMultipleShiftsToStaff(int staffId, List<Integer> shiftIds) {
        List<Staff_Shift> staffShifts = new ArrayList<>();
        for (int shiftId : shiftIds) {
            Staff_Shift staffShift = assignStaffToShift(staffId, shiftId);
            staffShifts.add(staffShift);
        }
        return staffShifts;
    }

    // Method to remove a staff member from a shift
    @Transactional
    public void removeStaffFromShift(int staffId, long shiftId) {
        // Fetch the staff and shift entities from the database
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        Shift shift = shiftRepository.findById((long) shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        // Find the Staff_Shift entity that links the staff member and the shift
        Staff_Shift staffShift = staffShiftRepository.findByStaffAccountAndShift(staff, shift)
                .orElseThrow(() -> new RuntimeException("Staff is not assigned to this shift"));

        // Delete the Staff_Shift entity from the database
        staffShiftRepository.delete(staffShift);
    }

    // Method to remove a shift from a staff member
    @Transactional
    public void removeShiftFromStaff(int shiftId, int staffId) {
        removeStaffFromShift(staffId, shiftId);
    }

    @Autowired
    private ShiftService shiftService;

    @Transactional
    public StaffShiftResponse assignStaffToDay(int staffId, LocalDate date, String shiftType) {
        // Fetch the staff entity from the database
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new ShiftAssignmentException("Staff not found"));

        // Check if the staff member is already assigned to a shift during the same period on the same day
        boolean isAssigned = staff.getStaffShifts().stream()
                .anyMatch(ss -> ss.getShift().getStartTime().toLocalDate().equals(date) && ss.getShift().getShiftType().equals(shiftType));

        if (isAssigned) {
            throw new ShiftAssignmentException("Staff is already assigned to a shift during this period on this day. Please choose a different shift or staff member.");
        }
        // Find a shift on the specified date and period
        Shift shift = shiftRepository.findAllByDateAndType(date, shiftType)
                .stream().findFirst().orElse(null);

        // If no shift exists, create a new one using the ShiftService
        if (shift == null) {
            CreateShiftRequest createShiftRequest = new CreateShiftRequest();
            createShiftRequest.setShiftType(shiftType);
            createShiftRequest.setStatus("Active");
            createShiftRequest.setWorkArea("Sales");  // Set this according to your requirements
            createShiftRequest.setRegister(0);

            // Define the start and end times for each shift type
            String startTime, endTime;
            switch (shiftType) {
                case "Morning":
                    startTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 08";
                    endTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 12";
                    break;
                case "Afternoon":
                    startTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 13";
                    endTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 17";
                    break;
                case "Evening":
                    startTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 17";
                    endTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 21";
                    break;
                default:
                    throw new RuntimeException("Invalid shift type");
            }

            createShiftRequest.setStartTime(startTime);
            createShiftRequest.setEndTime(endTime);

            ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
            shift = shiftRepository.findById((long) createdShift.getShiftID())
                    .orElseThrow(() -> new RuntimeException("Shift not found"));
        }

        // Create a new Staff_Shift entity
        Staff_Shift staffShift = new Staff_Shift();
        staffShift.setStaffAccount(staff);
        staffShift.setShift(shift);

        // Save the new Staff_Shift entity to the database
        staffShift = staffShiftRepository.save(staffShift);

        // Convert the new Staff_Shift entity to a StaffShiftResponse and return it
        return toStaffShiftResponse(staffShift);
    }

    //This need a revised
    private static class ShiftAssignmentException extends RuntimeException {
        public ShiftAssignmentException(String message) {
            super(message);
        }
    }

    // Helper method to convert a Staff_Shift entity to a StaffShiftResponse
    private StaffShiftResponse toStaffShiftResponse(Staff_Shift staffShift) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedStartTime = staffShift.getShift().getStartTime().format(timeFormatter);
        String formattedEndTime = staffShift.getShift().getEndTime().format(timeFormatter);

        StaffShiftResponse.StaffResponse staffResponse = new StaffShiftResponse.StaffResponse(staffShift.getStaffAccount().getStaffID(), staffShift.getStaffAccount().getAccount().getAccountName(), staffShift.getStaffAccount().getAccount().getEmail(), staffShift.getStaffAccount().getAccount().getUsername());

        return new StaffShiftResponse(staffShift.getShift().getShiftID(), formattedStartTime, formattedEndTime, staffShift.getShift().getShiftType(), staffShift.getShift().getStatus(), staffShift.getShift().getWorkArea(), staffShift.getShift().getRegister(), Collections.singletonList(staffResponse));
    }

    @Transactional
    public Shift updateShiftWorkArea(long shiftId, String newWorkArea) {
        // Fetch the shift entity from the database
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        // Update the work area
        shift.setWorkArea(newWorkArea);

        // Save the updated shift entity to the database
        return shiftRepository.save(shift);
    }
}