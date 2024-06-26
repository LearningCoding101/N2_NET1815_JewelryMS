package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePBOrderRequest {
    private String paymentType;
    private Float totalAmount;
    private Integer status;
    private Integer staff_ID;
}
