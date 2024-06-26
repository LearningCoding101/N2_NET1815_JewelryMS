package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderBuyDetail implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long PK_OBDID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_OrderID", referencedColumnName = "PK_OrderID")
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_ProductBuyID", referencedColumnName = "PK_ProductBuyID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProductBuy productBuy;

}
