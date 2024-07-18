package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"productSell", "order"})

public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PK_ODID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_ProductID", referencedColumnName = "PK_ProductID")
    private ProductSell productSell;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_OrderID", referencedColumnName = "PK_OrderID")
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PurchaseOrder purchaseOrder;

    private Integer quantity;
    @Column(name = "refunded_quantity")
    private Integer refundedQuantity = 0;
    @Column(name = "guaranteeEndDate")
    private Timestamp guaranteeEndDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_Promotion_ID", referencedColumnName = "PK_promotionID", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Promotion promotion;
}
