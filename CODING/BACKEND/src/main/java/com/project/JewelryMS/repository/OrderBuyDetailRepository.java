package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.OrderBuyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderBuyDetailRepository extends JpaRepository<OrderBuyDetail, Long> {
}
