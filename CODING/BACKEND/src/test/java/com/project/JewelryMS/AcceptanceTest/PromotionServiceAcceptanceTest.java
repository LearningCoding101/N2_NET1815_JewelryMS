package com.project.JewelryMS.AcceptanceTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.repository.OrderDetailRepository;
import com.project.JewelryMS.repository.ProductSellPromotionRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import com.project.JewelryMS.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceAcceptanceTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ProductSellPromotionRepository productSellPromotionRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private ProductSellRepository productSellRepository;

    @InjectMocks
    private PromotionService promotionService;

    private Promotion promotion;
    private CreatePromotionRequest createPromotionRequest;
    private PromotionRequest promotionRequest;

    @BeforeEach
    public void setUp() {
        promotion = new Promotion();
        promotion.setPK_promotionID(1L);
        promotion.setCode("PROMO1");
        promotion.setDescription("Test Promotion");
        promotion.setStartDate(new Date());
        promotion.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // +1 day
        promotion.setDiscount(10);
        promotion.setStatus(true);

        // Initialize orderDetails
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPromotion(promotion);
        orderDetails.add(orderDetail);

        promotion.setOrderDetails(orderDetails);

        createPromotionRequest = new CreatePromotionRequest();
        createPromotionRequest.setCode("PROMO1");
        createPromotionRequest.setDescription("Test Promotion");
        createPromotionRequest.setStartDate(LocalDate.parse("2024-09-01"));
        createPromotionRequest.setEndDate(LocalDate.parse("2024-09-10"));
        createPromotionRequest.setDiscount(10);
        createPromotionRequest.setProductSell_IDs(Arrays.asList(1L, 2L));

        promotionRequest = new PromotionRequest();
        promotionRequest.setPK_promotionID(1L);
        promotionRequest.setCode("PROMO1");
        promotionRequest.setDescription("Updated Promotion");
        promotionRequest.setStartDate(LocalDate.parse("2024-09-01"));
        promotionRequest.setEndDate(LocalDate.parse("2024-09-15"));
        promotionRequest.setDiscount(20);
    }

    @Test
    public void testCreatePromotion_Success() {
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);
        when(productSellRepository.findAllById(anyList())).thenReturn(Arrays.asList(new ProductSell(), new ProductSell()));

        Promotion createdPromotion = promotionService.createPromotion(createPromotionRequest);

        assertNotNull(createdPromotion);
        assertEquals("PROMO1", createdPromotion.getCode());
        verify(promotionRepository, times(1)).save(any(Promotion.class));
        verify(productSellPromotionRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testCreatePromotion_InvalidDiscount() {
        createPromotionRequest.setDiscount(110); // Invalid discount

        Exception exception = assertThrows(RuntimeException.class, () -> {
            promotionService.createPromotion(createPromotionRequest);
        });

        String expectedMessage = "Can't Set Promotion <0 and >100";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testReadAllPromotion_Success() {
        when(promotionRepository.findAll()).thenReturn(Arrays.asList(promotion));

        List<PromotionResponse> promotionResponses = promotionService.readAllPromotion();

        assertNotNull(promotionResponses);
        assertEquals(1, promotionResponses.size());
        verify(promotionRepository, times(1)).findAll();
    }

    @Test
    public void testGetPromotionById_Success() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        PromotionResponse promotionResponse = promotionService.getPromotionById(1L);

        assertNotNull(promotionResponse);
        assertEquals("PROMO1", promotionResponse.getCode());
        verify(promotionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPromotionById_NotFound() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());

        PromotionResponse promotionResponse = promotionService.getPromotionById(1L);

        assertNull(promotionResponse);
    }

    @Test
    public void testGetPromotionByCode_Success() {
        when(promotionRepository.findByCode("PROMO1")).thenReturn(Collections.singletonList(promotion));

        List<PromotionResponse> promotionResponses = promotionService.getPromotionByCode("PROMO1");

        assertNotNull(promotionResponses);
        assertEquals(1, promotionResponses.size());
        verify(promotionRepository, times(1)).findByCode("PROMO1");
    }

    @Test
    public void testUpdatePromotionDetails_Success() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        promotionService.updatePromotionDetails(promotionRequest);

        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }

    @Test
    public void testUpdatePromotionDetails_InvalidDiscount() {
        promotionRequest.setDiscount(110); // Invalid discount
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            promotionService.updatePromotionDetails(promotionRequest);
        });

        String expectedMessage = "Discount must be between 0 and 100";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDeletePromotionById_Success() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        promotionService.deletePromotionById(1L);

        verify(promotionRepository, times(1)).findById(1L); // Check if findById was called

        if (promotion.getOrderDetails() != null) {
            verify(orderDetailRepository, times(promotion.getOrderDetails().size())).save(any(OrderDetail.class)); // Check if save was called for each order detail
        }

        verify(promotionRepository, times(1)).save(promotion); // Check if save was called for the promotion
        assertFalse(promotion.isStatus()); // Check if the status was set to false
    }

    @Test
    public void testReadAllActivePromotion_Success() {
        when(promotionRepository.findByStatus(true)).thenReturn(Arrays.asList(promotion));

        List<PromotionResponse> promotionResponses = promotionService.readAllActivePromotion();

        assertNotNull(promotionResponses);
        assertEquals(1, promotionResponses.size());
        verify(promotionRepository, times(1)).findByStatus(true);
    }

    @Test
    public void testGetPromotionsByDate_Success() {
        Date targetDate = new Date();
        when(promotionRepository.findAll()).thenReturn(Arrays.asList(promotion));

        List<PromotionResponse> promotionResponses = promotionService.getPromotionsByDate(targetDate);

        assertNotNull(promotionResponses);
        assertEquals(1, promotionResponses.size());
        verify(promotionRepository, times(1)).findAll();
    }
}

