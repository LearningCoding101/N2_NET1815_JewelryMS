package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderRefundDetail {
    Long orderID;
    Integer quantity;
}
