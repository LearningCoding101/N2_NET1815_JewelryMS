package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.ShiftController;
import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.service.ShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ShiftControllerTest {

    @Mock
    private ShiftService shiftService;

    @InjectMocks
    private ShiftController shiftController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShift() {
        CreateShiftRequest createShiftRequest = new CreateShiftRequest();
        ShiftRequest createdShift = new ShiftRequest();
        when(shiftService.createShift(createShiftRequest)).thenReturn(createdShift);

        ResponseEntity<ShiftRequest> response = shiftController.createShift(createShiftRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdShift, response.getBody());
    }

    @Test
    void getAllShifts() {
        List<ShiftRequest> allShifts = Arrays.asList(new ShiftRequest(), new ShiftRequest());
        when(shiftService.readAllShifts()).thenReturn(allShifts);

        ResponseEntity<List<ShiftRequest>> response = shiftController.getAllShifts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(allShifts, response.getBody());
    }

    @Test
    void getShiftById() {
        int id = 1;
        ShiftRequest shift = new ShiftRequest();
        when(shiftService.getShiftById(id)).thenReturn(shift);

        ResponseEntity<ShiftRequest> response = shiftController.getShiftById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(shift, response.getBody());
    }

    @Test
    void deleteShiftById() {
        int id = 1;

        ResponseEntity<String> response = shiftController.deleteShiftById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Shift details marked as invalid successfully", response.getBody());
        verify(shiftService).deleteShiftById((long)id);
    }

    @Test
    void updateShift() {
        int id = 1;
        ShiftRequest shiftRequest = new ShiftRequest();

        ResponseEntity<String> response = shiftController.updateShift(id, shiftRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Shift Details updated successfully", response.getBody());
        verify(shiftService).updateShiftDetails(shiftRequest);
    }

    @Test
    void deleteShiftsWithCriteria() {
        ResponseEntity<String> response = shiftController.deleteShiftsWithCriteria();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Shifts deleted successfully based on criteria.", response.getBody());
        verify(shiftService).deleteShiftsWithCriteria();
    }
}