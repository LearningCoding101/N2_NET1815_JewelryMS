package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"productSell", "promotion"})

public class ProductSell_Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_PPID")
    private Long PK_PPID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productsellID", referencedColumnName = "PK_productID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference

    private ProductSell productSell;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotionID", referencedColumnName = "PK_promotionID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference

    private Promotion promotion;
}
