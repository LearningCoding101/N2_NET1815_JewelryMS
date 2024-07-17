package com.project.JewelryMS.model.Order;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReleaseOrderRequest {
    Long orderId;
    String userId;
}
