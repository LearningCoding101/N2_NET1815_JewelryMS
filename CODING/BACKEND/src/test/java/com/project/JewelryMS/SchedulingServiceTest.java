package com.project.JewelryMS;


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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;

    @InjectMocks
    private ShiftService shiftService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateShift() {
        CreateShiftRequest createShiftRequest = new CreateShiftRequest();
        createShiftRequest.setShiftType("Morning");
        createShiftRequest.setStartTime("2023-01-01 08");
        createShiftRequest.setEndTime("2023-01-01 12");
        createShiftRequest.setStatus("Active");
        createShiftRequest.setWorkArea("Sales");
        createShiftRequest.setRegister(0);

        Shift shift = new Shift();
        shift.setShiftID(1);
        shift.setShiftType(createShiftRequest.getShiftType());
        shift.setStartTime(LocalDateTime.of(2023, 1, 1, 8, 0));
        shift.setEndTime(LocalDateTime.of(2023, 1, 1, 12, 0));
        shift.setStatus(createShiftRequest.getStatus());
        shift.setWorkArea(createShiftRequest.getWorkArea());
        shift.setRegister(createShiftRequest.getRegister());

        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        ShiftRequest result = shiftService.createShift(createShiftRequest);

        assertNotNull(result);
        assertEquals(shift.getShiftID(), result.getShiftID());
        assertEquals("2023-01-01 12", result.getEndTime());
        assertEquals("2023-01-01 08 (Sunday)", result.getStartTime());
        assertEquals(shift.getStatus(), result.getStatus());
        assertEquals(shift.getWorkArea(), result.getWorkArea());
        assertEquals(shift.getRegister(), result.getRegister());

        verify(shiftRepository, times(1)).save(any(Shift.class));
    }

    @Test
    public void testGetShiftsByStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 8, 0);
        Shift shift = new Shift();
        shift.setShiftID(1);
        shift.setStartTime(startTime);

        when(shiftRepository.findByStartTime(startTime)).thenReturn(Arrays.asList(shift));

        List<ShiftRequest> result = shiftService.getShiftsByStartTime("2023-01-01 08");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getShiftID());

        verify(shiftRepository, times(1)).findByStartTime(startTime);
    }

    @Test
    public void testGetShiftsByEndTime() {
        LocalDateTime endTime = LocalDateTime.of(2023, 1, 1, 12, 0);
        Shift shift = new Shift();
        shift.setShiftID(1);
        shift.setEndTime(endTime);

        when(shiftRepository.findByEndTime(endTime)).thenReturn(Arrays.asList(shift));

        List<ShiftRequest> result = shiftService.getShiftsByEndTime("2023-01-01 12");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getShiftID());

        verify(shiftRepository, times(1)).findByEndTime(endTime);
    }

    @Test
    public void testGetShiftsByShiftType() {
        String shiftType = "Morning";
        Shift shift = new Shift();
        shift.setShiftID(1);
        shift.setShiftType(shiftType);

        when(shiftRepository.findByShiftType(shiftType)).thenReturn(Arrays.asList(shift));

        List<ShiftRequest> result = shiftService.getShiftsByShiftType(shiftType);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getShiftID());

        verify(shiftRepository, times(1)).findByShiftType(shiftType);
    }

    @Test
    public void testGetShiftsByStatus() {
        String status = "Active";
        Shift shift = new Shift();
        shift.setShiftID(1);
        shift.setStatus(status);

        when(shiftRepository.findByStatus(status)).thenReturn(Arrays.asList(shift));

        List<ShiftRequest> result = shiftService.getShiftsByStatus(status);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getShiftID());

        verify(shiftRepository, times(1)).findByStatus(status);
    }

    @Test
    public void testGetShiftsByWorkArea() {
        String workArea = "Sales";
        Shift shift = new Shift();
        shift.setShiftID(1);
        shift.setWorkArea(workArea);

        when(shiftRepository.findByWorkArea(workArea)).thenReturn(Arrays.asList(shift));

        List<ShiftRequest> result = shiftService.getShiftsByWorkArea(workArea);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getShiftID());

        verify(shiftRepository, times(1)).findByWorkArea(workArea);
    }

    @Test
    public void testGetShiftById() {
        Long shiftID = 1L;
        Shift shift = new Shift();
        shift.setShiftID(Math.toIntExact(shiftID));

        when(shiftRepository.findById(shiftID)).thenReturn(Optional.of(shift));

        ShiftRequest result = shiftService.getShiftById(Math.toIntExact(shiftID));

        assertNotNull(result);
        assertEquals(shiftID, result.getShiftID());

        verify(shiftRepository, times(1)).findById(shiftID);
    }

    @Test
    public void testGetShiftByIdNotFound() {
        Long shiftID = 1L;

        when(shiftRepository.findById(shiftID)).thenReturn(Optional.empty());

        ShiftRequest result = shiftService.getShiftById(Math.toIntExact(shiftID));

        assertNull(result);

        verify(shiftRepository, times(1)).findById(shiftID);
    }

    @Test
    public void testUpdateShift() {
        Long shiftID = 1L;
        CreateShiftRequest updateShiftRequest = new CreateShiftRequest();
        updateShiftRequest.setShiftType("Evening");
        updateShiftRequest.setStartTime("2023-01-01 16");
        updateShiftRequest.setEndTime("2023-01-01 20");
        updateShiftRequest.setStatus("Active");
        updateShiftRequest.setWorkArea("Inventory");
        updateShiftRequest.setRegister(1);

        Shift shift = new Shift();
        shift.setShiftID(Math.toIntExact(shiftID));
        shift.setShiftType("Morning");
        shift.setStartTime(LocalDateTime.of(2023, 1, 1, 8, 0));
        shift.setEndTime(LocalDateTime.of(2023, 1, 1, 12, 0));
        shift.setStatus("Inactive");
        shift.setWorkArea("Sales");
        shift.setRegister(0);

        when(shiftRepository.findById(shiftID)).thenReturn(Optional.of(shift));
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        ShiftRequest result = shiftService.updateShift( updateShiftRequest);

        assertNotNull(result);
        assertEquals(shiftID, result.getShiftID());
        assertEquals("Evening", result.getShiftType());
        assertEquals("2023-01-01 16 (Sunday)", result.getStartTime());
        assertEquals("2023-01-01 20", result.getEndTime());
        assertEquals("Active", result.getStatus());
        assertEquals("Inventory", result.getWorkArea());
        assertEquals(1, result.getRegister());

        verify(shiftRepository, times(1)).findById(shiftID);
        verify(shiftRepository, times(1)).save(any(Shift.class));
    }

    @Test
    public void testDeleteShift() {
        Long shiftID = 1L;
        Shift shift = new Shift();
        shift.setShiftID(Math.toIntExact(shiftID));

        when(shiftRepository.findById(shiftID)).thenReturn(Optional.of(shift));

        shiftService.deleteShiftById(shiftID);

        verify(shiftRepository, times(1)).delete(shift);
    }

    @Test
    public void testDeleteShiftNotFound() {
        Long shiftID = 1L;

        when(shiftRepository.findById(shiftID)).thenReturn(Optional.empty());

        shiftService.deleteShiftById(shiftID);

        verify(shiftRepository, never()).delete(any(Shift.class));
    }
}