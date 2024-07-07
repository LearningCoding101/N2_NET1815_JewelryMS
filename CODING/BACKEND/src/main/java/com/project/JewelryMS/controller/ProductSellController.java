package com.project.JewelryMS.controller;


import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.ProductSell.*;
import com.project.JewelryMS.model.Promotion.AssignPromotionRequest;
import com.project.JewelryMS.service.ProductSellService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/productSell")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class ProductSellController {

    @Autowired
    ProductSellService productSellService;

    // Create a new ProductSell
    @PostMapping
    public ResponseEntity<ProductSellResponse> createProductSell(@ModelAttribute CreateProductSellRequest createProductSellRequest) {
        ProductSellResponse createdProduct = productSellService.createProductSell(createProductSellRequest);
        return ResponseEntity.ok(createdProduct);
    }

    // Get all ProductSells
    @GetMapping
    public ResponseEntity<List<ProductSellResponse>> readAllProductSell() {
        List<ProductSellResponse> allProductSell = productSellService.getAllProductSellResponses();
        return ResponseEntity.ok(allProductSell);
    }

    // Get a ProductSell by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductSellResponse> getProductSellById(@PathVariable long id) {
        ProductSellResponse response = productSellService.getProductSellById2(id);
        return ResponseEntity.ok(response);
    }

    // Update a ProductSell by ID
    @PutMapping("/{id}")
    public ResponseEntity<ProductSellResponse> updateProductSell(@PathVariable long id, @ModelAttribute ProductSellRequest productSellRequest) {
        ProductSellResponse updatedProduct = productSellService.updateProductSell(id, productSellRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a ProductSell by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductSell(@PathVariable long id) {
        productSellService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // Adjust pricing ratio
    @PostMapping("/adjust-ratio/{ratio}")
    public ResponseEntity<Float> adjustRatio(@PathVariable Float ratio) {
        productSellService.updatePricingRatioPS(ratio);
        return ResponseEntity.ok(ratio);
    }

    // Add promotions to a ProductSell
    @PostMapping("/promotions")
    public ResponseEntity<String> addPromotionsToProductSell(@RequestBody AddPromotionsRequest request) {
        ProductSell updatedProductSell = productSellService.addPromotionsToProductSell(request);
        return ResponseEntity.ok("Add Successfully");
    }

    // Remove promotions from a ProductSell
    @DeleteMapping("/promotions")
    public ResponseEntity<String> removePromotionsFromProductSell(@RequestBody RemovePromotionRequest request) {
        ProductSell updatedProductSell = productSellService.removePromotionsFromProductSell(request);
        return ResponseEntity.ok("Remove Successfully");
    }
    @PostMapping("/assign")
    public ResponseEntity<String> assignPromotionToProductSells(@RequestBody AssignPromotionRequest request) {
        productSellService.assignPromotionToProductSells(request);
        return ResponseEntity.ok("Promotion assigned to product sells successfully");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removePromotionFromProductSells(@RequestBody AssignPromotionRequest request) {
        productSellService.removePromotionFromProductSells(request);
        return ResponseEntity.ok("Promotion removed from product sells successfully");
    }

    @DeleteMapping("/remove-all-promotion-from-product")
    public ResponseEntity<String> removeAllPromotionsFromProductSells(@RequestBody List<Long> productSellIds) {
        productSellService.removeAllPromotionsFromProductSells(productSellIds);
        return ResponseEntity.ok("All promotions removed from product sells successfully");
    }
}
