package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Dashboard.*;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerDemographics;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerLoyalty;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerPurchaseHistoryResponse;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerSignUp;
import com.project.JewelryMS.model.Transition.TransitionResponse;
import com.project.JewelryMS.service.DashboardService;
import com.project.JewelryMS.service.TransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Dashboard")
public class DashBoardController {
    @Autowired
    DashboardService dashboardService;
    @Autowired
    private TransitionService transitionService;

    @GetMapping("/purchase-order-history")
    public ResponseEntity<List<TransitionResponse>> getPurchaseOrderHistory() {
        List<TransitionResponse> purchaseOrderHistory = transitionService.getPurchaseOrderHistory();
        return ResponseEntity.ok(purchaseOrderHistory);
    }

    @GetMapping("revenue-category")
    public ResponseEntity<List<CategoryResponse>> getAllRevenueCategory(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        List<CategoryResponse> revenueCategoryResponse = dashboardService.RevenueCategory(revenueDateRequest);
        return ResponseEntity.ok(revenueCategoryResponse);
    }

    @GetMapping("top-selling-products")
    public ResponseEntity<List<TopSellProductResponse>> getTopSellingProducts(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getTopSellingProducts(revenueDateRequest));
    }

    @GetMapping("revenue-category-all")
    public ResponseEntity<List<CategoryResponse>> getAllRevenueCategory() {
        List<CategoryResponse> revenueCategoryResponse = dashboardService.RevenueCategory();
        return ResponseEntity.ok(revenueCategoryResponse);
    }

    @GetMapping("top-selling-products-all")
    public ResponseEntity<List<TopSellProductResponse>> getTopSellingProducts() {
        return ResponseEntity.ok(dashboardService.getTopSellingProducts());
    }

    @GetMapping("/loyalty-customers")
    public ResponseEntity<List<CustomerLoyalty>> getCustomerLoyaltyStatistics(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getCustomerLoyaltyStatistics(revenueDateRequest));
    }

    @GetMapping("demographic-customers")
    public ResponseEntity<List<CustomerDemographics>> getCustomerDemoGraphic(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getCustomerDemoGraphicResponse(revenueDateRequest));
    }

    @GetMapping("/customer-signups")
    public ResponseEntity<List<CustomerSignUp>> getCustomerSignUpsByStaff(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        RevenueDateRequest revenueDateRequest = new RevenueDateRequest(LocalDate.parse(startTime), LocalDate.parse(endTime));
        return ResponseEntity.ok(dashboardService.getCustomerSignUpsByStaff(revenueDateRequest));
    }


    @GetMapping("compare-sale-year")
    public YearComparisonResponse comparationYear(@RequestParam("year1") String year1, @RequestParam("year2") String year2) {
        YearComparisonRequest request = new YearComparisonRequest(year1, year2);
        return dashboardService.compareYear(request);
    }
    @GetMapping("/revenueByStaff")
    public ResponseEntity<List<StaffRevenueResponse>> getRevenueGeneratedByStaff(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<StaffRevenueResponse> response = dashboardService.getRevenueGeneratedByStaff(startDate, endDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }
    @GetMapping("discount-code-effectiveness")
    public List<DiscountEffectivenessResponse> getDiscountCodeEffectiveness() {
        return dashboardService.getDiscountCodeEffectiveness();
    }


    @GetMapping("/salesByStaff")
    public ResponseEntity<List<StaffSalesResponse>> getSalesMadeByStaff(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<StaffSalesResponse> response = dashboardService.getSalesMadeByStaff(startDate, endDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

    @GetMapping("/customer-products-trend")
    public ResponseEntity<List<ProductSellTrendResponse>> getTopProductsForAllCustomers() {
        List<ProductSellTrendResponse> responses = dashboardService.getTopProductsForAllCustomers();
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/monthlyAverageRevenue")
    public ResponseEntity<Map<String, Float>> getMonthlyAverageRevenue(
            @RequestParam String startMonthYear, @RequestParam String endMonthYear) {
        Map<String, Float> dailyAverageRevenue = dashboardService.getDailyAverageRevenuePerMonth(startMonthYear, endMonthYear);
        if (dailyAverageRevenue.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(dailyAverageRevenue);
        }
    }

    @GetMapping("/daily-revenue")
    public ResponseEntity<Map<String, Double>> getDailyRevenue(
            @RequestParam String startDate, @RequestParam String endDate) {
        try {
            Map<String, Double> dailyRevenue = dashboardService.getDailyRevenue(startDate, endDate);
            return ResponseEntity.ok(dailyRevenue);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/customer-purchase-history")
    public ResponseEntity<List<CustomerPurchaseHistoryResponse>> getAllCustomerPurchaseHistories() {
        List<CustomerPurchaseHistoryResponse> response = dashboardService.getAllCustomerPurchaseHistories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/staff-statistics/{staffId}")
    public ResponseEntity<StaffStatisticsResponse> getStaffStats(@RequestParam("staffId") long staffId) {
        StaffStatisticsResponse response = dashboardService.getStaffStats(staffId);
        return ResponseEntity.ok(response);
    }
}
