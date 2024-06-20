package com.project.JewelryMS.model.ProductSell;

import lombok.Data;

import java.util.List;

@Data
public class RemovePromotionRequest {
    private long productSellId;
    private List<Long> promotionIds;
}
