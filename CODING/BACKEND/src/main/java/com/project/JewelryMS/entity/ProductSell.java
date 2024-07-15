package com.project.JewelryMS.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = "guarantee")
@ToString(exclude = {"orderDetails", "guarantee"})

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "category.productSell", "promotion.productSell"})
public class ProductSell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_productID")
    private long productID;

    @Column(name = "carat")
    private Float carat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_categoryID", referencedColumnName = "id")
//    @JsonBackReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    @OneToMany(mappedBy = "productSell", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("productSell")
    private List<ProductSell_Promotion> productSellPromotions;

    @Column(name = "chi")
    private Float chi;

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

    @OneToOne(mappedBy = "productSell")
    @JsonIgnoreProperties
    @JsonManagedReference

    Guarantee guarantee;
    @Override
    public int hashCode() {
        return Objects.hash(productID, carat, chi, cost, pDescription, gemstoneType, manufacturer,
                manufactureCost, metalType, pName, productCode, pStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSell that = (ProductSell) o;
        return productID == that.productID &&
                Float.compare(that.carat, carat) == 0 &&
                chi == that.chi &&
                Float.compare(that.cost, cost) == 0 &&
                pStatus == that.pStatus &&
                Objects.equals(pDescription, that.pDescription) &&
                Objects.equals(gemstoneType, that.gemstoneType) &&
                Objects.equals(manufacturer, that.manufacturer) &&
                Objects.equals(manufactureCost, that.manufactureCost) &&
                Objects.equals(metalType, that.metalType) &&
                Objects.equals(pName, that.pName) &&
                Objects.equals(productCode, that.productCode);
    }
}
