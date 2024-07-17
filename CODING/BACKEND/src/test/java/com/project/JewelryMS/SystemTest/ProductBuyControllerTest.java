package com.project.JewelryMS.SystemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.JewelryMS.controller.ProductBuyController;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.CalculatePBRequest;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.service.ProductBuyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductBuyControllerTest {

    @Mock
    private ProductBuyService productBuyService;

    @InjectMocks
    private ProductBuyController productBuyController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSaleCreateBuyOrder() throws Exception {
        CreateProductBuyRequest request = new CreateProductBuyRequest();
        CreateProductBuyRequest[] requestArray = {request};
        String orderJson = objectMapper.writeValueAsString(requestArray);
        byte[] requestData = orderJson.getBytes();

        when(productBuyService.createProductBuy(any(CreateProductBuyRequest.class))).thenReturn(1L);

        ResponseEntity<List<Long>> response = productBuyController.saleCreateBuyOrder(requestData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0));
    }

//    @Test
//    void testGetAllProductBuys() {
//        List<ProductBuyResponse> mockResponse = new ArrayList<>();
//        when(productBuyService.getAllProductBuys()).thenReturn(mockResponse);
//
//        ResponseEntity<List<ProductBuyResponse>> response = productBuyController.getAllProductBuysByOrderStatus3();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockResponse, response.getBody());
//    }

    @Test
    void testGetProductBuyById() {
        ProductBuyResponse mockResponse = new ProductBuyResponse();
        when(productBuyService.getProductBuyById(anyLong())).thenReturn(mockResponse);

        ResponseEntity<ProductBuyResponse> response = productBuyController.getProductBuyById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testDeleteProductBuy() {
        String mockResponse = "Product buy deleted successfully";
        when(productBuyService.deleteProductBuy(anyLong())).thenReturn(mockResponse);

        ResponseEntity<String> response = productBuyController.deleteProductBuy(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testCalculateProductBuyCost() {
        CalculatePBRequest request = new CalculatePBRequest();
        float mockCost = 100.0f;
        when(productBuyService.calculateProductBuyCost(any(CalculatePBRequest.class))).thenReturn(mockCost);

        ResponseEntity<Float> response = productBuyController.calculateProductBuyCost(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCost, response.getBody());
    }

    @Test
    void testAdjustRatio() {
        float ratio = 1.5f;

        ResponseEntity<Float> response = productBuyController.adjustRatio(ratio);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ratio, response.getBody());
        verify(productBuyService).updatePricingRatioPB(ratio);
    }
}