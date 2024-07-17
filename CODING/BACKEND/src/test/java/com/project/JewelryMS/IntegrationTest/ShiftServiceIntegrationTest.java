package com.project.JewelryMS.IntegrationTest;

import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.service.ShiftService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.springframework.transaction.annotation.Transactional;

//
//@SpringBootTest
//@Transactional
//public class ShiftServiceIntegrationTest {
//
//    @Autowired
//    private ShiftService shiftService;
//
//    // Test for creating a new shift
//    @Test
//    public void testCreateShift() {
//        CreateShiftRequest createShiftRequest = new CreateShiftRequest();
//        createShiftRequest.setStartTime("2024-07-08 09");
//        createShiftRequest.setEndTime("2024-07-08 17");
//        createShiftRequest.setRegister(1);
//        createShiftRequest.setShiftType("Morning");
//        createShiftRequest.setStatus("Active");
//        createShiftRequest.setWorkArea("Front Desk");
//
//        ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
//
//        Assertions.assertEquals("2024-07-08 17", createdShift.getEndTime());
//        Assertions.assertEquals("Morning", createdShift.getShiftType());
//        Assertions.assertEquals("Active", createdShift.getStatus());
//        Assertions.assertEquals("Front Desk", createdShift.getWorkArea());
//    }
//
//    // Test for reading shifts by start time
//    @Test
//    public void testGetShiftsByStartTime() {
//        // Create shifts for testing
//        CreateShiftRequest createShiftRequest1 = new CreateShiftRequest();
//        createShiftRequest1.setStartTime("2024-07-08 09");
//        createShiftRequest1.setEndTime("2024-07-08 17");
//        createShiftRequest1.setRegister(1);
//        createShiftRequest1.setShiftType("Morning");
//        createShiftRequest1.setStatus("Active");
//        createShiftRequest1.setWorkArea("Front Desk");
//        shiftService.createShift(createShiftRequest1);
//
//        CreateShiftRequest createShiftRequest2 = new CreateShiftRequest();
//        createShiftRequest2.setStartTime("2024-07-08 13");
//        createShiftRequest2.setEndTime("2024-07-08 21");
//        createShiftRequest2.setRegister(2);
//        createShiftRequest2.setShiftType("Afternoon");
//        createShiftRequest2.setStatus("Active");
//        createShiftRequest2.setWorkArea("Back Office");
//        shiftService.createShift(createShiftRequest2);
//
//        // Test fetching shifts by start time
//        List<ShiftRequest> shifts = shiftService.getShiftsByStartTime("2024-07-08 09");
//
//        Assertions.assertFalse(shifts.isEmpty());
//        Assertions.assertEquals(1, shifts.size()); // Assuming only one shift starts at 09:00 AM on the given date
//        Assertions.assertEquals("Morning", shifts.get(0).getShiftType());
//    }
//
//    // Test for reading shifts by end time
//    @Test
//    public void testGetShiftsByEndTime() {
//        // Create shifts for testing
//        CreateShiftRequest createShiftRequest1 = new CreateShiftRequest();
//        createShiftRequest1.setStartTime("2024-07-08 09");
//        createShiftRequest1.setEndTime("2024-07-08 17");
//        createShiftRequest1.setRegister(1);
//        createShiftRequest1.setShiftType("Morning");
//        createShiftRequest1.setStatus("Active");
//        createShiftRequest1.setWorkArea("Front Desk");
//        shiftService.createShift(createShiftRequest1);
//
//        CreateShiftRequest createShiftRequest2 = new CreateShiftRequest();
//        createShiftRequest2.setStartTime("2024-07-08 13");
//        createShiftRequest2.setEndTime("2024-07-08 21");
//        createShiftRequest2.setRegister(2);
//        createShiftRequest2.setShiftType("Afternoon");
//        createShiftRequest2.setStatus("Active");
//        createShiftRequest2.setWorkArea("Back Office");
//        shiftService.createShift(createShiftRequest2);
//
//        // Test fetching shifts by end time
//        List<ShiftRequest> shifts = shiftService.getShiftsByEndTime("2024-07-08 21");
//
//        Assertions.assertFalse(shifts.isEmpty());
//        Assertions.assertEquals(1, shifts.size()); // Assuming only one shift ends at 21:00 on the given date
//        Assertions.assertEquals("Afternoon", shifts.get(0).getShiftType());
//    }
//
//    // Test for reading shifts by shift type
//    @Test
//    public void testGetShiftsByShiftType() {
//        // Create shifts for testing
//        CreateShiftRequest createShiftRequest1 = new CreateShiftRequest();
//        createShiftRequest1.setStartTime("2024-07-08 09");
//        createShiftRequest1.setEndTime("2024-07-08 17");
//        createShiftRequest1.setRegister(1);
//        createShiftRequest1.setShiftType("Morning");
//        createShiftRequest1.setStatus("Active");
//        createShiftRequest1.setWorkArea("Front Desk");
//        shiftService.createShift(createShiftRequest1);
//
//        CreateShiftRequest createShiftRequest2 = new CreateShiftRequest();
//        createShiftRequest2.setStartTime("2024-07-08 13");
//        createShiftRequest2.setEndTime("2024-07-08 21");
//        createShiftRequest2.setRegister(2);
//        createShiftRequest2.setShiftType("Morning");
//        createShiftRequest2.setStatus("Active");
//        createShiftRequest2.setWorkArea("Back Office");
//        shiftService.createShift(createShiftRequest2);
//
//        // Test fetching shifts by shift type "Morning"
//        List<ShiftRequest> shifts = shiftService.getShiftsByShiftType("Morning");
//
//        Assertions.assertFalse(shifts.isEmpty());
//        Assertions.assertEquals(2, shifts.size()); // Assuming two shifts are of type "Morning"
//        // Add more specific assertions if needed
//    }
//
//    // Test for reading shifts by status
//    @Test
//    public void testGetShiftsByStatus() {
//        // Create shifts for testing
//        CreateShiftRequest createShiftRequest1 = new CreateShiftRequest();
//        createShiftRequest1.setStartTime("2024-07-08 09");
//        createShiftRequest1.setEndTime("2024-07-08 17");
//        createShiftRequest1.setRegister(1);
//        createShiftRequest1.setShiftType("Morning");
//        createShiftRequest1.setStatus("Active");
//        createShiftRequest1.setWorkArea("Front Desk");
//        shiftService.createShift(createShiftRequest1);
//
//        CreateShiftRequest createShiftRequest2 = new CreateShiftRequest();
//        createShiftRequest2.setStartTime("2024-07-08 13");
//        createShiftRequest2.setEndTime("2024-07-08 21");
//        createShiftRequest2.setRegister(2);
//        createShiftRequest2.setShiftType("Afternoon");
//        createShiftRequest2.setStatus("Inactive");
//        createShiftRequest2.setWorkArea("Back Office");
//        shiftService.createShift(createShiftRequest2);
//
//        // Test fetching shifts by status "Active"
//        List<ShiftRequest> shifts = shiftService.getShiftsByStatus("Active");
//
//        Assertions.assertFalse(shifts.isEmpty());
//        Assertions.assertEquals(1, shifts.size()); // Assuming only one shift is "Active"
//        Assertions.assertEquals("Morning", shifts.get(0).getShiftType());
//    }
//
//    // Test for reading shifts by register
//    @Test
//    public void testGetShiftsByRegister() {
//        // Create shifts for testing
//        CreateShiftRequest createShiftRequest1 = new CreateShiftRequest();
//        createShiftRequest1.setStartTime("2024-07-08 09");
//        createShiftRequest1.setEndTime("2024-07-08 17");
//        createShiftRequest1.setRegister(1);
//        createShiftRequest1.setShiftType("Morning");
//        createShiftRequest1.setStatus("Active");
//        createShiftRequest1.setWorkArea("Front Desk");
//        shiftService.createShift(createShiftRequest1);
//
//        CreateShiftRequest createShiftRequest2 = new CreateShiftRequest();
//        createShiftRequest2.setStartTime("2024-07-08 13");
//        createShiftRequest2.setEndTime("2024-07-08 21");
//        createShiftRequest2.setRegister(2);
//        createShiftRequest2.setShiftType("Afternoon");
//        createShiftRequest2.setStatus("Active");
//        createShiftRequest2.setWorkArea("Back Office");
//        shiftService.createShift(createShiftRequest2);
//
//        // Test fetching shifts by register number 2
//        List<ShiftRequest> shifts = shiftService.getShiftsByRegister(2);
//
//        Assertions.assertFalse(shifts.isEmpty());
//        Assertions.assertEquals(1, shifts.size()); // Assuming only one shift is registered under 2
//        Assertions.assertEquals("Afternoon", shifts.get(0).getShiftType());
//    }
//
//    // Test for reading shifts by work area
//    @Test
//    public void testGetShiftsByWorkArea() {
//        // Create shifts for testing
//        CreateShiftRequest createShiftRequest1 = new CreateShiftRequest();
//        createShiftRequest1.setStartTime("2024-07-08 09");
//        createShiftRequest1.setEndTime("2024-07-08 17");
//        createShiftRequest1.setRegister(1);
//        createShiftRequest1.setShiftType("Morning");
//        createShiftRequest1.setStatus("Active");
//        createShiftRequest1.setWorkArea("Front Desk");
//        shiftService.createShift(createShiftRequest1);
//
//        CreateShiftRequest createShiftRequest2 = new CreateShiftRequest();
//        createShiftRequest2.setStartTime("2024-07-08 13");
//        createShiftRequest2.setEndTime("2024-07-08 21");
//        createShiftRequest2.setRegister(2);
//        createShiftRequest2.setShiftType("Afternoon");
//        createShiftRequest2.setStatus("Active");
//        createShiftRequest2.setWorkArea("Back Office");
//        shiftService.createShift(createShiftRequest2);
//
//        // Test fetching shifts by work area "Front Desk"
//        List<ShiftRequest> shifts = shiftService.getShiftsByWorkArea("Front Desk");
//
//        Assertions.assertFalse(shifts.isEmpty());
//        Assertions.assertEquals(1, shifts.size()); // Assuming only one shift is in "Front Desk"
//        Assertions.assertEquals("Morning", shifts.get(0).getShiftType());
//    }
//
//    // Test for reading all shifts
//    @Test
//    public void testReadAllShifts() {
//        // Create shifts for testing
//        CreateShiftRequest createShiftRequest1 = new CreateShiftRequest();
//        createShiftRequest1.setStartTime("2024-07-08 09");
//        createShiftRequest1.setEndTime("2024-07-08 17");
//        createShiftRequest1.setRegister(1);
//        createShiftRequest1.setShiftType("Morning");
//        createShiftRequest1.setStatus("Active");
//        createShiftRequest1.setWorkArea("Front Desk");
//        shiftService.createShift(createShiftRequest1);
//
//        CreateShiftRequest createShiftRequest2 = new CreateShiftRequest();
//        createShiftRequest2.setStartTime("2024-07-08 13");
//        createShiftRequest2.setEndTime("2024-07-08 21");
//        createShiftRequest2.setRegister(2);
//        createShiftRequest2.setShiftType("Afternoon");
//        createShiftRequest2.setStatus("Active");
//        createShiftRequest2.setWorkArea("Back Office");
//        shiftService.createShift(createShiftRequest2);
//
//        // Test fetching all shifts
//        List<ShiftRequest> shifts = shiftService.readAllShifts();
//
//        Assertions.assertFalse(shifts.isEmpty());
//        Assertions.assertEquals(2, shifts.size()); // Assuming two shifts exist in the database
//        // Add more specific assertions if needed
//    }
//
//    // Test for reading a shift by ID
//    @Test
//    public void testGetShiftById() {
//        CreateShiftRequest createShiftRequest = new CreateShiftRequest();
//        createShiftRequest.setStartTime("2024-07-08 09");
//        createShiftRequest.setEndTime("2024-07-08 17");
//        createShiftRequest.setRegister(1);
//        createShiftRequest.setShiftType("Morning");
//        createShiftRequest.setStatus("Active");
//        createShiftRequest.setWorkArea("Front Desk");
//
//        ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
//        Integer shiftId = createdShift.getShiftID();
//
//        ShiftRequest fetchedShift = shiftService.getShiftById(shiftId);
//
//        Assertions.assertNotNull(fetchedShift);
//        Assertions.assertEquals(shiftId, fetchedShift.getShiftID());
//        Assertions.assertEquals("Morning", fetchedShift.getShiftType());
//        Assertions.assertEquals("Front Desk", fetchedShift.getWorkArea());
//    }
//
//    // Test for updating shift details
//    @Test
//    public void testUpdateShiftDetails() {
//        CreateShiftRequest createShiftRequest = new CreateShiftRequest();
//        createShiftRequest.setStartTime("2024-07-08 09");
//        createShiftRequest.setEndTime("2024-07-08 17");
//        createShiftRequest.setRegister(1);
//        createShiftRequest.setShiftType("Morning");
//        createShiftRequest.setStatus("Active");
//        createShiftRequest.setWorkArea("Front Desk");
//
//        ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
//        Integer shiftId = createdShift.getShiftID();
//
//        // Modify some properties of the shift
//        createdShift.setStatus("Inactive");
//        createdShift.setEndTime("2024-07-08 18");
//
//        Shift updatedShift = shiftService.updateShiftDetails(createdShift);
//
//        Assertions.assertEquals("Inactive", updatedShift.getStatus());
//        Assertions.assertEquals("2024-07-08 18", updatedShift.getEndTime());
//        Assertions.assertEquals("Morning", updatedShift.getShiftType());
//        Assertions.assertEquals("Front Desk", updatedShift.getWorkArea());
//    }
//
//    // Test for deleting a shift by ID
//    @Test
//    public void testDeleteShiftById() {
//        CreateShiftRequest createShiftRequest = new CreateShiftRequest();
//        createShiftRequest.setStartTime("2024-07-08 09");
//        createShiftRequest.setEndTime("2024-07-08 17");
//        createShiftRequest.setRegister(1);
//        createShiftRequest.setShiftType("Morning");
//        createShiftRequest.setStatus("Active");
//        createShiftRequest.setWorkArea("Front Desk");
//
//        ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
//        int shiftId = createdShift.getShiftID();
//
//        shiftService.deleteShiftById((long) shiftId);
//
//        Assertions.assertThrows(RuntimeException.class, () -> shiftService.getShiftById(shiftId));
//    }
//
//    // Test for deleting shifts with criteria (no staff and older than 2 months)
//    @Test
//    public void testDeleteShiftsWithCriteria() {
//        // Create a shift with no staff and older than 2 months
//        CreateShiftRequest createShiftRequest = new CreateShiftRequest();
//        createShiftRequest.setStartTime(LocalDateTime.now().minusMonths(3).toString());
//        createShiftRequest.setEndTime(LocalDateTime.now().minusMonths(3).plusHours(8).toString());
//        createShiftRequest.setRegister(1);
//        createShiftRequest.setShiftType("Morning");
//        createShiftRequest.setStatus("Active");
//        createShiftRequest.setWorkArea("Front Desk");
//
//        shiftService.createShift(createShiftRequest);
//
//        // Delete shifts that meet the criteria
//        shiftService.deleteShiftsWithCriteria();
//
//        // Assertions can be added to verify the deletion, if needed
//    }
//}
