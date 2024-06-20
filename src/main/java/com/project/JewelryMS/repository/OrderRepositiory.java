package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepositiory extends JpaRepository<PurchaseOrder, Long> {

}
