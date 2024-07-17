package com.project.JewelryMS.UnitTest;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductBuy;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.PricingRatioRepository;
import com.project.JewelryMS.repository.ProductBuyRepository;
import com.project.JewelryMS.service.ApiService;
import com.project.JewelryMS.service.ImageService;
import com.project.JewelryMS.service.ProductBuyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductBuyServiceUnitTest {

    @Mock
    private ProductBuyRepository productBuyRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ApiService apiService;

    @Mock
    private ImageService imageService;

    @Mock
    private PricingRatioRepository pricingRatioRepository;

    @InjectMocks
    private ProductBuyService productBuyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock gold price API response
        when(apiService.getGoldBuyPricecalculate("http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v"))
                .thenReturn("50000000.0");
    }

    @Test
    public void testCreateProductBuy() {
        CreateProductBuyRequest request = new CreateProductBuyRequest();
        request.setName("Test Product");
        request.setCategory_id(1L);
        request.setMetalType("Gold");
        request.setGemstoneType("Diamond");
        request.setMetalWeight(10.0F);
        request.setGemstoneWeight(5.0f);
        request.setCost(1000.0f);
        request.setImage("data:image/jpeg;base64,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");

        Category category = new Category();
        category.setId(1L);
        category.setName("Rings");

        when(categoryRepository.findCategoryById(1L)).thenReturn(Optional.of(category));
        when(imageService.uploadImageByPathService(any(MultipartFile.class))).thenReturn("http://image-url.com");
        when(productBuyRepository.save(any(ProductBuy.class))).thenAnswer(i -> {
            ProductBuy productBuy = i.getArgument(0);
            productBuy.setPK_ProductBuyID(1L);
            return productBuy;
        });

        Long productBuyId = productBuyService.createProductBuy(request);
        assertNotNull(productBuyId);
        assertEquals(1L, productBuyId);
    }

//    @Test
//    public void testCalculateProductBuyCost() {
//        CalculatePBRequest request = new CalculatePBRequest();
//        request.setGemstoneType("Diamond");
//        request.setMetalType("Gold");
//        request.setGemstoneWeight(5.0f);
//        request.setMetalWeight(10f);
//
//        when(apiService.getGoldBuyPricecalculate(anyString())).thenReturn("1000");
//        productBuyService.initializeGoldPrice();
//
//        Float cost = productBuyService.calculateProductBuyCost(request);
//        assertNotNull(cost);
//        assertEquals(400001.0F, cost);
//    }

    @Test
    public void testGetProductBuyById() {
        ProductBuy productBuy = new ProductBuy();
        productBuy.setPK_ProductBuyID(1L);
        Category category = new Category();
        category.setName("Rings");
        productBuy.setCategory(category);
        productBuy.setPbName("Test Product");
        productBuy.setMetalType("Gold");
        productBuy.setGemstoneType("Diamond");
        productBuy.setPbCost(1000.0f);

        when(productBuyRepository.findById(1L)).thenReturn(Optional.of(productBuy));

        ProductBuyResponse response = productBuyService.getProductBuyById(1L);
        assertNotNull(response);
        assertEquals("Test Product", response.getPbName());
        assertEquals("Rings", response.getCategoryName());
    }

    @Test
    public void testDeleteProductBuy() {
        ProductBuy productBuy = new ProductBuy();
        productBuy.setPK_ProductBuyID(1L);

        when(productBuyRepository.findById(1L)).thenReturn(Optional.of(productBuy));

        String response = productBuyService.deleteProductBuy(1L);
        assertEquals("Product Buy1delete sucessfully!", response);
        verify(productBuyRepository, times(1)).save(productBuy);
    }

    @Test
    void testGetAllProductBuys() {
        // Prepare test data
        ProductBuy productBuy = new ProductBuy();
        productBuy.setPbName("Test Product");

        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        productBuy.setCategory(category);

        when(productBuyRepository.findAll()).thenReturn(Collections.singletonList(productBuy));

        // Call the method
        List<ProductBuyResponse> productBuyResponses = productBuyService.getAllProductBuys();

        // Verify results
        assertNotNull(productBuyResponses);
        assertFalse(productBuyResponses.isEmpty());
    }

}