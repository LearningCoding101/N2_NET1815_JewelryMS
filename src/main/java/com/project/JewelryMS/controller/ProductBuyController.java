package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.ProductBuy.CalculatePBRequest;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.service.ProductBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/productBuy")
@CrossOrigin(origins = "*")
public class ProductBuyController {
    @Autowired
    private ProductBuyService productBuyService;

    // Create a new product buy
//    @PostMapping
//    public ResponseEntity<CreateProductBuyResponse> createProductBuy(@ModelAttribute CreateProductBuyRequest request) {
//        CreateProductBuyResponse response = productBuyService.createProductBuy(request);
//        return ResponseEntity.ok(response);
//    }

    // Get all product buys
    @GetMapping
    public ResponseEntity<List<ProductBuyResponse>> getAllProductBuys() {
        List<ProductBuyResponse> productBuys = productBuyService.getAllProductBuys();
        return ResponseEntity.ok(productBuys);
    }

    // Get a product buy by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductBuyResponse> getProductBuyById(@PathVariable Long id) {
        ProductBuyResponse productBuy = productBuyService.getProductBuyById(id);
        return ResponseEntity.ok(productBuy);
    }

    // Delete a product buy by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductBuy(@PathVariable Long id) {
        String response = productBuyService.deleteProductBuy(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate-cost")
    public ResponseEntity<Float> calculateProductBuyCost(@RequestBody CalculatePBRequest createProductBuyRequest) {
        return ResponseEntity.ok(productBuyService.calculateProductBuyCost(createProductBuyRequest));
    }

}
