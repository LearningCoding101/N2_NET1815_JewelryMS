package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.ProductSellController;
import com.project.JewelryMS.model.ProductSell.*;
import com.project.JewelryMS.service.ProductSellService;
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

class ProductSellControllerTest {

    @Mock
    private ProductSellService productSellService;

    @InjectMocks
    private ProductSellController productSellController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProductSell() {
        CreateProductSellRequest request = new CreateProductSellRequest();
        ProductSellResponse response = new ProductSellResponse();
        when(productSellService.createProductSell(request)).thenReturn(response);

        ResponseEntity<ProductSellResponse> result = productSellController.createProductSell(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void readAllProductSell() {
        List<ProductSellResponse> products = Arrays.asList(new ProductSellResponse(), new ProductSellResponse());
        when(productSellService.getAllProductSellResponses()).thenReturn(products);

        ResponseEntity<List<ProductSellResponse>> result = productSellController.readAllProductSell();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(products, result.getBody());
    }

    @Test
    void getProductSellById() {
        long id = 1L;
        ProductSellResponse response = new ProductSellResponse();
        when(productSellService.getProductSellById2(id)).thenReturn(response);

        ResponseEntity<ProductSellResponse> result = productSellController.getProductSellById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void updateProductSell() {
        long id = 1L;
        ProductSellRequest request = new ProductSellRequest();
        ProductSellResponse response = new ProductSellResponse();
        when(productSellService.updateProductSell(id, request)).thenReturn(response);

        ResponseEntity<ProductSellResponse> result = productSellController.updateProductSell(id, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void deleteProductSell() {
        long id = 1L;

        ResponseEntity<String> result = productSellController.deleteProductSell(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Product deleted successfully", result.getBody());
        verify(productSellService).deleteProduct(id);
    }

    @Test
    void adjustRatio() {
        Float ratio = 1.5f;

        ResponseEntity<Float> result = productSellController.adjustRatio(ratio);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(ratio, result.getBody());
        verify(productSellService).updatePricingRatioPS(ratio);
    }
}