package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.PurchaseOrder;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.WorkArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder, Long> {
    @Query("SELECT o FROM PurchaseOrder o WHERE o.purchaseDate BETWEEN :startDate AND :endDate AND o.status = 3")
    List<PurchaseOrder> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o.purchaseOrder.customer.PK_CustomerID, o.purchaseOrder.customer.cusName, COUNT(o.purchaseOrder.PK_OrderID), o.productSell.pName, COUNT(o.productSell.pName) " +
            "FROM OrderDetail o " +
            "GROUP BY o.purchaseOrder.customer.PK_CustomerID, o.purchaseOrder.customer.cusName, o.productSell.pName")
    List<Object[]> findCustomerPurchaseHistory();

    @Query("SELECT SUM(po.totalAmount) FROM PurchaseOrder po WHERE FUNCTION('YEAR', po.purchaseDate) = CAST(:year AS int) AND po.orderBuyDetails IS EMPTY")
    Float findTotalRevenueByYear(@Param("year") String year);

    @Query("SELECT SUM(od.quantity) FROM OrderDetail od WHERE FUNCTION('YEAR', od.purchaseOrder.purchaseDate) = CAST(:year AS int) AND od.purchaseOrder.orderBuyDetails IS EMPTY")
    Long findTotalQuantityByYear(@Param("year") String year);

    @Query("SELECT po FROM PurchaseOrder po JOIN FETCH po.orderDetails od ")
    List<PurchaseOrder> findAllPaidOrders();

    @Query("SELECT po FROM PurchaseOrder po LEFT JOIN FETCH po.customer LEFT JOIN FETCH po.staffAccount ")
    List<PurchaseOrder> findAllWithDetails();

    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 3")
    List<PurchaseOrder> findAllCompleteOrder();

    @Query("SELECT po FROM PurchaseOrder po WHERE po.customer.PK_CustomerID = :customerID")
    List<PurchaseOrder> findByCustomerID(@Param("customerID") Long customerID);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.staffAccount.workArea = :workArea OR po.staffAccountSale.workArea = :workArea")
    List<PurchaseOrder> findByWorkArea(WorkArea workArea);

    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.staffAccount.workArea = :workArea OR po.staffAccountSale.workArea = :workArea")
    Integer countByWorkArea(WorkArea workArea);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.staffAccount = :staffAccount OR po.staffAccountSale = :staffAccount")
    List<PurchaseOrder> findByStaffAccount(StaffAccount staffAccount);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.staffAccount.staffID IN :staffIDs AND YEAR(po.purchaseDate) = :year")
    List<PurchaseOrder> findByStaffIDsAndYear(@Param("staffIDs") List<Integer> staffIDs, @Param("year") int year);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.staffAccountSale.staffID IN :staffIDs AND YEAR(po.purchaseDate) = :year")
    List<PurchaseOrder> findByStaffSaleIDsAndYear(@Param("staffIDs") List<Integer> staffIDs, @Param("year") int year);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.staffAccount.staffID IN :staffIDs AND YEAR(po.purchaseDate) = :year AND MONTH(po.purchaseDate) = :month")
    List<PurchaseOrder> findByStaffIDsAndMonth(@Param("staffIDs") List<Integer> staffIDs, @Param("year") int year, @Param("month") int month);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.staffAccountSale.staffID IN :staffIDs AND YEAR(po.purchaseDate) = :year AND MONTH(po.purchaseDate) = :month")
    List<PurchaseOrder> findByStaffSaleIDsAndMonth(@Param("staffIDs") List<Integer> staffIDs, @Param("year") int year, @Param("month") int month);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.staffAccount.staffID IN :staffIDs AND YEAR(po.purchaseDate) = :year AND MONTH(po.purchaseDate) = :month AND DAY(po.purchaseDate) = :day")
    List<PurchaseOrder> findByStaffIDsAndDay(@Param("staffIDs") List<Integer> staffIDs, @Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.staffAccountSale.staffID IN :staffIDs AND YEAR(po.purchaseDate) = :year AND MONTH(po.purchaseDate) = :month AND DAY(po.purchaseDate) = :day")
    List<PurchaseOrder> findByStaffSaleIDsAndDay(@Param("staffIDs") List<Integer> staffIDs, @Param("year") int year, @Param("month") int month, @Param("day") int day);
}
