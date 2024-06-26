package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Guarantee;
import com.project.JewelryMS.entity.ProductSell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuaranteeRepository extends JpaRepository<Guarantee, Long> {

    @Query("SELECT g FROM Guarantee g WHERE g.PK_guaranteeID = :PK_guaranteeID")
    Guarantee findByGuaranteeId(@Param("PK_guaranteeID") long PK_guaranteeID);

    @Query("SELECT g FROM Guarantee g WHERE g.status = :status")
    List<Guarantee> findByStatus(@Param("status") boolean status);

    @Query("SELECT g FROM Guarantee g WHERE g.productSell.productID = :productID")
    Guarantee findByProductId(@Param("productID") long productID);

    @Query("SELECT g FROM Guarantee g WHERE g.policyType LIKE %:policyType%")
    List<Guarantee> listAllGuaranteesByPolicyType(@Param("policyType") String policyType);

    @Query("SELECT g FROM Guarantee g WHERE g.coverage LIKE %:coverage%")
    List<Guarantee> listAllGuaranteesByCoverage(@Param("coverage") String coverage);

    @Query("SELECT g FROM Guarantee g")
    List<Guarantee> listAllGuarantees();

    Optional<Guarantee> findByProductSell(ProductSell productSell);
}