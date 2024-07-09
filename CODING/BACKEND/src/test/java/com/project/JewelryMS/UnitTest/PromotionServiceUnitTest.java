package com.project.JewelryMS.UnitTest;

import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.repository.PromotionRepository;
import com.project.JewelryMS.repository.ProductSellPromotionRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PromotionServiceUnitTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ProductSellPromotionRepository productSellPromotionRepository;

    @Mock
    private ProductSellRepository productSellRepository;

    @InjectMocks
    private PromotionService promotionService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Promotion promotion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Initialize the promotion object
        promotion = new Promotion();
        promotion.setPK_promotionID(1L);
        promotion.setCode("PROMO1");
        promotion.setDescription("Test promotion");
        promotion.setStartDate(java.sql.Date.valueOf("2024-07-01"));
        promotion.setEndDate(java.sql.Date.valueOf("2024-07-10"));
        promotion.setDiscount(10);
        promotion.setStatus(true);
    }


    @Test
    public void testCreatePromotion() {
        CreatePromotionRequest request = new CreatePromotionRequest();
        request.setCode("CODE123");
        request.setDescription("Test Description");
        request.setDiscount(20);
        request.setStartDate(LocalDate.parse("2024-07-01", formatter));
        request.setEndDate(LocalDate.parse("2024-07-10", formatter));
        request.setProductSell_IDs(Arrays.asList(1L, 2L, 3L));

        // Additional validation to ensure dates are not null
        assertNotNull(request.getStartDate());
        assertNotNull(request.getEndDate());

        Promotion promotion = new Promotion();
        promotion.setPK_promotionID(1L);

        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);
        when(productSellRepository.findAllById(any())).thenReturn(Arrays.asList(new ProductSell(), new ProductSell(), new ProductSell()));

        Promotion createdPromotion = promotionService.createPromotion(request);

        assertEquals(1L, createdPromotion.getPK_promotionID());
        verify(promotionRepository, times(1)).save(any(Promotion.class));
        verify(productSellPromotionRepository, times(1)).saveAll(any());
    }


    @Test
    public void testReadAllPromotion() {
        // Arrange
        List<Promotion> promotions = Arrays.asList(
                createPromotion(1L, "CODE123", "Test Description", 20, true),
                createPromotion(2L, "CODE456", "Another Description", 25, true)
        );
        when(promotionRepository.findAll()).thenReturn(promotions);

        // Act
        List<PromotionResponse> responses = promotionService.readAllPromotion();

        // Assert
        assertEquals(2, responses.size());
        verify(promotionRepository, times(1)).findAll();
    }

    // Helper method to create Promotion objects for testing
    private Promotion createPromotion(Long id, String code, String description, int discount, boolean status) {
        Promotion promotion = new Promotion();
        promotion.setPK_promotionID(id);
        promotion.setCode(code);
        promotion.setDescription(description);
        promotion.setDiscount(discount);
        promotion.setStatus(status);
        // Set any other necessary fields
        return promotion;
    }



    @ParameterizedTest
    @CsvSource({
            "1, NEW_CODE, New Description, 30, 2024-07-10, 2024-07-22",
            "2, CODE456, Another Description, 25, 2024-08-01, 2024-08-10"
    })
    public void testUpdatePromotionDetails(long id, String code, String description, int discount, String startDate, String endDate) {
        Promotion promotion = new Promotion();
        promotion.setPK_promotionID(id);
        promotion.setCode("Initial_Code"); // Initialize code to avoid null checks in tests
        promotion.setDescription("Initial Description"); // Initialize description
        promotion.setStartDate(new Date()); // Initialize startDate
        promotion.setEndDate(new Date()); // Initialize endDate

        when(promotionRepository.findById(anyLong())).thenReturn(Optional.of(promotion));

        PromotionRequest request = new PromotionRequest();
        request.setPK_promotionID(id);
        request.setCode(code);
        request.setDescription(description);
        request.setDiscount(discount);
        request.setStartDate(LocalDate.parse(startDate, formatter));
        request.setEndDate(LocalDate.parse(endDate, formatter));

        promotionService.updatePromotionDetails(request);

        verify(promotionRepository, times(1)).save(any(Promotion.class));
        assertEquals(code, promotion.getCode());
        assertEquals(description, promotion.getDescription());
        assertEquals(discount, promotion.getDiscount());
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

        List<Promotion> promotions = Arrays.asList(promotion1, promotion2);
        when(promotionRepository.findAll()).thenReturn(promotions);

        List<PromotionResponse> responses = promotionService.getPromotionsByDate(targetDate);

        assertEquals(expectedSize, responses.size());
        verify(promotionRepository, times(1)).findAll();
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideDatesForPromotionsByDate() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(new Date(123456789L + 50000L), 1),
                org.junit.jupiter.params.provider.Arguments.of(new Date(223456789L + 50000L), 1),
                org.junit.jupiter.params.provider.Arguments.of(new Date(323456789L), 0)
        );
    }

    @Test
    public void testDeletePromotionById() {
        Promotion promotion = new Promotion();
        promotion.setPK_promotionID(1L);
        when(promotionRepository.findById(anyLong())).thenReturn(Optional.of(promotion));

        promotionService.deletePromotionById(1L);

        verify(promotionRepository, times(1)).save(any(Promotion.class));
        assertEquals(false, promotion.isStatus());
    }

    @Test
    public void testReadAllActivePromotion() {
        // Arrange
        List<Promotion> promotions = Arrays.asList(
                createPromotion(1L, "CODE123", "Test Description", 20, true, new Date(), new Date()),
                createPromotion(2L, "CODE456", "Another Description", 25, true, new Date(), new Date())
        );
        when(promotionRepository.findByStatus(true)).thenReturn(promotions);

        // Act
        List<PromotionResponse> responses = promotionService.readAllActivePromotion();

        // Assert
        assertEquals(2, responses.size());
        verify(promotionRepository, times(1)).findByStatus(true);
    }

    private Promotion createPromotion(long id, String code, String description, int discount, boolean status, Date startDate, Date endDate) {
        Promotion promotion = new Promotion();
        promotion.setPK_promotionID(id);
        promotion.setCode(code);
        promotion.setDescription(description);
        promotion.setDiscount(discount);
        promotion.setStatus(status);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        return promotion;
    }


    @Test
    public void testReadPromotionById() {
        // Arrange
        Promotion promotion = new Promotion();
        promotion.setPK_promotionID(1L);
        promotion.setCode("CODE123");
        promotion.setDescription("Test Description");
        promotion.setDiscount(20);
        promotion.setStartDate(new Date()); // Ensure startDate is initialized
        promotion.setEndDate(new Date());   // Ensure endDate is initialized

        when(promotionRepository.findById(anyLong())).thenReturn(Optional.of(promotion));

        // Act
        PromotionResponse response = promotionService.getPromotionById(1L);

        // Assert
        assertNotNull(response); // Ensure response is not null
        assertEquals(1L, response.getPromotionID());
        verify(promotionRepository, times(1)).findById(anyLong());
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
