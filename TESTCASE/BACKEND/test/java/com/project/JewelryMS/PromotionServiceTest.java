package com.project.JewelryMS;

import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.repository.PromotionRepository;
import com.project.JewelryMS.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {

    @InjectMocks
    private PromotionService promotionService;

    @Mock
    private PromotionRepository promotionRepository;

    private Promotion promotion;

    @BeforeEach
    void setUp() {
        promotion = new Promotion();
        promotion.setPK_promotionID(1L);
        promotion.setCode("SUMMER25");
        promotion.setDescription("Summer Sale 25% off");
        promotion.setStartDate(LocalDateTime.of(2024, 6, 1, 0, 0));
        promotion.setEndDate(LocalDateTime.of(2024, 8, 31, 23, 59, 59));
        promotion.setStatus(true);
        promotion.setDiscount(25);
    }

    @Test
    void testCreatePromotion() {
        CreatePromotionRequest request = new CreatePromotionRequest();
        request.setCode("SUMMER25");
        request.setDescription("Summer Sale 25% off");
        request.setStartDate("2024-06-01 00");
        request.setEndDate("2024-08-31 23");
        request.setDiscount(25);

        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        Promotion createdPromotion = promotionService.createPromotion(request);

        assertNotNull(createdPromotion);
        assertEquals("SUMMER25", createdPromotion.getCode());
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }

    @Test
    void testGetPromotionById() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        PromotionResponse response = promotionService.getPromotionById(1L);

        assertNotNull(response);
        assertEquals("SUMMER25", response.getCode());
        verify(promotionRepository, times(1)).findById(1L);
    }

    @Test
    void testReadAllPromotion() {
        List<Promotion> promotions = Arrays.asList(promotion);
        when(promotionRepository.findAll()).thenReturn(promotions);

        List<PromotionResponse> responses = promotionService.readAllPromotion();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("SUMMER25", responses.get(0).getCode());
        verify(promotionRepository, times(1)).findAll();
    }

    @Test
    void testReadAllActivePromotion() {
        List<Promotion> promotions = Arrays.asList(promotion);
        when(promotionRepository.findByStatus(true)).thenReturn(promotions);

        List<PromotionResponse> responses = promotionService.readAllActivePromotion();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("SUMMER25", responses.get(0).getCode());
        verify(promotionRepository, times(1)).findByStatus(true);
    }

    @Test
    void testDeletePromotionById() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
        promotionService.deletePromotionById(1L);
        verify(promotionRepository, times(1)).save(promotion);
        assertFalse(promotion.isStatus());
    }
}