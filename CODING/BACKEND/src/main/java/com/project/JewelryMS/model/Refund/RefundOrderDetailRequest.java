package com.project.JewelryMS.model.Refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundOrderDetailRequest {
    private Long orderDetailId;
    private String refundReason;
    private Integer quantityToRefund;
}
