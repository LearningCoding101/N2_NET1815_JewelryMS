
package com.project.JewelryMS.UnitTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.service.ShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShiftServiceUnitTest {

    @Mock
    private ShiftRepository shiftRepository;

    @InjectMocks
    private ShiftService shiftService;

    private Shift shift;
    private CreateShiftRequest createShiftRequest;
    private DateTimeFormatter formatter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

        shift = new Shift();
        shift.setShiftID(1);
        shift.setStartTime(LocalDateTime.parse("2024-07-01 08", formatter));
        shift.setEndTime(LocalDateTime.parse("2024-07-01 16", formatter));
        shift.setRegister(1);
        shift.setShiftType("morning");
        shift.setStatus("active");
        shift.setWorkArea("cashier");

        createShiftRequest = new CreateShiftRequest();
        createShiftRequest.setStartTime("2024-07-01 08");
        createShiftRequest.setEndTime("2024-07-01 16");
        createShiftRequest.setRegister(1);
        createShiftRequest.setShiftType("morning");
        createShiftRequest.setStatus("active");
        createShiftRequest.setWorkArea("cashier");
    }

    @Test
    public void testCreateShift() {
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        ShiftRequest createdShift = shiftService.createShift(createShiftRequest);

        assertNotNull(createdShift);
        assertEquals(shift.getShiftID(), createdShift.getShiftID());
        verify(shiftRepository, times(1)).save(any(Shift.class));
    }

    @Test
    public void testGetShiftById() {
        when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));

        ShiftRequest shiftRequest = shiftService.getShiftById(1);

        assertNotNull(shiftRequest);
        assertEquals(shift.getShiftID(), shiftRequest.getShiftID());
        verify(shiftRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateShiftDetails() {
        when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setShiftID(1);
        shiftRequest.setStartTime("2024-07-01 08");
        shiftRequest.setEndTime("2024-07-01 16");
        shiftRequest.setRegister(1);
        shiftRequest.setShiftType("morning");
        shiftRequest.setStatus("active");
        shiftRequest.setWorkArea("cashier");

        Shift updatedShift = shiftService.updateShiftDetails(shiftRequest);

        assertNotNull(updatedShift);
        assertEquals(shift.getShiftID(), updatedShift.getShiftID());
        verify(shiftRepository, times(1)).findById(1L);
        verify(shiftRepository, times(1)).save(any(Shift.class));
    }

    @Test
    public void testDeleteShiftById() {
        when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));

        shiftService.deleteShiftById(1L);

        assertEquals("Unapplicable", shift.getStatus());
        verify(shiftRepository, times(1)).findById(1L);
        verify(shiftRepository, times(1)).save(shift);
    }

    @Test
    public void testGetShiftsByStartTime() {
        List<Shift> shifts = new ArrayList<>();
        shifts.add(shift);
        when(shiftRepository.findByStartTime(any(LocalDateTime.class))).thenReturn(shifts);

        List<ShiftRequest> shiftRequests = shiftService.getShiftsByStartTime("2024-07-01 08");

        assertNotNull(shiftRequests);
        assertEquals(1, shiftRequests.size());
        assertEquals(shift.getShiftID(), shiftRequests.get(0).getShiftID());
        verify(shiftRepository, times(1)).findByStartTime(any(LocalDateTime.class));
    }

    @Test
    public void testGetShiftsByEndTime() {
        List<Shift> shifts = new ArrayList<>();
        shifts.add(shift);
        when(shiftRepository.findByEndTime(any(LocalDateTime.class))).thenReturn(shifts);

        List<ShiftRequest> shiftRequests = shiftService.getShiftsByEndTime("2024-07-01 16");

        assertNotNull(shiftRequests);
        assertEquals(1, shiftRequests.size());
        assertEquals(shift.getShiftID(), shiftRequests.get(0).getShiftID());
        verify(shiftRepository, times(1)).findByEndTime(any(LocalDateTime.class));
    }

    @Test
    public void testGetShiftsByShiftType() {
        List<Shift> shifts = new ArrayList<>();
        shifts.add(shift);
        when(shiftRepository.findByShiftType(any(String.class))).thenReturn(shifts);

        List<ShiftRequest> shiftRequests = shiftService.getShiftsByShiftType("morning");

        assertNotNull(shiftRequests);
        assertEquals(1, shiftRequests.size());
        assertEquals(shift.getShiftID(), shiftRequests.get(0).getShiftID());
        verify(shiftRepository, times(1)).findByShiftType(any(String.class));
    }

    @Test
    public void testGetShiftsByStatus() {
        List<Shift> shifts = new ArrayList<>();
        shifts.add(shift);
        when(shiftRepository.findByStatus(any(String.class))).thenReturn(shifts);

        List<ShiftRequest> shiftRequests = shiftService.getShiftsByStatus("active");

        assertNotNull(shiftRequests);
        assertEquals(1, shiftRequests.size());
        assertEquals(shift.getShiftID(), shiftRequests.get(0).getShiftID());
        verify(shiftRepository, times(1)).findByStatus(any(String.class));
    }

    @Test
    public void testGetShiftsByRegister() {
        List<Shift> shifts = new ArrayList<>();
        shifts.add(shift);
        when(shiftRepository.findByRegister(any(Integer.class))).thenReturn(shifts);

        List<ShiftRequest> shiftRequests = shiftService.getShiftsByRegister(1);

        assertNotNull(shiftRequests);
        assertEquals(1, shiftRequests.size());
        assertEquals(shift.getShiftID(), shiftRequests.get(0).getShiftID());
        verify(shiftRepository, times(1)).findByRegister(any(Integer.class));
    }

    @Test
    public void testGetShiftsByWorkArea() {
        List<Shift> shifts = new ArrayList<>();
        shifts.add(shift);
        when(shiftRepository.findByWorkArea(any(String.class))).thenReturn(shifts);

        List<ShiftRequest> shiftRequests = shiftService.getShiftsByWorkArea("cashier");

        assertNotNull(shiftRequests);
        assertEquals(1, shiftRequests.size());
        assertEquals(shift.getShiftID(), shiftRequests.get(0).getShiftID());
        verify(shiftRepository, times(1)).findByWorkArea(any(String.class));
    }

    @Test
    public void testReadAllShifts() {
        List<Shift> shifts = new ArrayList<>();
        shifts.add(shift);
        when(shiftRepository.findAll()).thenReturn(shifts);

        List<ShiftRequest> shiftRequests = shiftService.readAllShifts();

        assertNotNull(shiftRequests);
        assertEquals(1, shiftRequests.size());
        assertEquals(shift.getShiftID(), shiftRequests.get(0).getShiftID());
        verify(shiftRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteShiftsWithCriteria() {
        // Create a shift object
        Shift shiftWithoutStaff = new Shift();
        shiftWithoutStaff.setShiftID(1);

        Shift shiftOlderThanTwoMonths = new Shift();
        shiftOlderThanTwoMonths.setShiftID(2);

        // Mock the repository responses
        when(shiftRepository.findShiftsWithoutStaff()).thenReturn(Collections.singletonList(shiftWithoutStaff));
        when(shiftRepository.findShiftsOlderThan(any(LocalDate.class))).thenReturn(Collections.singletonList(shiftOlderThanTwoMonths));

        // Call the method to test
        shiftService.deleteShiftsWithCriteria();

        // Verify interactions with the repository
        verify(shiftRepository, times(1)).findShiftsWithoutStaff();
        verify(shiftRepository, times(1)).findShiftsOlderThan(any(LocalDate.class));

        // Ensure delete is called the correct number of times
        verify(shiftRepository, times(2)).delete(any(Shift.class));

        // Verify that delete is called with the correct shifts
        verify(shiftRepository).delete(shiftWithoutStaff);
        verify(shiftRepository).delete(shiftOlderThanTwoMonths);
    }


}
