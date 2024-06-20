package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.model.OrderDetail.OrderDetailDTO;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {



    @Query("SELECT new com.project.JewelryMS.model.OrderDetail.OrderDetailDTO(od.PK_ODID, od.quantity, od.purchaseOrder.PK_OrderID, " +
            "od.guaranteeEndDate, g.coverage, g.policyType, g.warrantyPeriodMonth, g.status, ps.carat, " +
            "c.id, ps.chi, ps.cost, ps.pDescription, ps.gemstoneType, ps.image, ps.metalType, ps.pName, ps.productCode, " +
            "ps.pStatus, ps.manufacturer, ps.manufactureCost, ps.productID) " +
            "FROM OrderDetail od " +
            "JOIN od.productSell ps " +
            "JOIN ps.category c " +
            "JOIN Guarantee g ON g.productSell = ps " +
            "WHERE od.purchaseOrder.PK_OrderID = ?1")
    List<OrderDetailDTO> findOrderDetailsByOrderId(@Param("orderId") Long orderId);

}
