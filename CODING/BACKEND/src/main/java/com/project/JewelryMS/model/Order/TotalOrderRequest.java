package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalOrderRequest {
    private Long ProductSell_ID;
    private Integer Quantity;
    private Long Promotion_ID;
}
