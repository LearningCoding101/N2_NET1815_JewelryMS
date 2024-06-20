package com.project.JewelryMS.model.ProductSell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPromotionsRequest {
    private long productSellId;
    private List<Long> promotionIds;
}
