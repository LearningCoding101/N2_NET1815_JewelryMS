package com.project.JewelryMS.model.OrderDetail;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrderPromotionRequest {
    Long productSell_ID;
    Integer quantity;
    Long PromotionID;
}
