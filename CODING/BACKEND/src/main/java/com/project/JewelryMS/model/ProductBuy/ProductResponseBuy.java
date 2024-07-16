package com.project.JewelryMS.model.ProductBuy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseBuy {
    private Long productBuyID;
    private String categoryName;
    private Long categoryID;
    private String pbName;
    private String metalType;
    private String gemstoneType;
    private Float cost;
    private String image;
}
