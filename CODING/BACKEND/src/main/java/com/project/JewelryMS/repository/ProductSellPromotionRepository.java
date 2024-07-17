package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.ProductSell_Promotion;
import com.project.JewelryMS.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSellPromotionRepository extends JpaRepository<ProductSell_Promotion, Long> {
    List<ProductSell_Promotion> findByProductSell(ProductSell productSell);
    Optional<ProductSell_Promotion> findByProductSellAndPromotion(ProductSell productSell, Promotion promotion);

}
