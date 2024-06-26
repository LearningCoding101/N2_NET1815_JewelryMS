package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Guarantee")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "productSell.guarantee"})
public class Guarantee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long PK_guaranteeID;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_productID", referencedColumnName = "PK_productID")
    @JsonIgnoreProperties
    private ProductSell productSell;

    //For years, or months
    private String policyType;

    //For what product
    private String coverage;
    @Column(name="warrantyPeriodMonth")
    private Integer warrantyPeriodMonth;
    private boolean status = true;
}
