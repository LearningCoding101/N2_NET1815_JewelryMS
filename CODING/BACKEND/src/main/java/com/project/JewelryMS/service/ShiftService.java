package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Shift;

import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.DeleteShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffShiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
//import java.security.Timestamp;
import java.util.stream.Collectors;

@Service
public class ShiftService {
        @Autowired
        ShiftRepository shiftRepository;

        // Helper method to convert a Shift entity to a ShiftRequest DTO
        private ShiftRequest toShiftRequest(Shift shift) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH");

            String formattedStartDate = shift.getStartTime().toLocalDate().format(dateFormatter);
            String formattedStartTime = shift.getStartTime().toLocalTime().format(timeFormatter);
            String dayOfWeek = shift.getStartTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            String formattedEndDate = shift.getEndTime().toLocalDate().format(dateFormatter);
            String formattedEndTime = shift.getEndTime().toLocalTime().format(timeFormatter);

            return new ShiftRequest(
                    shift.getShiftID(),
                    formattedEndDate + " " + formattedEndTime,
                    shift.getRegister(),
                    shift.getShiftType(),
                    formattedStartDate + " " + formattedStartTime + " (" + dayOfWeek + ")",
                    shift.getStatus(),
                    shift.getWorkArea()
            );
        }

        // Create Shift
        public ShiftRequest createShift(CreateShiftRequest createShiftRequest) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

            LocalDateTime startTime = LocalDateTime.parse(createShiftRequest.getStartTime(), formatter);
            LocalDateTime endTime = LocalDateTime.parse(createShiftRequest.getEndTime(), formatter);

            Shift newShift = new Shift();

            newShift.setEndTime(endTime);
            newShift.setRegister(createShiftRequest.getRegister());
            newShift.setShiftType(createShiftRequest.getShiftType());
            newShift.setStartTime(startTime);
            newShift.setStatus(createShiftRequest.getStatus());
            newShift.setWorkArea(createShiftRequest.getWorkArea());

            Shift newShiftAddition = shiftRepository.save(newShift);
            return toShiftRequest(newShiftAddition);
        }

        // Read Shifts by start time
        public List<ShiftRequest> getShiftsByStartTime(String startTimeStr) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);

            List<Shift> shifts = shiftRepository.findByStartTime(startTime);

            return shifts.stream()
                    .map(this::toShiftRequest) // This will apply the date formatting
                    .collect(Collectors.toList());
        }

        // Read Shifts by end time
        public List<ShiftRequest> getShiftsByEndTime(String endTimeStr) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

            List<Shift> shifts = shiftRepository.findByEndTime(endTime);

            return shifts.stream()
                    .map(this::toShiftRequest) // This will apply the date formatting
                    .collect(Collectors.toList());
        }

        // Read Shifts by shift type
        public List<ShiftRequest> getShiftsByShiftType(String shiftType) {
            List<Shift> shifts = shiftRepository.findByShiftType(shiftType);

            return shifts.stream()
                    .map(this::toShiftRequest) // This will apply the date formatting
                    .collect(Collectors.toList());
        }

        // Read Shifts by status
        public List<ShiftRequest> getShiftsByStatus(String status) {
            List<Shift> shifts = shiftRepository.findByStatus(status);

            return shifts.stream()
                    .map(this::toShiftRequest) // This will apply the date formatting
                    .collect(Collectors.toList());
        }

        // Read Shifts by register
        public List<ShiftRequest> getShiftsByRegister(int register) {
            List<Shift> shifts = shiftRepository.findByRegister(register);

            return shifts.stream()
                    .map(this::toShiftRequest) // This will apply the date formatting
                    .collect(Collectors.toList());
        }

        // Read Shifts by work area
        public List<ShiftRequest> getShiftsByWorkArea(String workArea) {
            List<Shift> shifts = shiftRepository.findByWorkArea(workArea);

            return shifts.stream()
                    .map(this::toShiftRequest) // This will apply the date formatting
                    .collect(Collectors.toList());
        }

        // Read all Shifts
        public List<ShiftRequest> readAllShifts() {
            List<Shift> shifts = shiftRepository.findAll();

            return shifts.stream()
                    .map(this::toShiftRequest) // This will apply the date formatting
                    .collect(Collectors.toList());
        }

        // Read Shift by ID
        public ShiftRequest getShiftById(Integer id) {
            Optional<Shift> shiftOptional = shiftRepository.findById((long) id);
            if (shiftOptional.isPresent()) {
                Shift shift = shiftOptional.get();
                return toShiftRequest(shift); // This will apply the date formatting
            } else {
                throw new RuntimeException("Shift ID:  " + id + " not found");
            }
        }


        // Update Shift details
        public Shift updateShiftDetails(ShiftRequest shiftRequest) {
            Optional<Shift> shiftUpdate = shiftRepository.findById((long) shiftRequest.getShiftID());

            if (shiftUpdate.isPresent()) {
                Shift shift = shiftUpdate.get();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
                LocalDateTime startTime = LocalDateTime.parse(shiftRequest.getStartTime(), formatter);
                LocalDateTime endTime = LocalDateTime.parse(shiftRequest.getEndTime(), formatter);

                shift.setEndTime(endTime);
                shift.setRegister(shiftRequest.getRegister());
                shift.setShiftType(shiftRequest.getShiftType());
                shift.setStartTime(startTime);
                shift.setStatus(shiftRequest.getStatus());
                shift.setWorkArea(shiftRequest.getWorkArea());

                return shiftRepository.save(shift);
            } else {
                throw new RuntimeException("Staff shift with ID " + shiftRequest.getShiftID() + " not found");
            }
        }

        // Delete Shift by ID
        public void deleteShiftById(Long id) {
            Optional<Shift> shiftOptional = shiftRepository.findById(id);
            if (shiftOptional.isPresent()) {
                Shift shift = shiftOptional.get();
                if (shift != null) {
                    String status = "Unapplicable";
                    shift.setStatus(status);
                    shiftRepository.save(shift);
                } else {
                    throw new RuntimeException("Shift ID:  " + id + " not found");
                }
            } else {
                throw new RuntimeException("Shift ID" + id + " not found");
            }
        }

        // Method to delete shifts with no staff and older than 2 months
        public void deleteShiftsWithCriteria() {
            // Calculate 2 months ago
            LocalDate twoMonthsAgo = LocalDate.now().minusMonths(3);

            // Fetch shifts without staff assigned
            List<Shift> shiftsWithoutStaff = shiftRepository.findShiftsWithoutStaff();
            shiftsWithoutStaff.forEach(shiftRepository::delete);

            // Fetch shifts older than 2 months
            List<Shift> shiftsOlderThanTwoMonths = shiftRepository.findShiftsOlderThan(twoMonthsAgo.withDayOfMonth(1));
            shiftsOlderThanTwoMonths.forEach(shiftRepository::delete);
        }
}