package com.project.JewelryMS.model.Dashboard.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPurchaseHistoryResponse {
    String cusName;
    Integer purchaseCount;
    String productTrend;
}
