package com.project.JewelryMS.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER) // Change fetch type for testing
    @JoinColumn(name = "product_id")
    private ProductSell productSell;

    private Integer quantity;

    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Column(name = "last_restocked")
    private Date lastRestocked;
}
