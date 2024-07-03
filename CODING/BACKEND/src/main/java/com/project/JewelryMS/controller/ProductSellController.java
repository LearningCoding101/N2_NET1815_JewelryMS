package com.project.JewelryMS.controller;


import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.ProductSell.*;
import com.project.JewelryMS.service.ImageService;
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
        productSellService.updatePricingRatio(ratio);
        return ResponseEntity.ok(ratio);
    }

    // Add promotions to a ProductSell
    @PostMapping("/promotions")
    public ResponseEntity<ProductSell> addPromotionsToProductSell(@RequestBody AddPromotionsRequest request) {
        ProductSell updatedProductSell = productSellService.addPromotionsToProductSell(request);
        return ResponseEntity.ok(updatedProductSell);
    }

    // Remove promotions from a ProductSell
    @DeleteMapping("/promotions")
    public ResponseEntity<ProductSell> removePromotionsFromProductSell(@RequestBody RemovePromotionRequest request) {
        ProductSell updatedProductSell = productSellService.removePromotionsFromProductSell(request);
        return ResponseEntity.ok(updatedProductSell);
    }
}
