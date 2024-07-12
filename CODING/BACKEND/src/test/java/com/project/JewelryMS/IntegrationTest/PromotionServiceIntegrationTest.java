package com.project.JewelryMS.IntegrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.repository.ProductSellPromotionRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import com.project.JewelryMS.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PromotionServiceIntegrationTest {

    @Autowired
    private PromotionService promotionService;

    @MockBean
    private PromotionRepository promotionRepository;

    @MockBean
    private ProductSellPromotionRepository productSellPromotionRepository;

    @MockBean
    private ProductSellRepository productSellRepository;

    private Promotion promotion;
    private CreatePromotionRequest createPromotionRequest;

    @BeforeEach
    public void setUp() {
        promotion = new Promotion();
        promotion.setPK_promotionID(1L);
        promotion.setCode("PROMO1");
        promotion.setDescription("Test promotion");

        Date startDate = java.sql.Date.valueOf("2024-07-01");
        Date endDate = java.sql.Date.valueOf("2024-07-10");

        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setDiscount(10);
        promotion.setStatus(true);

        createPromotionRequest = new CreatePromotionRequest();
        createPromotionRequest.setCode("PROMO1");
        createPromotionRequest.setDescription("Test promotion");
        createPromotionRequest.setStartDate(LocalDate.parse("2024-07-01"));
        createPromotionRequest.setEndDate(LocalDate.parse("2024-07-10"));
        createPromotionRequest.setDiscount(10);
        createPromotionRequest.setProductSell_IDs(List.of(1L, 2L));
    }

    @Test
    public void testCreatePromotion() {
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        Promotion createdPromotion = promotionService.createPromotion(createPromotionRequest);

        assertNotNull(createdPromotion);
        assertEquals("PROMO1", createdPromotion.getCode());
        assertEquals("Test promotion", createdPromotion.getDescription());
        assertEquals(10, createdPromotion.getDiscount());

        verify(promotionRepository, times(1)).save(any(Promotion.class));
        verify(productSellRepository, times(1)).findAllById(createPromotionRequest.getProductSell_IDs());
        verify(productSellPromotionRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testReadAllPromotionWithProductID() {
        when(promotionRepository.findAllPromotion()).thenReturn(List.of(promotion));
        when(promotionRepository.findProductSellIdsByPromotionId(1L)).thenReturn(List.of(1L, 2L));

        List<PromotionResponse> promotions = promotionService.ReadAllPromotionWithProductID();

        assertNotNull(promotions);
        assertFalse(promotions.isEmpty());
        assertEquals(1, promotions.size());
        assertEquals("PROMO1", promotions.get(0).getCode());

        verify(promotionRepository, times(1)).findAllPromotion();
        verify(promotionRepository, times(1)).findProductSellIdsByPromotionId(1L);
    }

    @Test
    public void testGetPromotionById() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
        when(promotionRepository.findProductSellIdsByPromotionId(1L)).thenReturn(List.of(1L, 2L));

        PromotionResponse promotionResponse = promotionService.getPromotionById(1L);

        assertNotNull(promotionResponse);
        assertEquals("PROMO1", promotionResponse.getCode());

        verify(promotionRepository, times(1)).findById(1L);
        verify(promotionRepository, times(1)).findProductSellIdsByPromotionId(1L);
    }
/*

    @Test
    public void testUpdatePromotion() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        PromotionRequest updateRequest = new PromotionRequest();
        updateRequest.setPK_promotionID(1L);
        updateRequest.setCode("NEWCODE");
        updateRequest.setDescription("Updated Description");
        updateRequest.setDiscount(15);
        updateRequest.setStartDate(LocalDate.parse("2024-09-09"));
        updateRequest.setEndDate(LocalDate.parse("2024-09-29"));

        promotionService.updatePromotionDetails(updateRequest);

        verify(promotionRepository, times(1)).save(any(Promotion.class));
        assertEquals("NEWCODE", promotion.getCode());
        assertEquals("Updated Description", promotion.getDescription());
        assertEquals(15, promotion.getDiscount());
        assertEquals(java.sql.Date.valueOf("2024-09-09"), promotion.getStartDate());
        assertEquals(java.sql.Date.valueOf("2024-09-29"), promotion.getEndDate());
    }
*/

    @Test
    public void testDeletePromotionById() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        promotionService.deletePromotionById(1L);

        verify(promotionRepository, times(1)).save(any(Promotion.class));
        assertFalse(promotion.isStatus());
    }

    @Test
    public void testReadAllActivePromotions() {
        when(promotionRepository.findByStatus(true)).thenReturn(List.of(promotion));

        List<PromotionResponse> promotions = promotionService.readAllActivePromotion();

        assertNotNull(promotions);
        assertFalse(promotions.isEmpty());
        assertEquals(1, promotions.size());
        assertEquals("PROMO1", promotions.get(0).getCode());

        verify(promotionRepository, times(1)).findByStatus(true);
    }

    @ParameterizedTest
    @MethodSource("provideDatesForPromotionsByDate")
    public void testGetPromotionsByDate(Date targetDate, int expectedSize) {
        Promotion promotion1 = new Promotion();
        promotion1.setStartDate(new Date(123456789L));
        promotion1.setEndDate(new Date(123456789L + 100000L));
        Promotion promotion2 = new Promotion();
        promotion2.setStartDate(new Date(223456789L));
        promotion2.setEndDate(new Date(223456789L + 100000L));

        when(promotionRepository.findAll()).thenReturn(Arrays.asList(promotion1, promotion2));

        List<PromotionResponse> responses = promotionService.getPromotionsByDate(targetDate);

        assertEquals(expectedSize, responses.size());
        verify(promotionRepository, times(1)).findAll();
    }

    private static Stream<Arguments> provideDatesForPromotionsByDate() {
        return Stream.of(
                Arguments.of(new Date(123456789L + 50000L), 1),
                Arguments.of(new Date(223456789L + 50000L), 1),
                Arguments.of(new Date(323456789L), 0)
        );
    }

    @Test
    public void testReadAllActivePromotion() {
        when(promotionRepository.findByStatus(true)).thenReturn(List.of(promotion));

        List<PromotionResponse> activePromotions = promotionService.readAllActivePromotion();

        assertNotNull(activePromotions);
        assertFalse(activePromotions.isEmpty());
        assertEquals(1, activePromotions.size());
        assertEquals("PROMO1", activePromotions.get(0).getCode());

        verify(promotionRepository, times(1)).findByStatus(true);
    }

    @Test
    public void testGetPromotionsByDate() {
        when(promotionRepository.findAll()).thenReturn(List.of(promotion));

        Date targetDate = java.sql.Date.valueOf("2024-07-05");
        List<PromotionResponse> promotions = promotionService.getPromotionsByDate(targetDate);

        assertNotNull(promotions);
        assertFalse(promotions.isEmpty());
        assertEquals(1, promotions.size());
        assertEquals("PROMO1", promotions.get(0).getCode());

        verify(promotionRepository, times(1)).findAll();
    }
}
