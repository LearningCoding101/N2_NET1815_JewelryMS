package com.project.JewelryMS.model.Dashboard.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPurchaseHistoryResponse {
    String customerName;
    Long orderID;
    String paymentType;
    Date purchaseDate;
    Integer status;
    Float total;
    Integer staffID;
    String staffName;
}
