package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStaffSaleRequest {
    Long orderID;
    Integer staffSaleID;
}
