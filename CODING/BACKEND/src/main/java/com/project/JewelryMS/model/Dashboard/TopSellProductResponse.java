package com.project.JewelryMS.model.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopSellProductResponse {
    String product_Name;
    Integer unitSold;
    Float revenueSold;
}
