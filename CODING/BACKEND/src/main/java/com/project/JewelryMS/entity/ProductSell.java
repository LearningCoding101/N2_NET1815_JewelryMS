package com.project.JewelryMS.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "category.productSell", "promotion.productSell"})
public class ProductSell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_productID")
    private long productID;

    @Column(name = "carat")
    private float carat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_categoryID", referencedColumnName = "id")
//    @JsonBackReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ProductSell_Promotion",
            joinColumns = @JoinColumn(name = "productsellID", referencedColumnName = "PK_productID"),
            inverseJoinColumns = @JoinColumn(name = "promotionID", referencedColumnName = "PK_promotionID")
    )
    @JsonIgnoreProperties("productSell")
    private List<Promotion> promotion ;

    @Column(name = "chi")
    private int chi;

    @Column(name = "cost")
    private float cost;

    @Column(name = "pDescription", length = 255)
    private String pDescription;

    @Column(name = "gemstoneType", length = 255)
    private String gemstoneType;

    @Lob
    @Column(name = "image")
    private String image;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "manufactureCost")
    private Float manufactureCost;

    @Column(name = "metalType", length = 255)
    private String metalType;

    @Column(name = "pName", length = 255)
    private String pName;

    @Column(name = "productCode", length = 255)
    private String productCode;

    @Column(name = "pStatus")
    private boolean pStatus;

    @OneToMany(mappedBy = "productSell")
    @JsonIgnoreProperties
    Set<OrderDetail> orderDetails;
}
