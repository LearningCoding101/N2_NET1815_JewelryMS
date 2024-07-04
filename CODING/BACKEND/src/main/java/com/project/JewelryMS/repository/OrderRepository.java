package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder, Long> {
    @Query("SELECT o FROM PurchaseOrder o WHERE o.purchaseDate BETWEEN :startDate AND :endDate")
    List<PurchaseOrder> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o.purchaseOrder.customer.PK_CustomerID, o.purchaseOrder.customer.cusName, COUNT(o.purchaseOrder.PK_OrderID), o.productSell.pName, COUNT(o.productSell.pName) " +
            "FROM OrderDetail o " +
            "GROUP BY o.purchaseOrder.customer.PK_CustomerID, o.purchaseOrder.customer.cusName, o.productSell.pName")
    List<Object[]> findCustomerPurchaseHistory();

    @Query("SELECT SUM(o.totalAmount) FROM PurchaseOrder o WHERE FUNCTION('YEAR', o.purchaseDate) = :year AND o.orderBuyDetails IS EMPTY")
    Double findTotalRevenueByYear(@Param("year") String year);

    @Query("SELECT SUM(od.quantity) FROM OrderDetail od WHERE FUNCTION('YEAR', od.purchaseOrder.purchaseDate) = :year AND od.purchaseOrder.orderBuyDetails IS EMPTY")
    Long findTotalQuantityByYear(@Param("year") String year);

    @Query("SELECT po FROM PurchaseOrder po JOIN FETCH po.orderDetails od ")
    List<PurchaseOrder> findAllPaidOrders();

    @Query("SELECT po FROM PurchaseOrder po LEFT JOIN FETCH po.customer LEFT JOIN FETCH po.staffAccount ")
    List<PurchaseOrder> findAllWithDetails();
}
