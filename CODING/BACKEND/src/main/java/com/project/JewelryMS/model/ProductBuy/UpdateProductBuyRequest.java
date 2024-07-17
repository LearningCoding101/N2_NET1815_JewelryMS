package com.project.JewelryMS.model.ProductBuy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductBuyRequest {
    Long categoryID;
    String pbName;
    String metalType;
    Float chi;
    String gemstoneType;
    Float carat;
    String image;
    Float cost;
}
