package com.project.JewelryMS.service.Shift;

import com.project.JewelryMS.entity.Shift;

import com.project.JewelryMS.entity.WorkArea;
import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.model.Shift.WorkAreaRequest;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.WorkAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private WorkAreaRepository workAreaRepository;

    // Define a formatter for "yyyy-MM-dd HH:mm"
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Method to create a new shift with an associated work area
    @Transactional
    public ShiftRequest createShift(CreateShiftRequest createShiftRequest) {
        Shift shift = new Shift();
        shift.setStartTime(LocalDateTime.parse(createShiftRequest.getStartTime(), DATE_TIME_FORMATTER));
        shift.setEndTime(LocalDateTime.parse(createShiftRequest.getEndTime(), DATE_TIME_FORMATTER));
        shift.setShiftType(createShiftRequest.getShiftType());
        shift.setStatus(createShiftRequest.getStatus());

        // Create or find the WorkArea entity
        WorkArea workArea = findOrCreateWorkArea(createShiftRequest.getWorkArea());

        shift.setWorkArea(workArea);

        shiftRepository.save(shift);
        return convertToShiftRequest(shift);
    }

    // Method to find or create a WorkArea entity based on workAreaID
    private WorkArea findOrCreateWorkArea(WorkAreaRequest workAreaRequest) {
        String workAreaID = workAreaRequest.getWorkAreaID();

        // Validate the workAreaID format
        if (!isValidWorkAreaID(workAreaID)) {
            throw new IllegalArgumentException("Invalid workAreaID format. Expected format: <4 letters><3 numbers>.");
        }

        // Find the WorkArea entity by workAreaID or create a new one if not found
        Optional<WorkArea> workAreaOptional = workAreaRepository.findByWorkAreaID(workAreaID);
        WorkArea workArea;
        if (workAreaOptional.isPresent()) {
            workArea = workAreaOptional.get();
        } else {
            // Create a new WorkArea if not found
            workArea = new WorkArea();
            workArea.setWorkAreaID(workAreaID);
            workArea.setRegister(extractRegisterNumber(workAreaID));
            workArea.setDescription(workAreaRequest.getDescription());
            workArea = workAreaRepository.save(workArea);
        }
        return workArea;
    }

    // Method to validate the workAreaID format
    private boolean isValidWorkAreaID(String workAreaID) {
        // Check if the workAreaID matches the format: 4 letters followed by 3 numbers
        return workAreaID.matches("[A-Z]{4}\\d{3}");
    }

    // Method to extract register number from workAreaID
    private int extractRegisterNumber(String workAreaID) {
        // Extract the last 3 digits as the register number
        String registerStr = workAreaID.substring(4); // Extracts digits after the 4 letters
        return Integer.parseInt(registerStr);
    }

    // Convert Shift entity to ShiftRequest DTO
    private ShiftRequest convertToShiftRequest(Shift shift) {
        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setShiftID(shift.getShiftID());
        shiftRequest.setStartTime(shift.getStartTime().format(DATE_TIME_FORMATTER));
        shiftRequest.setEndTime(shift.getEndTime().format(DATE_TIME_FORMATTER));
        shiftRequest.setShiftType(shift.getShiftType());
        shiftRequest.setStatus(shift.getStatus());

        WorkAreaRequest workAreaRequest = new WorkAreaRequest();
        WorkArea workArea = shift.getWorkArea();
        if (workArea != null) {
            workAreaRequest.setWorkAreaID(workArea.getWorkAreaID());
            workAreaRequest.setRegister(workArea.getRegister());
            workAreaRequest.setDescription(workArea.getDescription());
        }

        shiftRequest.setWorkArea(workAreaRequest);
        return shiftRequest;
    }

    // Read all Shifts
    public List<ShiftRequest> readAllShifts() {
        List<Shift> shifts = shiftRepository.findAll();
        return shifts.stream()
                .map(this::convertToShiftRequest)
                .collect(Collectors.toList());
    }

    // Read Shift by ID
    public ShiftRequest getShiftById(Long id) {
        Optional<Shift> shiftOptional = shiftRepository.findById(id);
        if (shiftOptional.isPresent()) {
            Shift shift = shiftOptional.get();
            return convertToShiftRequest(shift);
        } else {
            throw new RuntimeException("Shift ID: " + id + " not found");
        }
    }

    // Update Shift details
    @Transactional
    public void updateShiftDetails(ShiftRequest shiftRequest) {
        Optional<Shift> shiftOptional = shiftRepository.findById((long) shiftRequest.getShiftID());
        if (shiftOptional.isPresent()) {
            Shift shift = shiftOptional.get();

            LocalDateTime startTime = LocalDateTime.parse(shiftRequest.getStartTime(), DATE_TIME_FORMATTER);
            LocalDateTime endTime = LocalDateTime.parse(shiftRequest.getEndTime(), DATE_TIME_FORMATTER);

            shift.setEndTime(endTime);
            shift.setShiftType(shiftRequest.getShiftType());
            shift.setStartTime(startTime);
            shift.setStatus(shiftRequest.getStatus());

            // Convert WorkAreaRequest to WorkArea entity
            WorkArea workArea = findOrCreateWorkArea(shiftRequest.getWorkArea());

            // Save or update WorkArea
            workArea = workAreaRepository.save(workArea);

            shift.setWorkArea(workArea);

            shiftRepository.save(shift);
        } else {
            throw new RuntimeException("Shift with ID " + shiftRequest.getShiftID() + " not found");
        }
    }

    // Delete Shift by ID
//    @Transactional
//    public void deleteShiftById(Long id) {
//        Optional<Shift> shiftOptional = shiftRepository.findById(id);
//        if (shiftOptional.isPresent()) {
//            shiftRepository.deleteById(id);
//        } else {
//            throw new RuntimeException("Shift ID: " + id + " not found");
//        }
//    }

    // Update Shift status to inactive by ID
    @Transactional
    public void updateShiftStatusToInactiveById(Long id) {
        Optional<Shift> shiftOptional = shiftRepository.findById(id);
        if (shiftOptional.isPresent()) {
            Shift shift = shiftOptional.get();
            shift.setStatus("inactive"); // Set the status to inactive
            shiftRepository.save(shift); // Save the updated shift
        } else {
            throw new RuntimeException("Shift ID: " + id + " not found");
        }
    }


    // Delete shifts with no staff and older than 2 months
    @Transactional
    public void deleteShiftsWithCriteria() {
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);

        List<Shift> shiftsWithoutStaff = shiftRepository.findShiftsWithoutStaff();
        shiftsWithoutStaff.forEach(shiftRepository::delete);

        List<Shift> shiftsOlderThanTwoMonths = shiftRepository.findShiftsOlderThan(LocalDate.from(twoMonthsAgo));
        shiftsOlderThanTwoMonths.forEach(shiftRepository::delete);
    }

    // Read Shifts by start time
    public List<ShiftRequest> getShiftsByStartTime(String startTimeStr) {
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DATE_TIME_FORMATTER);

        List<Shift> shifts = shiftRepository.findByStartTime(startTime);

        return shifts.stream()
                .map(this::convertToShiftRequest)
                .collect(Collectors.toList());
    }

    // Read Shifts by end time
    public List<ShiftRequest> getShiftsByEndTime(String endTimeStr) {
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, DATE_TIME_FORMATTER);

        List<Shift> shifts = shiftRepository.findByEndTime(endTime);

        return shifts.stream()
                .map(this::convertToShiftRequest)
                .collect(Collectors.toList());
    }

    // Read Shifts by shift type
    public List<ShiftRequest> getShiftsByShiftType(String shiftType) {
        List<Shift> shifts = shiftRepository.findByShiftType(shiftType);

        return shifts.stream()
                .map(this::convertToShiftRequest)
                .collect(Collectors.toList());
    }

    // Read Shifts by status
    public List<ShiftRequest> getShiftsByStatus(String status) {
        List<Shift> shifts = shiftRepository.findByStatus(status);

        return shifts.stream()
                .map(this::convertToShiftRequest)
                .collect(Collectors.toList());
    }

}
