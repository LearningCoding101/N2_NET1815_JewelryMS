package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.service.PromotionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/promotion")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class PromotionController {
    @Autowired
    PromotionService promotionService;

    @GetMapping("/product-sell-ids")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public List<PromotionResponse> getProductSellIdsByPromotionId() {
        return promotionService.ReadAllPromotionWithProductID();
    }

    // Create a new promotion
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Promotion> createPromotion(@RequestBody CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = promotionService.createPromotion(createPromotionRequest);
        return ResponseEntity.ok(promotion);
    }

    // Read all promotions
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PromotionResponse>> readAllPromotions() {
        List<PromotionResponse> promotionList = promotionService.readAllPromotion();
        return ResponseEntity.ok(promotionList);
    }

    // Read a promotion by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<PromotionResponse> readPromotionById(@PathVariable Long id) {
        PromotionResponse promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(promotion);
    }

    // Read promotions by code
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PromotionResponse>> readPromotionsByCode(@PathVariable String code) {
        List<PromotionResponse> promotionList = promotionService.getPromotionByCode(code);
        return ResponseEntity.ok(promotionList);
    }

    // Read all active promotions
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PromotionResponse>> readAllActivePromotions() {
        List<PromotionResponse> promotionList = promotionService.readAllActivePromotion();
        return ResponseEntity.ok(promotionList);
    }

    // Read promotions by date
    @GetMapping("/date")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PromotionResponse>> listPromotionsByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<PromotionResponse> promotions = promotionService.getPromotionsByDate(date);
        return ResponseEntity.ok(promotions);
    }

    // Delete a promotion by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deletePromotion(@PathVariable long id) {
        promotionService.deletePromotionById(id);
        return ResponseEntity.ok("Promotion details marked as inactive successfully");
    }

    // Update promotion details
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PromotionResponse>> updatePromotionDetails(@PathVariable long id, @RequestBody PromotionRequest promotionRequest) {
        promotionRequest.setPK_promotionID(id); // Set the ID from the path into the request object
        List<PromotionResponse> promotionResponse = promotionService.updatePromotionDetails(promotionRequest);
        return ResponseEntity.ok(promotionResponse);
    }
}
