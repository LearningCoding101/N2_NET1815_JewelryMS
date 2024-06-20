package com.project.JewelryMS.model.Order;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConfirmCashPaymentRequest {
    private Long orderID;
    private Float amount;
    private Float total;


}
