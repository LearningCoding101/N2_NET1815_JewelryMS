package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByProductSellProductID(Long productId);

}