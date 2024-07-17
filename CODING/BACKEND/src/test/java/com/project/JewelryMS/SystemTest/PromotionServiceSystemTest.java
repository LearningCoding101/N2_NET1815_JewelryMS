//package com.project.JewelryMS.SystemTest;
//
//import com.project.JewelryMS.entity.*;
//import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
//import com.project.JewelryMS.model.Promotion.PromotionRequest;
//import com.project.JewelryMS.model.Promotion.PromotionResponse;
//import com.project.JewelryMS.repository.OrderDetailRepository;
//import com.project.JewelryMS.repository.ProductSellPromotionRepository;
//import com.project.JewelryMS.repository.ProductSellRepository;
//import com.project.JewelryMS.repository.PromotionRepository;
//import com.project.JewelryMS.service.PromotionService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class PromotionServiceSystemTest {
//
//    @InjectMocks
//    private PromotionService promotionService;
//
//    @Mock
//    private PromotionRepository promotionRepository;
//
//    @Mock
//    private ProductSellPromotionRepository productSellPromotionRepository;
//
//    @Mock
//    private OrderDetailRepository orderDetailRepository;
//
//    @Mock
//    private ProductSellRepository productSellRepository;
//
//    @BeforeEach
//    public void initMocks() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreatePromotion() throws ParseException {
//        // Mock data
//        CreatePromotionRequest request = new CreatePromotionRequest();
//        request.setCode("SUMMER20");
//        request.setDescription("Summer Sale 2024");
//        request.setStartDate(LocalDate.parse("2024-07-10"));
//        request.setEndDate(LocalDate.parse("2024-07-30"));
//        request.setDiscount(20);
//        request.setProductSell_IDs(Arrays.asList(1L, 2L, 3L));
//
//        Promotion savedPromotion = new Promotion();
//        savedPromotion.setPK_promotionID(1L);
//        savedPromotion.setCode(request.getCode());
//        savedPromotion.setDescription(request.getDescription());
//        savedPromotion.setStartDate(java.sql.Date.valueOf(request.getStartDate()));
//        savedPromotion.setEndDate(java.sql.Date.valueOf(request.getEndDate()));
//        savedPromotion.setDiscount(request.getDiscount());
//        savedPromotion.setStatus(true);
//
//        when(promotionRepository.save(any(Promotion.class))).thenReturn(savedPromotion);
//
//        // Mock ProductSells
//        ProductSell productSell1 = new ProductSell();
//        productSell1.setProductID(1L);
//        ProductSell productSell2 = new ProductSell();
//        productSell2.setProductID(2L);
//        ProductSell productSell3 = new ProductSell();
//        productSell3.setProductID(3L);
//
//        when(productSellRepository.findAllById(request.getProductSell_IDs())).thenReturn(Arrays.asList(productSell1, productSell2, productSell3));
//
//        // Test method
//        Promotion createdPromotion = promotionService.createPromotion(request);
//
//        // Verify save calls
//        verify(promotionRepository, times(1)).save(any(Promotion.class));
//        verify(productSellPromotionRepository, times(1)).saveAll(anyList());
//
//        // Assertions
//        assertEquals(savedPromotion.getPK_promotionID(), createdPromotion.getPK_promotionID());
//        assertEquals(savedPromotion.getCode(), createdPromotion.getCode());
//        assertEquals(savedPromotion.getDescription(), createdPromotion.getDescription());
//        assertEquals(savedPromotion.getDiscount(), createdPromotion.getDiscount());
//        assertEquals(savedPromotion.getStartDate(), createdPromotion.getStartDate());
//        assertEquals(savedPromotion.getEndDate(), createdPromotion.getEndDate());
//        assertTrue(createdPromotion.isStatus());
//    }
//
//    @Test
//    public void testGetPromotionById() {
//        // Mock data
//        long promotionId = 1L;
//        Promotion promotion = new Promotion();
//        promotion.setPK_promotionID(promotionId);
//        promotion.setCode("SUMMER20");
//        promotion.setDescription("Summer Sale 2024");
//        promotion.setStartDate(java.sql.Date.valueOf("2024-06-01"));
//        promotion.setEndDate(java.sql.Date.valueOf("2024-06-30"));
//        promotion.setDiscount(20);
//        promotion.setStatus(true);
//
//        when(promotionRepository.findById(promotionId)).thenReturn(Optional.of(promotion));
//
//        // Test method
//        PromotionResponse promotionResponse = promotionService.getPromotionById(promotionId);
//
//        // Assertions
//        assertNotNull(promotionResponse);
//        assertEquals(promotion.getPK_promotionID(), promotionResponse.getPromotionID());
//        assertEquals(promotion.getCode(), promotionResponse.getCode());
//        assertEquals(promotion.getDescription(), promotionResponse.getDescription());
//        assertEquals(promotion.getDiscount(), promotionResponse.getDiscount());
//        assertEquals("2024-06-01", promotionResponse.getStartDate()); // Check formatted date
//        assertEquals("2024-06-30", promotionResponse.getEndDate()); // Check formatted date
//        assertTrue(promotionResponse.isStatus());
//    }
//
//    @Test
//    public void testReadAllActivePromotion() {
//        // Mock data
//        List<Promotion> activePromotions = new ArrayList<>();
//        Promotion promotion1 = new Promotion();
//        promotion1.setPK_promotionID(1L);
//        promotion1.setCode("SUMMER20");
//        promotion1.setDescription("Summer Sale 2024");
//        promotion1.setStartDate(java.sql.Date.valueOf("2024-06-01"));
//        promotion1.setEndDate(java.sql.Date.valueOf("2024-06-30"));
//        promotion1.setDiscount(20);
//        promotion1.setStatus(true);
//        activePromotions.add(promotion1);
//
//        when(promotionRepository.findByStatus(true)).thenReturn(activePromotions);
//
//        // Test method
//        List<PromotionResponse> promotionResponses = promotionService.readAllActivePromotion();
//
//        // Assertions
//        assertNotNull(promotionResponses);
//        assertEquals(1, promotionResponses.size());
//        PromotionResponse promotionResponse = promotionResponses.get(0);
//        assertEquals(promotion1.getPK_promotionID(), promotionResponse.getPromotionID());
//        assertEquals(promotion1.getCode(), promotionResponse.getCode());
//        assertEquals(promotion1.getDescription(), promotionResponse.getDescription());
//        assertEquals(promotion1.getDiscount(), promotionResponse.getDiscount());
//        assertEquals("2024-06-01", promotionResponse.getStartDate()); // Check formatted date
//        assertEquals("2024-06-30", promotionResponse.getEndDate()); // Check formatted date
//        assertTrue(promotionResponse.isStatus());
//    }
//
//    @Test
//    public void testUpdatePromotionDetails() {
//        // Mock data
//        long promotionId = 1L;
//        PromotionRequest promotionRequest = new PromotionRequest();
//        promotionRequest.setPK_promotionID(promotionId);
//        promotionRequest.setCode("SUMMER20");
//        promotionRequest.setDescription("Summer Sale 2024");
//        promotionRequest.setStartDate(java.sql.Date.valueOf("2024-08-10").toLocalDate());
//        promotionRequest.setEndDate(java.sql.Date.valueOf("2024-08-30").toLocalDate());
//        promotionRequest.setDiscount(20);
//
//        Promotion existingPromotion = new Promotion();
//        existingPromotion.setPK_promotionID(promotionId);
//        existingPromotion.setCode("OLD_CODE");
//        existingPromotion.setDescription("Old Description");
//        existingPromotion.setStartDate(java.sql.Date.valueOf("2024-08-10"));
//        existingPromotion.setEndDate(java.sql.Date.valueOf("2024-08-30"));
//        existingPromotion.setDiscount(10);
//        existingPromotion.setStatus(true);
//
//        // Mock repository behavior
//        when(promotionRepository.findById(promotionId)).thenReturn(Optional.of(existingPromotion));
//        when(promotionRepository.save(any(Promotion.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved promotion
//
//        // Test method
//        promotionService.updatePromotionDetails(promotionRequest);
//
//        // Verify save call
//        verify(promotionRepository, times(1)).save(any(Promotion.class));
//
//        // Assertions
//        assertEquals(promotionRequest.getPK_promotionID(), existingPromotion.getPK_promotionID());
//        assertEquals(promotionRequest.getCode(), existingPromotion.getCode());
//        assertEquals(promotionRequest.getDescription(), existingPromotion.getDescription());
//        assertEquals(promotionRequest.getDiscount(), existingPromotion.getDiscount());
//        assertEquals(java.sql.Date.valueOf("2024-08-10"), existingPromotion.getStartDate());
//        assertEquals(java.sql.Date.valueOf("2024-08-30"), existingPromotion.getEndDate());
//    }
//    @Test
//    public void testUpdatePromotionDetails_InvalidDiscount() {
//        // Mock data
//        long promotionId = 1L;
//        PromotionRequest promotionRequest = new PromotionRequest();
//        promotionRequest.setPK_promotionID(promotionId);
//        promotionRequest.setCode("SUMMER20");
//        promotionRequest.setDescription("Summer Sale 2024");
//        promotionRequest.setStartDate(java.sql.Date.valueOf("2024-08-10").toLocalDate());
//        promotionRequest.setEndDate(java.sql.Date.valueOf("2024-08-30").toLocalDate());
//        promotionRequest.setDiscount(120); // Invalid discount value
//
//        Promotion existingPromotion = new Promotion();
//        existingPromotion.setPK_promotionID(promotionId);
//        existingPromotion.setCode("OLD_CODE");
//        existingPromotion.setDescription("Old Description");
//        existingPromotion.setStartDate(java.sql.Date.valueOf("2024-08-10"));
//        existingPromotion.setEndDate(java.sql.Date.valueOf("2024-08-30"));
//        existingPromotion.setDiscount(10);
//        existingPromotion.setStatus(true);
//
//        // Mock repository behavior
//        when(promotionRepository.findById(promotionId)).thenReturn(Optional.of(existingPromotion));
//
//        // Test method and validate exception
//        assertThrows(RuntimeException.class, () -> promotionService.updatePromotionDetails(promotionRequest));
//
//        // Verify save was not called
//        verify(promotionRepository, never()).save(any());
//    }
//
//    @Test
//    public void testDeletePromotionById() {
//        // Mock data
//        long promotionId = 1L;
//        Promotion promotionToDelete = new Promotion();
//        promotionToDelete.setPK_promotionID(promotionId);
//        promotionToDelete.setCode("SUMMER20");
//        promotionToDelete.setDescription("Summer Sale 2024");
//        promotionToDelete.setStartDate(java.sql.Date.valueOf("2024-07-01"));
//        promotionToDelete.setEndDate(java.sql.Date.valueOf("2024-07-30"));
//        promotionToDelete.setDiscount(20);
//        promotionToDelete.setStatus(true);
//
//        // Mocking getOrderDetails() to return null
//        promotionToDelete.setOrderDetails(null);
//
//        when(promotionRepository.findById(promotionId)).thenReturn(Optional.of(promotionToDelete));
//
//        // Test method
//        promotionService.deletePromotionById(promotionId);
//
//        // Verify status update
//        verify(promotionRepository, times(1)).save(promotionToDelete);
//
//        // Assertions
//        assertFalse(promotionToDelete.isStatus());
//    }
//    @Test
//    public void testReadAllPromotion() throws ParseException {
//        // Mock data
//        List<Promotion> promotions = new ArrayList<>();
//        promotions.add(createPromotion(1L, "PROMO1", "Promotion 1", "2024-07-01", "2024-07-15", 10, true));
//        promotions.add(createPromotion(2L, "PROMO2", "Promotion 2", "2024-07-10", "2024-07-20", 15, true));
//
//        when(promotionRepository.findAll()).thenReturn(promotions);
//
//        // Test method
//        List<PromotionResponse> promotionResponses = promotionService.readAllPromotion();
//
//        // Assertions
//        assertNotNull(promotionResponses);
//        assertEquals(2, promotionResponses.size());
//
//        PromotionResponse promo1Response = promotionResponses.get(0);
//        assertEquals("PROMO1", promo1Response.getCode());
//        assertEquals("Promotion 1", promo1Response.getDescription());
//        assertEquals(10, promo1Response.getDiscount());
//        assertEquals("2024-07-01", promo1Response.getStartDate()); // Check formatted date
//        assertEquals("2024-07-15", promo1Response.getEndDate()); // Check formatted date
//        assertTrue(promo1Response.isStatus());
//
//        PromotionResponse promo2Response = promotionResponses.get(1);
//        assertEquals("PROMO2", promo2Response.getCode());
//        assertEquals("Promotion 2", promo2Response.getDescription());
//        assertEquals(15, promo2Response.getDiscount());
//        assertEquals("2024-07-10", promo2Response.getStartDate()); // Check formatted date
//        assertEquals("2024-07-20", promo2Response.getEndDate()); // Check formatted date
//        assertTrue(promo2Response.isStatus());
//
//        // Verify repository method call
//        verify(promotionRepository, times(1)).findAll();
//    }
//
//    @Test
//    public void testGetPromotionsByDate() throws ParseException {
//        // Mock data
//        List<Promotion> promotions = new ArrayList<>();
//        promotions.add(createPromotion(1L, "PROMO1", "Promotion 1", "2024-07-01", "2024-07-15", 10, true));
//        promotions.add(createPromotion(2L, "PROMO2", "Promotion 2", "2024-07-10", "2024-07-20", 15, true));
//
//        when(promotionRepository.findAll()).thenReturn(promotions);
//
//        // Test method
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date targetDate = dateFormat.parse("2024-07-12");
//        List<PromotionResponse> promotionResponses = promotionService.getPromotionsByDate(targetDate);
//
//        // Assertions
//        assertNotNull(promotionResponses);
//        assertEquals(2, promotionResponses.size());
//
//        PromotionResponse promo1Response = promotionResponses.get(0);
//        assertEquals("PROMO1", promo1Response.getCode());
//        assertEquals("Promotion 1", promo1Response.getDescription());
//        assertEquals(10, promo1Response.getDiscount());
//        assertEquals("2024-07-01", promo1Response.getStartDate()); // Check formatted date
//        assertEquals("2024-07-15", promo1Response.getEndDate()); // Check formatted date
//        assertTrue(promo1Response.isStatus());
//
//        PromotionResponse promo2Response = promotionResponses.get(1);
//        assertEquals("PROMO2", promo2Response.getCode());
//        assertEquals("Promotion 2", promo2Response.getDescription());
//        assertEquals(15, promo2Response.getDiscount());
//        assertEquals("2024-07-10", promo2Response.getStartDate()); // Check formatted date
//        assertEquals("2024-07-20", promo2Response.getEndDate()); // Check formatted date
//        assertTrue(promo2Response.isStatus());
//
//        // Verify repository method call
//        verify(promotionRepository, times(1)).findAll();
//    }
//
//    @Test
//    public void testUpdatePromotionDetails_InvalidStartDate() throws ParseException {
//        // Mock data
//        long promotionId = 1L;
//        PromotionRequest promotionRequest = new PromotionRequest();
//        promotionRequest.setPK_promotionID(promotionId);
//        promotionRequest.setCode("SUMMER20");
//        promotionRequest.setDescription("Summer Sale 2024");
//        promotionRequest.setStartDate(java.sql.Date.valueOf("2024-06-10").toLocalDate()); // Invalid start date
//        promotionRequest.setEndDate(java.sql.Date.valueOf("2024-06-30").toLocalDate());
//        promotionRequest.setDiscount(20);
//
//        Promotion existingPromotion = new Promotion();
//        existingPromotion.setPK_promotionID(promotionId);
//        existingPromotion.setCode("OLD_CODE");
//        existingPromotion.setDescription("Old Description");
//        existingPromotion.setStartDate(java.sql.Date.valueOf("2024-08-10"));
//        existingPromotion.setEndDate(java.sql.Date.valueOf("2024-08-30"));
//        existingPromotion.setDiscount(10);
//        existingPromotion.setStatus(true);
//
//        // Mock repository behavior
//        when(promotionRepository.findById(promotionId)).thenReturn(Optional.of(existingPromotion));
//
//        // Test method and validate exception
//        assertThrows(IllegalArgumentException.class, () -> promotionService.updatePromotionDetails(promotionRequest));
//
//        // Verify save was not called
//        verify(promotionRepository, never()).save(any());
//    }
//
//
//    private Promotion createPromotion(Long id, String code, String description, String startDate, String endDate, int discount, boolean status) throws ParseException {
//        Promotion promotion = new Promotion();
//        promotion.setPK_promotionID(id);
//        promotion.setCode(code);
//        promotion.setDescription(description);
//        promotion.setStartDate(java.sql.Date.valueOf(startDate));
//        promotion.setEndDate(java.sql.Date.valueOf(endDate));
//        promotion.setDiscount(discount);
//        promotion.setStatus(status);
//        return promotion;
//    }
//}
