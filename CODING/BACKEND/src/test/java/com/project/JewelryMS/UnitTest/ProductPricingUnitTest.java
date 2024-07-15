package com.project.JewelryMS.UnitTest;

import com.project.JewelryMS.entity.PricingRatio;
import com.project.JewelryMS.model.ProductBuy.CalculatePBRequest;
import com.project.JewelryMS.repository.PricingRatioRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.service.ApiService;
import com.project.JewelryMS.service.ProductBuyService;
import com.project.JewelryMS.service.ProductSellService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ProductPricingUnitTest {

    @InjectMocks
    private ProductSellService productSellService;

    @Mock
    private ApiService apiService;

    @Mock
    private PricingRatioRepository pricingRatioRepository;

    @Mock
    private ProductSellRepository productSellRepository;

    @InjectMocks
    private ProductBuyService productBuyService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock gold price API response
        when(apiService.getGoldBuyPricecalculate("http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v"))
                .thenReturn("50000000.0");

        // Mock pricing ratio repository response
        PricingRatio pricingRatio = new PricingRatio();
        pricingRatio.setPricingRatioPS(1.2F);
        when(pricingRatioRepository.findById(1L)).thenReturn(Optional.of(pricingRatio));

        // Initialize gold price
        productSellService.initializeGoldPrice();
        productBuyService.initializeGoldPrice();
    }

//    @Test
//    public void testCalculateProductSellCost() {
//        Float chi = 10.0F;
//        Float carat = 2.0F;
//        String gemstoneType = "Diamond";
//        String metalType = "Gold";
//        Float manufacturerCost = 1000000.0F;
//
//        Float expectedCost = 301200.0F;
//
//        Float calculatedCost = productSellService.calculateProductSellCost(chi, carat, gemstoneType, metalType, manufacturerCost);
//
//        assertEquals(expectedCost, calculatedCost);
//    }

//    @Test
//    void testCalculateProductBuyCost() {
//        // Prepare test data
//        CalculatePBRequest request = new CalculatePBRequest();
//        request.setGemstoneType("Diamond");
//        request.setMetalType("Gold");
//        request.setGemstoneWeight(1.5F);
//        request.setMetalWeight(5f);
//
//        when(apiService.getGoldBuyPricecalculate(anyString())).thenReturn("5000000");
//
//        Float expectedTotalPrice = 122500.0F;
//
//        // Call the method
//        Float cost = productBuyService.calculateProductBuyCost(request);
//
//        // Verify results
//        assertEquals(expectedTotalPrice, cost);
//    }

}

