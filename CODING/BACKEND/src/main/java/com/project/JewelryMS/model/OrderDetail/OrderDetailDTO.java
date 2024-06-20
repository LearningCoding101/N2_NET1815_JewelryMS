package com.project.JewelryMS.model.OrderDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Long orderDetailId;
    private int quantity;
    private Long orderId;
    private Timestamp guaranteeEndDate;
    private String coverage;
    private String policyType;
    private Integer warrantyPeriodMonth;
    private boolean guaranteeStatus;
    private float carat;
    private Long categoryId;
    private int chi;
    private float cost;
    private String pDescription;
    private String gemstoneType;
    private String image;
    private String metalType;
    private String pName;
    private String productCode;
    private boolean pStatus;
    private String manufacturer;
    private Float manufactureCost;
    private Long productID;
}
