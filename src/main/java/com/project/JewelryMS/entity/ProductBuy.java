package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="ProductBuy")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBuy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PK_ProductBuyID")
    private Long PK_ProductBuyID;

    @OneToMany(mappedBy = "productBuy")
    @JsonIgnoreProperties
    Set<OrderBuyDetail> orderBuyDetails = new HashSet<>();

    @Column(name="pbName")
    private String pbName;

    @Column(name="pbCost")
    private Float pbCost;

    @Column(name = "metalType")
    private String metalType;

    @Column(name = "gemstoneType")
    private String gemstoneType;

    @Column(name = "image")
    private String image;

    @Column(name= "chi")
    private Integer chi;

    @Column(name = "carat")
    private Float carat;

    @Column(name = "pbStatus")
    private boolean pbStatus;

    @Column(name = "paymentStatus")
    private boolean paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_categoryID", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;
}
