package com.project.JewelryMS.service.Shift;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.Staff_Shift;
import com.project.JewelryMS.entity.WorkArea;
import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.model.Shift.UpdateStaffWorkAreaRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.model.StaffShift.StaffShiftResponse;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import com.project.JewelryMS.repository.StaffShiftRepository;
import com.project.JewelryMS.repository.WorkAreaRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
@EnableScheduling
public class SchedulingService {

    private static final int THREAD_POOL_SIZE = 100;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    @Autowired
    private StaffAccountRepository staffAccountRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private StaffShiftRepository staffShiftRepository;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private WorkAreaRepository workAreaRepository;

    @Transactional
    public UpdateStaffWorkAreaRequest updateStaffWorkArea(UpdateStaffWorkAreaRequest request) {
        // Fetch the staff entity from the database
        StaffAccount staff = staffAccountRepository.findById(request.getStaffId())
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + request.getStaffId()));

        // Fetch the new work area entity from the database
        WorkArea newWorkArea = workAreaRepository.findByWorkAreaCode(request.getWorkAreaCode())
                .orElseThrow(() -> new EntityNotFoundException("Work area not found with code: " + request.getWorkAreaCode()));

        // Check if the new work area is already occupied by another staff member
        if (newWorkArea.getStaffAccounts().stream()
                .anyMatch(existingStaff -> !existingStaff.getStaffID().equals(staff.getStaffID()))) {
            throw new IllegalStateException("Work area is already occupied by another staff member.");
        }

        // Update the staff's work area
        staff.setWorkArea(newWorkArea);

        // Save the updated StaffAccount entity to the database
        staffAccountRepository.save(staff);

        // Create and return the response DTO
        return new UpdateStaffWorkAreaRequest(staff.getStaffID(), newWorkArea.getWorkAreaCode());
    }


    @Transactional
    public void switchStaffWorkArea(Integer staffId1, Integer staffId2) {
        // Fetch the first staff entity from the database
        StaffAccount staff1 = staffAccountRepository.findById(staffId1)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + staffId1));

        // Fetch the second staff entity from the database
        StaffAccount staff2 = staffAccountRepository.findById(staffId2)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + staffId2));

        // Get the work areas assigned to each staff member
        WorkArea workArea1 = staff1.getWorkArea();
        WorkArea workArea2 = staff2.getWorkArea();

        // Switch the work areas
        staff1.setWorkArea(workArea2);
        staff2.setWorkArea(workArea1);

        // Save the updated StaffAccount entities to the database
        staffAccountRepository.save(staff1);
        staffAccountRepository.save(staff2);
    }

    @Transactional
    public Staff_Shift assignStaffToShift(int staffId, long shiftId) {
        // Fetch the staff entity from the database
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + staffId));

        // Check if the staff has a work area assigned
        if (staff.getWorkArea() == null || staff.getWorkArea().getWorkAreaCode() == null) {
            throw new RuntimeException("Staff does not have a work area assigned. Please update their work area ID.");
        }

        // Fetch the shift entity from the database
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + shiftId));

        // Initialize collections to avoid lazy loading issues
        Hibernate.initialize(staff.getStaffShifts());
        Hibernate.initialize(shift.getStaffShifts());

        // Check if the staff member is already assigned to the shift
        boolean isAlreadyAssigned = staff.getStaffShifts().stream()
                .anyMatch(ss -> ss.getShift().equals(shift));
        if (isAlreadyAssigned) {
            throw new RuntimeException("Staff is already assigned to this shift");
        }

        // Create a new Staff_Shift entity
        Staff_Shift staffShift = new Staff_Shift();
        staffShift.setStaffAccount(staff);
        staffShift.setShift(shift);
        staffShift.setWorkArea(staff.getWorkArea()); // Set the work area to match the staff's work area
        staffShift.setAttendanceStatus("Not yet"); // Set the initial attendance status
        // Save the new Staff_Shift entity
        staffShift = staffShiftRepository.save(staffShift);

        return staffShift;
    }

    // Method to assign a shift to a staff member
    @Transactional
    public Staff_Shift assignShiftToStaff(int shiftId, int staffId) {
        return assignStaffToShift(staffId, shiftId);
    }

    // Method to view schedule
    @Transactional
    public Map<String, Map<String, List<StaffShiftResponse>>> getScheduleMatrix(LocalDate startDate, LocalDate endDate) {
        String[] shiftTypes = {"Morning", "Afternoon", "Evening"};
        Map<String, Map<String, List<StaffShiftResponse>>> matrix = new ConcurrentHashMap<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        // Fetch all shifts within the date range
        List<Shift> shifts = shiftRepository.findAllByStartTimeBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay().minusNanos(1));

        // Update status of past shifts
        LocalDateTime now = LocalDateTime.now();
        for (Shift shift : shifts) {
            if (shift.getEndTime().isBefore(now)) {
                for (Staff_Shift staffShift : shift.getStaffShifts()) {
                    if ("Not yet".equals(staffShift.getAttendanceStatus())) {
                        staffShift.setAttendanceStatus("Absent");
                        staffShiftRepository.save(staffShift);
                    }
                }
            }
        }

        // Cleanup shifts without staff
        cleanupShifts(shifts);

        // Group shifts by date and type
        Map<LocalDate, Map<String, List<Shift>>> groupedShifts = shifts.stream()
                .collect(Collectors.groupingBy(shift -> shift.getStartTime().toLocalDate(),
                        Collectors.groupingBy(Shift::getShiftType)));

        // Process grouped shifts to construct the schedule matrix
        groupedShifts.forEach((date, typeShiftMap) -> {
            String formattedDate = date.format(dateFormatter);
            typeShiftMap.forEach((shiftType, shiftsOfType) -> {
                List<StaffShiftResponse> shiftResponses = new ArrayList<>();

                shiftsOfType.forEach(shift -> {
                    String formattedStartTime = shift.getStartTime().format(timeFormatter);
                    String formattedEndTime = shift.getEndTime().format(timeFormatter);

                    List<StaffShiftResponse.StaffResponse> staffResponses = shift.getStaffShifts().stream()
                            .map(staffShift -> new StaffShiftResponse.StaffResponse(
                                    staffShift.getStaffAccount().getStaffID(),
                                    staffShift.getStaffAccount().getAccount().getAccountName(),
                                    staffShift.getStaffAccount().getAccount().getEmail(),
                                    staffShift.getStaffAccount().getAccount().getUsername(),
                                    staffShift.getWorkArea().getWorkAreaCode(),
                                    staffShift.getAttendanceStatus()
                            ))
                            .collect(Collectors.toList());

                    StaffShiftResponse staffShiftResponse = new StaffShiftResponse(
                            shift.getShiftID(),
                            formattedStartTime,
                            formattedEndTime,
                            shiftType,
                            shift.getStatus(),
                            staffResponses
                    );
                    shiftResponses.add(staffShiftResponse);
                });

                matrix.computeIfAbsent(formattedDate, k -> new ConcurrentHashMap<>())
                        .put(shiftType, shiftResponses);
            });
        });

        return matrix;
    }

    @Transactional
    public void cleanupShifts(List<Shift> shifts) {
        // Remove shifts without staff
        shifts.removeIf(shift -> {
            if (shift.getStaffShifts().isEmpty()) {
                // Delete the shift from the database
                shiftRepository.delete(shift);
                return true;
            }
            return false;
        });
    }

    @Transactional
    public void updateStatusOfPastShifts(List<Shift> shifts) {
        LocalDate today = LocalDate.now();
        shifts.forEach(shift -> {
            if (shift.getEndTime().toLocalDate().isBefore(today)) {
                shift.setStatus("Inactive");
                shiftRepository.save(shift);
            }
        });
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
        Shift shift = shiftRepository.findById(shiftId)
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

    @Transactional
    public StaffShiftResponse assignStaffToDay(int staffId, LocalDate date, String shiftType) {
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new ShiftAssignmentException("Staff not found"));

        // Check if the staff's work area ID is null
        if (staff.getWorkArea() == null || staff.getWorkArea().getWorkAreaCode() == null) {
            throw new ShiftAssignmentException("Staff does not have a work area assigned. Please assign a work area ID for the staff.");
        }

        try {
            // Check if the staff member is already assigned to any shift on the same day
            boolean isAssigned = staff.getStaffShifts().stream()
                    .anyMatch(ss -> ss.getShift().getStartTime().toLocalDate().equals(date));

            if (isAssigned) {
                throw new ShiftAssignmentException("Staff is already assigned to a shift on this day. Please choose a different day or staff member.");
            }

            // Find a shift on the specified date and period
            Shift shift = shiftRepository.findAllByDateAndType(date, shiftType).stream()
                    .findFirst()
                    .orElse(null);

            // If no shift exists, create a new one using the ShiftService
            if (shift == null) {
                CreateShiftRequest createShiftRequest = new CreateShiftRequest();
                createShiftRequest.setShiftType(shiftType);
                createShiftRequest.setStatus("Active");

                // Define the start and end times for each shift type
                String startTime, endTime;
                endTime = switch (shiftType) {
                    case "Morning" -> {
                        startTime = date.atTime(8, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        yield date.atTime(12, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    }
                    case "Afternoon" -> {
                        startTime = date.atTime(13, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        yield date.atTime(17, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    }
                    case "Evening" -> {
                        startTime = date.atTime(17, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        yield date.atTime(21, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    }
                    default -> throw new RuntimeException("Invalid shift type");
                };

                createShiftRequest.setStartTime(startTime);
                createShiftRequest.setEndTime(endTime);

                ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
                shift = shiftRepository.findById((long) createdShift.getShiftID())
                        .orElseThrow(() -> new RuntimeException("Shift not found after creation"));
            }

            // Create a new Staff_Shift entity
            Staff_Shift staffShift = new Staff_Shift();
            staffShift.setStaffAccount(staff);
            staffShift.setShift(shift);
            staffShift.setWorkArea(staff.getWorkArea()); // Set the work area to match the staff's work area
            staffShift.setAttendanceStatus("Not yet"); // Set the initial attendance status
            staffShift = staffShiftRepository.save(staffShift);

            return toStaffShiftResponse(staffShift);
        } catch (Exception e) {
            // Rollback the transaction on any exception
            throw new RuntimeException("Failed to assign staff to shift: " + e.getMessage(), e);
        }
    }

    private StaffShiftResponse toStaffShiftResponse(Staff_Shift staffShift) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh a");
        String formattedStartTime = staffShift.getShift().getStartTime().format(timeFormatter);
        String formattedEndTime = staffShift.getShift().getEndTime().format(timeFormatter);

        StaffShiftResponse.StaffResponse staffResponse = new StaffShiftResponse.StaffResponse(
                staffShift.getStaffAccount().getStaffID(),
                staffShift.getStaffAccount().getAccount().getAccountName(),
                staffShift.getStaffAccount().getAccount().getEmail(),
                staffShift.getStaffAccount().getAccount().getUsername(),
                staffShift.getStaffAccount().getWorkArea().getWorkAreaCode(),
                staffShift.getAttendanceStatus()  // Include attendance status
        );

        return new StaffShiftResponse(
                staffShift.getShift().getShiftID(),
                formattedStartTime,
                formattedEndTime,
                staffShift.getShift().getShiftType(),
                staffShift.getShift().getStatus(),
                Collections.singletonList(staffResponse)
        );
    }

    // Custom exception class
    private static class ShiftAssignmentException extends RuntimeException {
        public ShiftAssignmentException(String message) {
            super(message);
        }
    }

    @Transactional
    public List<StaffShiftResponse> assignStaffToDateRange(List<Integer> staffIds, LocalDate startDate, LocalDate endDate, List<String> shiftTypes) {
        List<StaffShiftResponse> staffShiftResponses = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<LocalDate> dates = startDate.datesUntil(endDate.plusDays(1)).toList();

        List<Future<?>> futures = new ArrayList<>();
        for (LocalDate date : dates) {
            for (String shiftType : shiftTypes) {
                for (int staffId : staffIds) {
                    futures.add(executorService.submit(() -> {
                        try {
                            StaffShiftResponse response = assignStaffToDay(staffId, date, shiftType);
                            if (response != null) {
                                synchronized (staffShiftResponses) {
                                    staffShiftResponses.add(response);
                                }
                            }
                        } catch (Exception e) {
                            // Log or handle exceptions
                            e.printStackTrace(); // Example logging, customize as needed
                        }
                    }));
                }
            }
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
        return staffShiftResponses;
    }

    //This can be used again, if necessary
//    @Transactional
//    public List<StaffShiftResponse> assignStaffByDayOfWeek(
//            Map<Integer, Map<DayOfWeek, List<String>>> staffAvailability,
//            LocalDate startDate,
//            LocalDate endDate) {
//
//        List<StaffShiftResponse> staffShiftResponses = new ArrayList<>();
//
//        // Iterate over each date in the range
//        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
//            DayOfWeek currentDayOfWeek = date.getDayOfWeek();
//
//            for (Map.Entry<Integer, Map<DayOfWeek, List<String>>> entry : staffAvailability.entrySet()) {
//                int staffId = entry.getKey();
//                Map<DayOfWeek, List<String>> availability = entry.getValue();
//
//                // Check if the current day of the week is in the staff's availability
//                if (availability.containsKey(currentDayOfWeek)) {
//                    List<String> shiftTypes = availability.get(currentDayOfWeek);
//
//                    for (String shiftType : shiftTypes) {
//                        try {
//                            StaffShiftResponse response = assignStaffToDay(staffId, date, shiftType);
//                            staffShiftResponses.add(response);
//                        } catch (ShiftAssignmentException e) {
//                            // Log and continue if staff is already assigned
//                            System.out.println("Staff ID " + staffId + " is already assigned on " + date + " for " + shiftType + " shift.");
//                        }
//                    }
//                }
//            }
//        }
//        return staffShiftResponses;
//    }

    @Transactional
    public void removeStaffFromShiftsInRange(int staffId, LocalDate startDate, LocalDate endDate) {
        // Validate staff existence
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        // Retrieve all shifts for the staff within the date range
        List<Staff_Shift> staffShifts = staffShiftRepository.findAllByStaffAccountAndShift_StartTimeBetween(
                staff,
                startDate.atStartOfDay(), // Start of day
                endDate.plusDays(1).atStartOfDay().minusNanos(1) // End of day
        );

        // Remove each staff shift found
        staffShiftRepository.deleteAll(staffShifts);
    }

    @Transactional
    public void removeAllStaffFromShiftsInRange(LocalDate startDate, LocalDate endDate) {
        // Retrieve all shifts within the date range
        List<Shift> shifts = shiftRepository.findAllByStartTimeBetween(
                startDate.atStartOfDay(), // Start of day
                endDate.plusDays(1).atStartOfDay().minusNanos(1) // End of day
        );

        for (Shift shift : shifts) {
            // Retrieve all staff shifts for the current shift
            List<Staff_Shift> staffShifts = new ArrayList<>(shift.getStaffShifts());

            // Clear the association between shift and staff
            for (Staff_Shift staffShift : staffShifts) {
                // Remove the reference to shift
                staffShift.setShift(null);
                // Delete the staff shift entry from the database
                staffShiftRepository.delete(staffShift);
            }

            // Clear the staff shifts collection from the shift entity
            shift.getStaffShifts().clear();

            // Save the updated shift entity
            shiftRepository.save(shift);
        }
    }

    //A modified version of the shift Type pattern, modified by demand by the front end team
    @Transactional
    public List<StaffAccountResponse> assignStaffByShiftTypePattern(
            Map<String, List<Integer>> staffShiftPatterns, LocalDate startDate, LocalDate endDate) {

        List<StaffShiftResponse> staffShiftResponses = new CopyOnWriteArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        List<LocalDate> dates = IntStream.range(0, totalDays)
                .mapToObj(startDate::plusDays)
                .filter(date -> date.getDayOfWeek() != DayOfWeek.SUNDAY)
                .toList();

        List<Future<?>> futures = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry : staffShiftPatterns.entrySet()) {
            String shiftType = entry.getKey();
            List<Integer> staffIds = entry.getValue();

            for (int staffId : staffIds) {
                for (LocalDate date : dates) {
                    futures.add(executorService.submit(() -> {
                        try {
                            StaffShiftResponse response = assignStaffToDayWithExistingShiftCheck(staffId, date, shiftType);
                            staffShiftResponses.add(response);
                        } catch (ShiftAssignmentException e) {
                            System.out.println("Staff ID " + staffId + " is already assigned on " + date + " for " + shiftType + " shift.");
                        }
                    }));
                }
            }
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();  // Handle exception
            }
        }
        executorService.shutdown();

        // Collect unique staff IDs from the responses
        Set<Integer> staffIds = staffShiftResponses.stream()
                .map(StaffShiftResponse::getStaff)
                .flatMap(List::stream)
                .map(StaffShiftResponse.StaffResponse::getStaffID)
                .collect(Collectors.toSet());

        // Fetch staff accounts eagerly and map to responses
        List<StaffAccount> staffAccounts = staffAccountRepository.findAllByIdWithWorkAreaEagerly(staffIds); // Custom repository method
        return staffAccounts.stream()
                .map(this::mapToStaffAccountResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public StaffShiftResponse assignStaffToDayWithExistingShiftCheck(int staffId, LocalDate date, String shiftType) {
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new ShiftAssignmentException("Staff not found"));

        // Check if the staff's work area ID is null
        if (staff.getWorkArea() == null || staff.getWorkArea().getWorkAreaCode() == null) {
            throw new ShiftAssignmentException("Staff does not have a work area assigned. Please assign a work area ID for the staff.");
        }

        // Check if a shift of the specified type already exists on the date
        Optional<Shift> optionalShift = shiftRepository.findByDateAndType(date, shiftType);
        Shift shift;
        if (optionalShift.isPresent()) {
            shift = optionalShift.get();
        } else {
            CreateShiftRequest createShiftRequest = new CreateShiftRequest();
            createShiftRequest.setShiftType(shiftType);
            createShiftRequest.setStatus("Active");

            // Define the start and end times for each shift type
            String startTime, endTime;
            endTime = switch (shiftType) {
                case "Morning" -> {
                    startTime = date.atTime(8, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    yield date.atTime(12, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                }
                case "Afternoon" -> {
                    startTime = date.atTime(13, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    yield date.atTime(17, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                }
                case "Evening" -> {
                    startTime = date.atTime(17, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    yield date.atTime(21, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                }
                default -> throw new RuntimeException("Invalid shift type");
            };

            createShiftRequest.setStartTime(startTime);
            createShiftRequest.setEndTime(endTime);

            ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
            shift = shiftRepository.findById((long) createdShift.getShiftID())
                    .orElseThrow(() -> new RuntimeException("Shift not found after creation"));
        }

        // Add the staff member to the existing shift with the correct work area
        Staff_Shift staffShift = new Staff_Shift();
        staffShift.setStaffAccount(staff);
        staffShift.setShift(shift);
        staffShift.setWorkArea(staff.getWorkArea()); // Set the work area to match the staff's work area
        staffShift.setAttendanceStatus("Not yet"); // Set the initial attendance status
        staffShiftRepository.save(staffShift);

        return toStaffShiftResponse(staffShift);
    }


    // Method to map StaffAccount to StaffAccountResponse
    private StaffAccountResponse mapToStaffAccountResponse(StaffAccount staffAccount) {
        return new StaffAccountResponse(
                staffAccount.getStaffID(),
                staffAccount.getPhoneNumber(),
                staffAccount.getSalary(),
                staffAccount.getStartDate(),
                staffAccount.getAccount().getAccountName(),
                staffAccount.getAccount().getRole(),
                staffAccount.getAccount().getStatus(),
                staffAccount.getAccount().getEmail(),
                staffAccount.getAccount().getUsername(),
                staffAccount.getAccount().getAImage(),
                staffAccount.getStaffShifts().stream()
                        .map(shift -> new StaffAccountResponse.ShiftResponse(
                                shift.getShift().getShiftID(),
                                shift.getShift().getEndTime(),
                                shift.getShift().getShiftType(),
                                shift.getShift().getStartTime(),
                                shift.getShift().getStatus()
                        ))
                        .collect(Collectors.toList())
        );
    }

    //New method for fast assigning
//    Staff with staffID % 3 == 1 get single shifts (morning, afternoon, or evening).
//    Staff with staffID % 3 == 2 get double shifts (morning-afternoon).
//    Staff with staffID % 3 == 0 get double shifts (afternoon-evening).
//    Staff with staffId % 3 == 2 will be assigned shifts on Monday, Wednesday, and Friday.
//    Staff with staffId % 3 == 0 will be assigned shifts on Tuesday, Thursday, and Saturday.

//    If the list contains all staff types (staffId % 3 == 0, 1, 2),
//      the schedule will be filled for the given period except for Sundays.


    private static final Logger logger = LoggerFactory.getLogger(SchedulingService.class);

    @Transactional
    public List<StaffAccountResponse> assignRandomStaffShiftPattern(List<Integer> staffIds, LocalDate startDate, LocalDate endDate) {
        ConcurrentLinkedQueue<StaffShiftResponse> staffShiftResponses = new ConcurrentLinkedQueue<>();
        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        List<LocalDate> dates = IntStream.range(0, totalDays)
                .mapToObj(startDate::plusDays)
                .filter(date -> date.getDayOfWeek() != DayOfWeek.SUNDAY)
                .toList();

        List<Integer> mod0Staff = staffIds.stream().filter(id -> id % 3 == 0).toList();
        List<Integer> mod1Staff = staffIds.stream().filter(id -> id % 3 == 1).toList();
        List<Integer> mod2Staff = staffIds.stream().filter(id -> id % 3 == 2).toList();

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<?>> futures = new ArrayList<>();

        for (LocalDate date : dates) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.FRIDAY) {
                for (int staffId : mod2Staff) {
                    futures.add(assignShift(executorService, staffId, date, "Morning", staffShiftResponses));
                    futures.add(assignShift(executorService, staffId, date, "Afternoon", staffShiftResponses));
                }
            } else if (dayOfWeek == DayOfWeek.TUESDAY || dayOfWeek == DayOfWeek.THURSDAY || dayOfWeek == DayOfWeek.SATURDAY) {
                for (int staffId : mod0Staff) {
                    futures.add(assignShift(executorService, staffId, date, "Afternoon", staffShiftResponses));
                    futures.add(assignShift(executorService, staffId, date, "Evening", staffShiftResponses));
                }
            }

            for (int staffId : mod1Staff) {
                futures.add(assignSingleShift(executorService, staffId, date, staffShiftResponses));
            }
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error while assigning shifts: ", e);
            }
        }

        executorService.shutdown();

        Set<Integer> uniqueStaffIds = staffShiftResponses.stream()
                .map(StaffShiftResponse::getStaff)
                .flatMap(List::stream)
                .map(StaffShiftResponse.StaffResponse::getStaffID)
                .collect(Collectors.toSet());

        List<StaffAccount> staffAccounts = staffAccountRepository.findAllById(uniqueStaffIds);
        return staffAccounts.stream()
                .map(this::mapToStaffAccountResponse)
                .collect(Collectors.toList());
    }
//    @Scheduled(fixedRate = 1209600000) // 2 weeks in milliseconds
//    public void scheduleShiftsAutomatically() {
//        List<Integer> staffIds = getAllStaffIds();
//        LocalDate startDate = LocalDate.now();
//        LocalDate endDate = startDate.plusDays(13); // Schedule for the next two weeks
//
//        assignRandomStaffShiftPattern(staffIds, startDate, endDate);
//    }

    @Scheduled(cron = "0 0 0 * * SAT") // Run every Saturday at midnight
//    @Scheduled(cron = "0 */10 * * * *") // Run every 10 minutes
    public void scheduleShiftsAutomatically() {
        List<Integer> staffIds = getAllStaffIds();
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;

        // Ensure this runs only on Saturdays
        if (today.getDayOfWeek() != DayOfWeek.SATURDAY) {
            return;
        }

        int currentWeek = today.get(WeekFields.ISO.weekOfMonth());
        int currentMonth = today.getMonthValue();

        if (currentWeek == 3) {
            // Generate shifts for week 4 of the current month and weeks 1 and 2 of the next month
            startDate = today.with(TemporalAdjusters.firstDayOfMonth()).plusWeeks(3).with(DayOfWeek.MONDAY);
            endDate = startDate.plusWeeks(2).plusDays(6);
        } else if (currentWeek == 2) {
            // Generate shifts for week 3 of the current month
            startDate = today.with(TemporalAdjusters.firstDayOfMonth()).plusWeeks(2).with(DayOfWeek.MONDAY);
            endDate = startDate.plusDays(6);
        } else if (currentWeek == 1) {
            // Do not generate any shifts in week 1
            return;
        } else {
            // Generate shifts for the next week
            startDate = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            endDate = startDate.plusDays(6);
        }

        // Ensure the dates are within the correct month/year boundaries
        if (endDate.getMonthValue() != currentMonth) {
            endDate = endDate.with(TemporalAdjusters.lastDayOfMonth());
        }

        assignRandomStaffShiftPattern(staffIds, startDate, endDate);
    }

    @PostConstruct
    public void checkAndRunAutomationOnStartup() {
        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek() == DayOfWeek.SATURDAY) {
            scheduleShiftsAutomatically();
        }
    }


    private List<Integer> getAllStaffIds() {
        return staffAccountRepository.findAllStaffAccountsByRoleStaff().stream()
                .map(StaffAccount::getStaffID)
                .collect(Collectors.toList());
    }

    private Future<?> assignShift(ExecutorService executorService, int staffId, LocalDate date, String shiftType, ConcurrentLinkedQueue<StaffShiftResponse> staffShiftResponses) {
        return executorService.submit(() -> {
            try {
                StaffShiftResponse response = assignStaffToDay(staffId, date, shiftType);
                staffShiftResponses.add(response);
            } catch (ShiftAssignmentException e) {
                logger.warn("Staff ID {} is already assigned on {}.", staffId, date);
            } catch (Exception e) {
                logger.error("Failed to assign shift for staff ID {} on {}: {}", staffId, date, e.getMessage(), e);
            }
        });
    }

    private Future<?> assignSingleShift(ExecutorService executorService, int staffId, LocalDate date, ConcurrentLinkedQueue<StaffShiftResponse> staffShiftResponses) {
        return executorService.submit(() -> {
            try {
                String shiftType = getRandomSingleShiftType();
                StaffShiftResponse response = assignStaffToDay(staffId, date, shiftType);
                staffShiftResponses.add(response);
            } catch (ShiftAssignmentException e) {
                logger.warn("Staff ID {} is already assigned on {}.", staffId, date);
            } catch (Exception e) {
                logger.error("Failed to assign single shift for staff ID {} on {}: {}", staffId, date, e.getMessage(), e);
            }
        });
    }

    private String getRandomSingleShiftType() {
        List<String> singleShiftTypes = Arrays.asList("Morning", "Afternoon", "Evening");
        Collections.shuffle(singleShiftTypes);
        return singleShiftTypes.get(0);
    }
}