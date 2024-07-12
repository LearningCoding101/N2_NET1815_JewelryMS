package com.project.JewelryMS.model.Order;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ClaimOrderRequest {
    Long orderId;
    String userId;
}
