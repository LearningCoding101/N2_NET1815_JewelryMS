package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    @Query("SELECT r FROM Refund r WHERE r.orderDetail.PK_ODID = :orderId")
    Optional<Refund> findByOrderDetailId(Long orderId);

}