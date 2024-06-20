package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.ProductSell;

import com.project.JewelryMS.model.ProductSell.ProductSellResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSellRepository extends JpaRepository<ProductSell, Long> {
    @Query("SELECT ps FROM ProductSell ps " +
            "LEFT JOIN FETCH ps.category c " +
            "LEFT JOIN FETCH ps.promotion p WHERE ps.pStatus = true")
    List<ProductSell> findAllWithCategoryAndPromotion();

    @Query("SELECT p.PK_promotionID FROM Promotion p JOIN p.productSell ps WHERE ps.productID = :productSellId")
    List<Long> findPromotionIdsByProductSellId(@Param("productSellId") long productSellId);

    @Query("SELECT ps FROM ProductSell ps " +
            "LEFT JOIN FETCH ps.category c " +
            "LEFT JOIN FETCH ps.promotion p " +
            "WHERE ps.productID = :productSellId AND ps.pStatus = true")
    Optional<ProductSell> findByIdWithCategoryAndPromotion(@Param("productSellId") long productSellId);


}
