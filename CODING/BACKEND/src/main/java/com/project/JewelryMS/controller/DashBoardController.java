package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Dashboard.*;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerDemographics;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerLoyalty;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerSignUp;
import com.project.JewelryMS.model.Transition.TransitionResponse;
import com.project.JewelryMS.service.DashboardService;
import com.project.JewelryMS.service.TransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("api/Dashboard")
public class DashBoardController {
    @Autowired
    DashboardService dashboardService;
    @Autowired
    private TransitionService transitionService;

    @GetMapping("/purchase-order-history")
    public ResponseEntity<List<TransitionResponse>> getPurchaseOrderHistory() {
        try {
            List<TransitionResponse> purchaseOrderHistory = transitionService.getPurchaseOrderHistory();
            return ResponseEntity.ok(purchaseOrderHistory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

    @GetMapping("revenue-category")
    public ResponseEntity<List<CategoryResponse>> getAllRevenueCategory(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
        try {
            LocalDate startDate = LocalDate.parse(startTime);
            LocalDate endDate = LocalDate.parse(endTime);

            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().body(null); // Or handle with a custom error response
            }

            RevenueDateRequest revenueDateRequest = new RevenueDateRequest(startDate, endDate);
            List<CategoryResponse> revenueCategoryResponse = dashboardService.RevenueCategory(revenueDateRequest);

            return ResponseEntity.ok(revenueCategoryResponse);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null); // Invalid date format
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

    @GetMapping("top-selling-products")
    public ResponseEntity<List<TopSellProductResponse>> getTopSellingProducts(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
        try {
            LocalDate startDate = LocalDate.parse(startTime);
            LocalDate endDate = LocalDate.parse(endTime);

            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().body(null);
            }

            RevenueDateRequest revenueDateRequest = new RevenueDateRequest(startDate, endDate);
            List<TopSellProductResponse> topSellingProducts = dashboardService.getTopSellingProducts(revenueDateRequest);

            return ResponseEntity.ok(topSellingProducts);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null); // Invalid date format
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

    @GetMapping("revenue-category-all")
    public ResponseEntity<List<CategoryResponse>> getAllRevenueCategory() {
        try {
            List<CategoryResponse> revenueCategoryResponse = dashboardService.RevenueCategory();
            return ResponseEntity.ok(revenueCategoryResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

    @GetMapping("top-selling-products-all")
    public ResponseEntity<List<TopSellProductResponse>> getTopSellingProducts() {
        try {
            List<TopSellProductResponse> topSellingProducts = dashboardService.getTopSellingProducts();
            return ResponseEntity.ok(topSellingProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

    @GetMapping("/customer-signups")
    public ResponseEntity<List<CustomerSignUp>> getCustomerSignUpsByStaff(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
        try {
            LocalDate startDate = LocalDate.parse(startTime);
            LocalDate endDate = LocalDate.parse(endTime);

            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().body(null); // Or handle with a custom error response
            }

            RevenueDateRequest revenueDateRequest = new RevenueDateRequest(startDate, endDate);
            List<CustomerSignUp> customerSignUps = dashboardService.getCustomerSignUpsByStaff(revenueDateRequest);

            return ResponseEntity.ok(customerSignUps);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null); // Invalid date format
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

    @GetMapping("/loyalty-customers")
    public ResponseEntity<CustomerLoyalty> getCustomerLoyaltyStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            RevenueDateRequest revenueDateRequest = new RevenueDateRequest(startDate, endDate);
            List<CustomerLoyalty> loyaltyStats = dashboardService.getCustomerLoyaltyStatistics(revenueDateRequest);

            return ResponseEntity.ok(loyaltyStats.get(0));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

    @GetMapping("/demographic-customers")
    public ResponseEntity<CustomerDemographics> getCustomerDemoGraphic(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            RevenueDateRequest revenueDateRequest = new RevenueDateRequest(startDate, endDate);
            List<CustomerDemographics> demographicStats = dashboardService.getCustomerDemoGraphicResponse(revenueDateRequest);

            return ResponseEntity.ok(demographicStats.get(0));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

    @GetMapping("compare-day")
    public ComparisonResponse compareDay(@RequestParam("date1") String date1, @RequestParam("date2") String date2) {
        DayComparisonRequest request = new DayComparisonRequest(LocalDate.parse(date1), LocalDate.parse(date2));
        return dashboardService.compareDay(request);
    }

    @GetMapping("compare-month")
    public ComparisonResponse compareMonth(@RequestParam("month1") String month1, @RequestParam("month2") String month2) {
        MonthComparisonRequest request = new MonthComparisonRequest(month1, month2);
        return dashboardService.compareMonth(request);
    }

    @GetMapping("compare-year")
    public ComparisonResponse compareYear(@RequestParam("year1") String year1, @RequestParam("year2") String year2) {
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
}
