package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("SELECT SUM(po.totalAmount) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId")
    Double getTotalRevenueByStaff(@Param("staffId") long staffId);

    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId")
    long getSalesCountByStaff(@Param("staffId") long staffId);


    @Query("SELECT SUM(po.totalAmount) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId AND po.purchaseDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueByStaffAndDateRange(@Param("staffId") long staffId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId AND po.purchaseDate BETWEEN :startDate AND :endDate")
    long getSalesCountByStaffAndDateRange(@Param("staffId") long staffId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
