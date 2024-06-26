package com.project.JewelryMS.model.ProductBuy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBuyResponse {
    private Long ProductBuyID;
    private String categoryName;
    private String pbName;
    private String metalType;
    private String gemstoneType;
    private Float cost;
}
