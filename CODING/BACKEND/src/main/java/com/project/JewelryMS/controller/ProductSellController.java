package com.project.JewelryMS.controller;


import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.ProductSell.*;
import com.project.JewelryMS.model.Promotion.AssignPromotionRequest;
import com.project.JewelryMS.service.ProductSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/productSell")
//@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class ProductSellController {

    @Autowired
    ProductSellService productSellService;

    // Create a new ProductSell
    @PostMapping

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<ProductSellResponse> createProductSell(@ModelAttribute CreateProductSellRequest createProductSellRequest) {
        ProductSellResponse createdProduct = productSellService.createProductSell(createProductSellRequest);
        return ResponseEntity.ok(createdProduct);
    }

    // Get all ProductSells
    @GetMapping
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")

    public ResponseEntity<List<ProductSellResponse>> readAllProductSell() {
        List<ProductSellResponse> allProductSell = productSellService.getAllProductSellResponses();
        return ResponseEntity.ok(allProductSell);
    }

    //Get all Active(In-Stock) Product Sell
    @GetMapping("active")
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")

    public ResponseEntity<List<ProductSellResponse>> readAllActiveProductSell() {
        List<ProductSellResponse> allProductSell = productSellService.getAllActiveProductSellResponses();
        return ResponseEntity.ok(allProductSell);
    }

    // Get a ProductSell by ID
    @GetMapping("/{id}")

   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<ProductSellResponse> getProductSellById(@PathVariable long id) {
        ProductSellResponse response = productSellService.getProductSellById2(id);
        return ResponseEntity.ok(response);
    }

    // Update a ProductSell by ID
    @PutMapping("/{id}")

   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ProductSellResponse> updateProductSell(@PathVariable long id, @ModelAttribute ProductSellRequest productSellRequest) {
        ProductSellResponse updatedProduct = productSellService.updateProductSell(id, productSellRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a ProductSell by ID
    @DeleteMapping("/{id}")

   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> deleteProductSell(@PathVariable long id) {
        productSellService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // Adjust pricing ratio
    @PostMapping("/adjust-ratio/{ratio}")

  //  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Float> adjustRatio(@PathVariable Float ratio) {
        productSellService.updatePricingRatioPS(ratio);
        return ResponseEntity.ok(ratio);
    }

    // Add promotions to a ProductSell
    @PostMapping("/promotions")

   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> addPromotionsToProductSell(@RequestBody AddPromotionsRequest request) {
        ProductSell updatedProductSell = productSellService.addPromotionsToProductSell(request);
        return ResponseEntity.ok("Add Successfully");
    }

    // Remove promotions from a ProductSell
    @DeleteMapping("/promotions")

   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> removePromotionsFromProductSell(@RequestBody RemovePromotionRequest request) {
        ProductSell updatedProductSell = productSellService.removePromotionsFromProductSell(request);
        return ResponseEntity.ok("Remove Successfully");
    }

    @PostMapping("/assign-promotion-to-various-products")

   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> assignPromotionToProductSells(@RequestBody AssignPromotionRequest request) {
        productSellService.assignPromotionToProductSells(request);
        return ResponseEntity.ok("Promotion assigned to product sells successfully");
    }

    @DeleteMapping("/remove")

   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> removePromotionFromProductSells(@RequestBody AssignPromotionRequest request) {
        productSellService.removePromotionFromProductSells(request);
        return ResponseEntity.ok("Promotion removed from product sells successfully");
    }

    @DeleteMapping("/remove-all-promotion-from-product")

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> removeAllPromotionsFromProductSells(@RequestBody List<Long> productSellIds) {
        productSellService.removeAllPromotionsFromProductSells(productSellIds);
        return ResponseEntity.ok("All promotions removed from product sells successfully");
    }

    // Get product by guarantee coverage, policyType and warrantyPeriodMonth
    @GetMapping("/search-product-by-guarantee")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeProductSellResponse>> searchProductByGuarantee(@RequestParam String search) {
        List<GuaranteeProductSellResponse> guaranteeProductSellResponses = productSellService.readProductByGuaranteeSearch(search);
        return ResponseEntity.ok(guaranteeProductSellResponses);
    }

    @GetMapping("/get-by-code")

   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<ProductSellResponse> getProductByCode(@RequestParam String productCode) {
        ProductSellResponse productSellResponse = productSellService.findByProductCode(productCode);
        return ResponseEntity.ok(productSellResponse);
    }
}
