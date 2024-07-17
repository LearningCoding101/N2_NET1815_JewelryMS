package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.OrderBuyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderBuyDetailRepository extends JpaRepository<OrderBuyDetail, Long> {

    @Query("SELECT obd FROM OrderBuyDetail obd " +
            "JOIN obd.purchaseOrder po " +
            "WHERE obd.productBuy.PK_ProductBuyID = :productBuyId " +
            "AND po.status = :status")
    Optional<OrderBuyDetail> findByProductBuyIdAndPurchaseOrderStatus(@Param("productBuyId") Long productBuyId, @Param("status") int status);

}
