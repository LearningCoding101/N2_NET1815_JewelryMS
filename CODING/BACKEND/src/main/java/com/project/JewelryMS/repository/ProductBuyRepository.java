package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.ProductBuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBuyRepository extends JpaRepository<ProductBuy, Long> {

}
