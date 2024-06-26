package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBuyResponse {
    private Long ProductBuyID;
    private String categoryName;
    private String pbName;
    private String metalType;
    private String gemstoneType;
    private Float cost;
}
