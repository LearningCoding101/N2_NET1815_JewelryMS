package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.model.Dashboard.DiscountEffectivenessResponse;
import com.project.JewelryMS.model.OrderDetail.OrderDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
//    @Query("SELECT new com.project.JewelryMS.model.Dashboard.DiscountEffectivenessResponse(d.discountCode, COUNT(d.discountCode)) " +
//            "FROM OrderDetail d " +
//            "WHERE d.discountCode IS NOT NULL " +
//            "GROUP BY d.discountCode " +
//            "ORDER BY COUNT(d.discountCode) DESC")
//    List<DiscountEffectivenessResponse> findDiscountCodeEffectiveness();


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

    @Query("SELECT SUM(od.quantity) AS totalQuantity, SUM(od.quantity * ps.cost) AS totalRevenue " +
            "FROM OrderDetail od JOIN od.productSell ps JOIN od.purchaseOrder po " +
            "WHERE FUNCTION('DATE', po.purchaseDate) = :date")
    OrderDetailProjection findTotalQuantityAndRevenueOnDate(@Param("date") Date date);

    @Query("SELECT SUM(od.quantity) AS totalQuantity, SUM(od.quantity * ps.cost) AS totalRevenue " +
            "FROM OrderDetail od JOIN od.productSell ps JOIN od.purchaseOrder po " +
            "WHERE DATE_FORMAT(po.purchaseDate, '%Y-%m') = :month")
    OrderDetailProjection findTotalQuantityAndRevenueInMonth(@Param("month") String month);

    @Query("SELECT SUM(od.quantity) AS totalQuantity, SUM(od.quantity * ps.cost) AS totalRevenue " +
            "FROM OrderDetail od JOIN od.productSell ps JOIN od.purchaseOrder po " +
            "WHERE FUNCTION('YEAR', po.purchaseDate) = :year")
    OrderDetailProjection findTotalQuantityAndRevenueInYear(Year year);

    @Query("SELECT FUNCTION('DATE', po.purchaseDate) AS purchaseDate, SUM(od.quantity * ps.cost) AS totalRevenue " +
            "FROM OrderDetail od JOIN od.productSell ps JOIN od.purchaseOrder po " +
            "WHERE po.purchaseDate BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', po.purchaseDate)")
    List<DailyRevenueProjection> findDailyRevenueBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT FUNCTION('DATE', po.purchaseDate) AS purchaseDate, SUM(od.quantity * ps.cost) AS totalRevenue " +
            "FROM OrderDetail od JOIN od.productSell ps JOIN od.purchaseOrder po " +
            "WHERE po.purchaseDate BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', po.purchaseDate)")
    List<DailyRevenueProjection> findDailyRevenue(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
