package com.project.JewelryMS.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private OrderDetail orderDetail;

    private float amount;
    private String reason;
    private Date refundDate;
    private Integer refundedQuantity;

}