package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder, Long> {
    @Query("SELECT o FROM PurchaseOrder o WHERE o.purchaseDate BETWEEN :startDate AND :endDate")
    List<PurchaseOrder> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
