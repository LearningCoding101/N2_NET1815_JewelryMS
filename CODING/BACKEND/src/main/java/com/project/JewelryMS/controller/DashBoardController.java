package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Dashboard.CategoryResponse;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerDemographicsResponse;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerLoyaltyResponse;
import com.project.JewelryMS.model.Dashboard.Customer.CustomerSignUpResponse;
import com.project.JewelryMS.model.Dashboard.RevenueDateRequest;
import com.project.JewelryMS.model.Dashboard.TopSellProductResponse;
import com.project.JewelryMS.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/Dashboard")
public class DashBoardController {
    @Autowired
    DashboardService dashboardService;

    @PostMapping("revenue-category")
    public ResponseEntity<List<CategoryResponse>> GetAllRevenueCategory(@RequestBody RevenueDateRequest revenueDateRequest){
        List<CategoryResponse> revenueCategoryResponse = dashboardService.RevenueCategory(revenueDateRequest);
        return ResponseEntity.ok(revenueCategoryResponse);
    }

    @PostMapping("top-selling-products")
    public ResponseEntity<List<TopSellProductResponse>> getTopSellingProducts(@RequestBody RevenueDateRequest revenueDateRequest) {
        return ResponseEntity.ok(dashboardService.getTopSellingProducts(revenueDateRequest));
    }

    @PostMapping("revenue-category-all")
    public ResponseEntity<List<CategoryResponse>> GetAllRevenueCategory(){
        List<CategoryResponse> revenueCategoryResponse = dashboardService.RevenueCategory();
        return ResponseEntity.ok(revenueCategoryResponse);
    }

    @PostMapping("top-selling-products-all")
    public ResponseEntity<List<TopSellProductResponse>> getTopSellingProducts() {
        return ResponseEntity.ok(dashboardService.getTopSellingProducts());
    }

    @PostMapping("/loyalty-customers")
    public ResponseEntity<CustomerLoyaltyResponse> getCustomerLoyaltyStatistics(@RequestBody RevenueDateRequest revenueDateRequest) {
        return ResponseEntity.ok(dashboardService.getCustomerLoyaltyStatistics(revenueDateRequest));
    }

    @PostMapping("demographic-customers")
    public ResponseEntity<CustomerDemographicsResponse> getCustomerDemoGraphic(@RequestBody RevenueDateRequest revenueDateRequest){
        return ResponseEntity.ok(dashboardService.getCustomerDemoGraphicResponse(revenueDateRequest));
    }

    @PostMapping("/customer-signups")
    public ResponseEntity<CustomerSignUpResponse> getCustomerSignUpsByStaff(@RequestBody RevenueDateRequest revenueDateRequest) {
        return ResponseEntity.ok(dashboardService.getCustomerSignUpsByStaff(revenueDateRequest));
    }
}
