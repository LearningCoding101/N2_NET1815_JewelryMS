package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateOrderBuyDetailRequest {
    private Long productBuyID;
}
