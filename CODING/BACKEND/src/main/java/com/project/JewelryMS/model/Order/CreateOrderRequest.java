package com.project.JewelryMS.model.Order;

import lombok.Data;

import java.util.Date;

@Data
public class CreateOrderRequest {
    private String paymentType;
    private Float totalAmount;
    private Integer status;
    private Long customer_ID;
}
