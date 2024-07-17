package com.project.JewelryMS.SystemTest;


import com.project.JewelryMS.controller.DashBoardController;
import com.project.JewelryMS.model.Dashboard.*;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerDemographics;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerLoyalty;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerPurchaseHistoryResponse;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerSignUp;
import com.project.JewelryMS.model.Transition.TransitionResponse;
import com.project.JewelryMS.service.DashboardService;
import com.project.JewelryMS.service.TransitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @Mock
    private TransitionService transitionService;

    @InjectMocks
    private DashBoardController dashBoardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPurchaseOrderHistory() {
        List<TransitionResponse> mockResponse = new ArrayList<>();
        when(transitionService.getPurchaseOrderHistory()).thenReturn(mockResponse);

        ResponseEntity<List<TransitionResponse>> response = dashBoardController.getPurchaseOrderHistory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetAllRevenueCategory() {
        List<CategoryResponse> mockResponse = new ArrayList<>();
        when(dashboardService.RevenueCategory(any(RevenueDateRequest.class))).thenReturn(mockResponse);

        ResponseEntity<List<CategoryResponse>> response = dashBoardController.getAllRevenueCategory("2023-01-01", "2023-12-31");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetTopSellingProducts() {
        List<TopSellProductResponse> mockResponse = new ArrayList<>();
        when(dashboardService.getTopSellingProducts(any(RevenueDateRequest.class))).thenReturn(mockResponse);

        ResponseEntity<List<TopSellProductResponse>> response = dashBoardController.getTopSellingProducts("2023-01-01", "2023-12-31");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetCustomerLoyaltyStatistics() {
        List<CustomerLoyalty> mockResponse = new ArrayList<>();
        when(dashboardService.getCustomerLoyaltyStatistics(any(RevenueDateRequest.class))).thenReturn(mockResponse);

        ResponseEntity<List<CustomerLoyalty>> response = dashBoardController.getCustomerLoyaltyStatistics("2023-01-01", "2023-12-31");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetCustomerDemoGraphic() {
        List<CustomerDemographics> mockResponse = new ArrayList<>();
        when(dashboardService.getCustomerDemoGraphicResponse(any(RevenueDateRequest.class))).thenReturn(mockResponse);

        ResponseEntity<List<CustomerDemographics>> response = dashBoardController.getCustomerDemoGraphic("2023-01-01", "2023-12-31");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetCustomerSignUpsByStaff() {
        List<CustomerSignUp> mockResponse = new ArrayList<>();
        when(dashboardService.getCustomerSignUpsByStaff(any(RevenueDateRequest.class))).thenReturn(mockResponse);

        ResponseEntity<List<CustomerSignUp>> response = dashBoardController.getCustomerSignUpsByStaff("2023-01-01", "2023-12-31");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testComparationYear() {
        YearComparisonResponse mockResponse = new YearComparisonResponse();
        when(dashboardService.compareYear(any(YearComparisonRequest.class))).thenReturn(mockResponse);

        YearComparisonResponse response = dashBoardController.comparationYear("2022", "2023");

        assertEquals(mockResponse, response);
    }

    @Test
    void testGetRevenueGeneratedByStaff() {
        List<StaffRevenueResponse> mockResponse = new ArrayList<>();
        when(dashboardService.getRevenueGeneratedByStaff(any(LocalDate.class), any(LocalDate.class))).thenReturn(mockResponse);

        ResponseEntity<List<StaffRevenueResponse>> response = dashBoardController.getRevenueGeneratedByStaff(LocalDate.now(), LocalDate.now().plusMonths(1));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetDiscountCodeEffectiveness() {
        List<DiscountEffectivenessResponse> mockResponse = new ArrayList<>();
        when(dashboardService.getDiscountCodeEffectiveness()).thenReturn(mockResponse);

        List<DiscountEffectivenessResponse> response = dashBoardController.getDiscountCodeEffectiveness();

        assertEquals(mockResponse, response);
    }

    @Test
    void testGetSalesMadeByStaff() {
        List<StaffSalesResponse> mockResponse = new ArrayList<>();
        when(dashboardService.getSalesMadeByStaff(any(LocalDate.class), any(LocalDate.class))).thenReturn(mockResponse);

        ResponseEntity<List<StaffSalesResponse>> response = dashBoardController.getSalesMadeByStaff(LocalDate.now(), LocalDate.now().plusMonths(1));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetTopProductsForAllCustomers() {
        List<ProductSellTrendResponse> mockResponse = new ArrayList<>();
        when(dashboardService.getTopProductsForAllCustomers()).thenReturn(mockResponse);

        ResponseEntity<List<ProductSellTrendResponse>> response = dashBoardController.getTopProductsForAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

//    @Test
//    void testGetMonthlyAverageRevenue() {
//        Map<String, Float> mockResponse = new HashMap<>();
//        when(dashboardService.getDailyAverageRevenuePerMonth(anyString(), anyString())).thenReturn(mockResponse);
//
//        ResponseEntity<Map<String, Float>> response = dashBoardController.getMonthlyAverageRevenue("2024-01", "2024-12");
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockResponse, response.getBody());
//    }



    @Test
    void testGetAllCustomerPurchaseHistories() {
        List<CustomerPurchaseHistoryResponse> mockResponse = new ArrayList<>();
        when(dashboardService.getAllCustomerPurchaseHistories()).thenReturn(mockResponse);

        ResponseEntity<List<CustomerPurchaseHistoryResponse>> response = dashBoardController.getAllCustomerPurchaseHistories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetStaffStats() {
        StaffStatisticsResponse mockResponse = new StaffStatisticsResponse();
        when(dashboardService.getStaffStats(anyLong())).thenReturn(mockResponse);

        ResponseEntity<StaffStatisticsResponse> response = dashBoardController.getStaffStats(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }
}