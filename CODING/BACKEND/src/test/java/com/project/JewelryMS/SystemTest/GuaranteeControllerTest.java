package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.GuaranteeController;
import com.project.JewelryMS.model.Guarantee.*;
import com.project.JewelryMS.service.GuaranteeService;
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

class GuaranteeControllerTest {

    @Mock
    private GuaranteeService guaranteeService;

    @InjectMocks
    private GuaranteeController guaranteeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGuarantee() {
        CreateGuaranteeRequest request = new CreateGuaranteeRequest();
        GuaranteeResponse response = new GuaranteeResponse();
        when(guaranteeService.createGuarantee(request)).thenReturn(response);

        ResponseEntity<GuaranteeResponse> result = guaranteeController.createGuarantee(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void readAllGuarantees() {
        List<GuaranteeResponse> guarantees = Arrays.asList(new GuaranteeResponse(), new GuaranteeResponse());
        when(guaranteeService.readAllGuaranteeResponses()).thenReturn(guarantees);

        ResponseEntity<List<GuaranteeResponse>> result = guaranteeController.readAllGuarantees();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(guarantees, result.getBody());
    }

    @Test
    void readGuaranteeById() {
        Long id = 1L;
        GuaranteeResponse response = new GuaranteeResponse();
        when(guaranteeService.getGuaranteeResponseById(id)).thenReturn(response);

        ResponseEntity<GuaranteeResponse> result = guaranteeController.readGuaranteeById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

//    @Test
//    void deleteGuarantee() {
//        Long id = 1L;
//
//        ResponseEntity<String> result = guaranteeController.deleteGuarantee(id);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals("Guarantee policy details marked as inactive successfully", result.getBody());
//        verify(guaranteeService).deleteGuaranteePolicyById(id);
//    }

    @Test
    void updateGuaranteeDetails() {
        GuaranteeRequest request = new GuaranteeRequest();

        ResponseEntity<String> result = guaranteeController.updateGuaranteeDetails(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Guarantee policy details updated successfully", result.getBody());
        verify(guaranteeService).updateGuaranteeDetails(request);
    }
}