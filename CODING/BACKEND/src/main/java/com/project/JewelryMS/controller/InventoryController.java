package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Inventory;
import com.project.JewelryMS.model.ProductSell.InventoryResponse;
import com.project.JewelryMS.service.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")

public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        System.out.println("Reached");
        List<InventoryResponse> inventoryList = inventoryService.getAllInventoryWithProductSell();
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId) {
        InventoryResponse inventory = inventoryService.getInventoryForProductWithDetails(productId);
        return  ResponseEntity.ok(inventory);
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@RequestBody Inventory inventory) {
        InventoryResponse createdInventory = inventoryService.createInventory(inventory);
        return new ResponseEntity<>(createdInventory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        InventoryResponse updatedInventory = inventoryService.updateInventory(id, inventory);
        return  ResponseEntity.ok(updatedInventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return  ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<InventoryResponse> updateQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        InventoryResponse updatedInventory = inventoryService.updateQuantity(id, quantity);
        return ResponseEntity.ok(updatedInventory);
    }
}
