package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.PromotionController;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PromotionControllerTest {

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private PromotionController promotionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductSellIdsByPromotionId() {
        List<PromotionResponse> promotions = Arrays.asList(new PromotionResponse(), new PromotionResponse());
        when(promotionService.ReadAllPromotionWithProductID()).thenReturn(promotions);

        List<PromotionResponse> response = promotionController.getProductSellIdsByPromotionId();

        assertEquals(promotions, response);
    }

    @Test
    void createPromotion() {
        CreatePromotionRequest request = new CreatePromotionRequest();
        Promotion promotion = new Promotion();
        when(promotionService.createPromotion(request)).thenReturn(promotion);

        ResponseEntity<Promotion> response = promotionController.createPromotion(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(promotion, response.getBody());
    }

    @Test
    void readAllPromotions() {
        List<PromotionResponse> promotions = Arrays.asList(new PromotionResponse(), new PromotionResponse());
        when(promotionService.readAllPromotion()).thenReturn(promotions);

        ResponseEntity<List<PromotionResponse>> response = promotionController.readAllPromotions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(promotions, response.getBody());
    }

    @Test
    void readPromotionById() {
        Long id = 1L;
        PromotionResponse promotion = new PromotionResponse();
        when(promotionService.getPromotionById(id)).thenReturn(promotion);

        ResponseEntity<PromotionResponse> response = promotionController.readPromotionById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(promotion, response.getBody());
    }

    @Test
    void deletePromotion() {
        long id = 1L;

        ResponseEntity<String> response = promotionController.deletePromotion(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Promotion details marked as inactive successfully", response.getBody());
        verify(promotionService).deletePromotionById(id);
    }

    @Test
    void updatePromotionDetails() {
        long id = 1L;
        PromotionRequest request = new PromotionRequest();
        PromotionResponse expectedResponse = new PromotionResponse();

        expectedResponse.setPromotionID(id);
        expectedResponse.setCode("NEWCODE");
        expectedResponse.setDescription("New description");
        expectedResponse.setStartDate("2024-07-16");
        expectedResponse.setEndDate("2024-08-16");
        expectedResponse.setDiscount(20);
        expectedResponse.setStatus(true);

        List<PromotionResponse> expectedResponseList = Arrays.asList(expectedResponse);

        when(promotionService.updatePromotionDetails(request)).thenReturn(expectedResponseList);

        ResponseEntity<List<PromotionResponse>> response = promotionController.updatePromotionDetails(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseList, response.getBody());
        verify(promotionService).updatePromotionDetails(request);
    }
}