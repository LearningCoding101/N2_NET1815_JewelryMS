package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Dashboard.DiscountEffectivenessResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByStatus(boolean b);
    List<Promotion> findByCode(String n);
    @Query("SELECT p FROM Promotion p")
    List<Promotion> findAllPromotion();

    @Query("SELECT ps.productID FROM ProductSell ps JOIN ps.promotion p WHERE p.PK_promotionID = :promotionID")
    List<Long> findProductSellIdsByPromotionId(@Param("promotionID") Long promotionID);



}
