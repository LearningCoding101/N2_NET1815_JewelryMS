package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSell_Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_PPID")
    private Long PK_PPID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productsellID", referencedColumnName = "PK_productID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProductSell productSell;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotionID", referencedColumnName = "PK_promotionID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Promotion promotion;
}
