package com.project.JewelryMS.model.Promotion;

import lombok.Data;

import java.util.List;

@Data
public class AssignPromotionRequest {
    private long promotionId;
    private List<Long> productSellIds;
}
