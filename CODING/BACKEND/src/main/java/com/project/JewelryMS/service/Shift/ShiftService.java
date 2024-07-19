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

    // Define a formatter for "yyyy-MM-dd HH:mm"
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Method to create a new shift
    @Transactional
    public ShiftRequest createShift(CreateShiftRequest createShiftRequest) {
        Shift shift = new Shift();
        shift.setStartTime(LocalDateTime.parse(createShiftRequest.getStartTime(), DATE_TIME_FORMATTER));
        shift.setEndTime(LocalDateTime.parse(createShiftRequest.getEndTime(), DATE_TIME_FORMATTER));
        shift.setShiftType(createShiftRequest.getShiftType());
        shift.setStatus(createShiftRequest.getStatus());

        shiftRepository.save(shift);
        return convertToShiftRequest(shift);
    }

    // Convert Shift entity to ShiftRequest DTO
    private ShiftRequest convertToShiftRequest(Shift shift) {
        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setShiftID(shift.getShiftID());
        shiftRequest.setStartTime(shift.getStartTime().format(DATE_TIME_FORMATTER));
        shiftRequest.setEndTime(shift.getEndTime().format(DATE_TIME_FORMATTER));
        shiftRequest.setShiftType(shift.getShiftType());
        shiftRequest.setStatus(shift.getStatus());

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

            shiftRepository.save(shift);
        } else {
            throw new RuntimeException("Shift with ID " + shiftRequest.getShiftID() + " not found");
        }
    }

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
    //De-activate 2 month shifts deletion
//        List<Shift> shiftsOlderThanTwoMonths = shiftRepository.findShiftsOlderThan(LocalDate.from(twoMonthsAgo));
//        shiftsOlderThanTwoMonths.forEach(shiftRepository::delete);
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