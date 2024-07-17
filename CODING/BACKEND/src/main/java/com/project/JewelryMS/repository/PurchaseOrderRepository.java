package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Date;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

//    @Query("SELECT SUM(po.totalAmount) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId")
//    Double getTotalRevenueByStaff(@Param("staffId") long staffId);
//
//    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId")
//    long getSalesCountByStaff(@Param("staffId") long staffId);
//
//    @Query("SELECT SUM(po.totalAmount) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId AND po.purchaseDate BETWEEN :startDate AND :endDate")
//    Double getTotalRevenueByStaffAndDateRange(@Param("staffId") long staffId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//
//    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId AND po.purchaseDate BETWEEN :startDate AND :endDate")
//    long getSalesCountByStaffAndDateRange(@Param("staffId") long staffId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(po.totalAmount) FROM PurchaseOrder po WHERE po.staffAccount.account.email = :email")
    Double getTotalRevenueByStaffEmail(@Param("email") String email);

    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.staffAccount.account.email = :email")
    long getSalesCountByStaffEmail(@Param("email") String email);

    @Query("SELECT SUM(po.totalAmount) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId AND po.purchaseDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueByStaffAndDateRange(@Param("staffId") long staffId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(po.totalAmount) FROM PurchaseOrder po WHERE po.staffAccount.account.email = :email AND po.purchaseDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueByStaffEmailAndDateRange(
            @Param("email") String email,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.staffAccount.account.email = :email AND po.purchaseDate BETWEEN :startDate AND :endDate")
    long getSalesCountByStaffEmailAndDateRange(
            @Param("email") String email,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
