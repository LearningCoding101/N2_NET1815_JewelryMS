package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Inventory;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.ProductSell.InventoryResponse;
import com.project.JewelryMS.repository.InventoryRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductSellRepository productSellRepository;

    @Transactional
    public void initializeInventoryForAllProducts() {
        List<ProductSell> allProducts = productSellRepository.findAll();
        for (ProductSell product : allProducts) {
            if (product.getInventory() == null) {
                Inventory inventory = new Inventory();
                inventory.setProductSell(product);
                inventory.setQuantity(0); // Set a default quantity
                inventory.setReorderLevel(10); // Set a default reorder level
                inventory.setLastRestocked(new Date());
                inventoryRepository.save(inventory);
            }
        }
    }

    public Inventory getInventoryForProduct(Long productId) {
        return inventoryRepository.findByProductSellProductID(productId);
    }

    public void updateInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    public void checkAndNotifyLowStock(Inventory inventory) {
        if (inventory.getQuantity() <= inventory.getReorderLevel()) {
            // Implement notification logic (e.g., send email, create alert)
            System.out.println("Low stock alert for product: " + inventory.getProductSell().getPName());
        }
    }
    public List<InventoryResponse> getAllInventoryWithProductSell() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToInventoryResponse)
                .collect(Collectors.toList());
    }

    public InventoryResponse getInventoryForProductWithDetails(Long productId) {
        Inventory inventory = inventoryRepository.findByProductSellProductID(productId);
        return mapToInventoryResponse(inventory);
    }

    public InventoryResponse createInventory(Inventory inventory) {
        Inventory savedInventory = inventoryRepository.save(inventory);
        return mapToInventoryResponse(savedInventory);
    }

    public InventoryResponse updateInventory(Long id, Inventory inventory) {
        Inventory existingInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        existingInventory.setQuantity(inventory.getQuantity());
        existingInventory.setReorderLevel(inventory.getReorderLevel());
        existingInventory.setLastRestocked(inventory.getLastRestocked());

        Inventory updatedInventory = inventoryRepository.save(existingInventory);
        return mapToInventoryResponse(updatedInventory);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    public InventoryResponse updateQuantity(Long id, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setQuantity(quantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return mapToInventoryResponse(updatedInventory);
    }
    private InventoryResponse mapToInventoryResponse(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();

        // Map Inventory fields
        response.setId(inventory.getId());
        response.setQuantity(inventory.getQuantity());
        response.setReorderLevel(inventory.getReorderLevel());
        response.setLastRestocked(inventory.getLastRestocked());

        // Map ProductSell fields
        ProductSell productSell = inventory.getProductSell();
        if (productSell != null) {
            response.setProductId(productSell.getProductID());
            response.setCarat(productSell.getCarat());
            response.setChi(productSell.getChi());
            response.setCost(productSell.getCost());
            response.setPDescription(productSell.getPDescription());
            response.setGemstoneType(productSell.getGemstoneType());
            response.setImage(productSell.getImage());
            response.setManufacturer(productSell.getManufacturer());
            response.setManufactureCost(productSell.getManufactureCost());
            response.setMetalType(productSell.getMetalType());
            response.setPName(productSell.getPName());
            response.setProductCode(productSell.getProductCode());
            response.setPStatus(productSell.isPStatus());

            // Map Category fields
            if (productSell.getCategory() != null) {
                response.setCategoryId(productSell.getCategory().getId());
                response.setCategoryName(productSell.getCategory().getName());
            }
        }

        return response;
    }
}
