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
public class InventoryResponse {
    private Long id;
    private Integer quantity;
    private Integer reorderLevel;
    private Date lastRestocked;
    private Long productId;
    private Float carat;
    private Long categoryId;
    private String categoryName;
    private Float chi;
    private Float cost;
    private String pDescription;
    private String gemstoneType;
    private String image;
    private String manufacturer;
    private Float manufactureCost;
    private String metalType;
    private String pName;
    private String productCode;
    private boolean pStatus;

}
