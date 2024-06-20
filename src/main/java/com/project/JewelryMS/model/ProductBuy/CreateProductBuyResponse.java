package com.project.JewelryMS.model.ProductBuy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductBuyResponse {
    private Long ProductBuyID;
    private String categoryName;
    private String pbName;
    private String metalType;
    private String gemstoneType;
    private String image;
    private Float cost;
}
