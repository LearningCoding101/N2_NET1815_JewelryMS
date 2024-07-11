package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("SELECT SUM(po.totalAmount) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId")
    Double getTotalRevenueByStaff(@Param("staffId") long staffId);

    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.staffAccount.staffID = :staffId")
    long getSalesCountByStaff(@Param("staffId") long staffId);
}
