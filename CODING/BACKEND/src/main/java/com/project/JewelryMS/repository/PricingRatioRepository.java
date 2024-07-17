package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.PricingRatio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingRatioRepository extends JpaRepository<PricingRatio, Long> {
}
