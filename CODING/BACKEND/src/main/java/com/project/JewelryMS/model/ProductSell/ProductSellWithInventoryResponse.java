package com.project.JewelryMS.model.ProductSell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductSellWithInventoryResponse extends ProductSellResponse{
    private Integer quantity;
    private Integer reorderLevel;
    private Date lastRestocked;
}
