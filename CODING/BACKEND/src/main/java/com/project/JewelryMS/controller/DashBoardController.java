package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Dashboard.RevenueCategoryResponse;
import com.project.JewelryMS.model.Dashboard.RevenueDateRequest;
import com.project.JewelryMS.model.Dashboard.RevenueProductResponse;
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
    public ResponseEntity<RevenueCategoryResponse> GetAllRevenueCategory(@RequestBody RevenueDateRequest revenueDateRequest){
        RevenueCategoryResponse revenueCategoryResponse = dashboardService.RevenueCategory(revenueDateRequest);
        return ResponseEntity.ok(revenueCategoryResponse);
    }

    @PostMapping("top-selling-products")
    public ResponseEntity<RevenueProductResponse> getTopSellingProducts(@RequestBody RevenueDateRequest revenueDateRequest) {
        return ResponseEntity.ok(dashboardService.getTopSellingProducts(revenueDateRequest));
    }

    @PostMapping("revenue-category-all")
    public ResponseEntity<RevenueCategoryResponse> GetAllRevenueCategory(){
        RevenueCategoryResponse revenueCategoryResponse = dashboardService.RevenueCategory();
        return ResponseEntity.ok(revenueCategoryResponse);
    }

    @PostMapping("top-selling-products-all")
    public ResponseEntity<RevenueProductResponse> getTopSellingProducts() {
        return ResponseEntity.ok(dashboardService.getTopSellingProducts());
    }

}
