package com.project.JewelryMS.model.Refund;


import lombok.Builder;
import lombok.Data;


import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
public class RefundResponse {
    private Long refundId;
    private float amount;
    private String reason;
    private Date refundDate;
    private Integer refundedQuantity;

    private Long orderDetailId;
    private Integer orderDetailQuantity;
    private Integer orderDetailRefundedQuantity;
    private Timestamp guaranteeEndDate;

    private Long orderId;
    private Date orderDate;
    private Integer orderStatus;
    private float orderTotalAmount;
    private String paymentType;

    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerLoyaltyRank;

    private Long productId;
    private String productName;
    private String productCode;
    private float productCost;
}
