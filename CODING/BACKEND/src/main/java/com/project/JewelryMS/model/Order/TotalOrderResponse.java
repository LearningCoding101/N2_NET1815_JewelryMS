package com.project.JewelryMS.model.Order;

import lombok.Data;

@Data
public class TotalOrderResponse {
    private Float SubTotal;
    private Float Discount_Price;
    private Float Total;
}
