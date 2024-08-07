package com.project.JewelryMS.model.ProductSell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSellRequest {
    private Float carat;
    private long category_id;
    private Float chi;
    private Float cost;
    private String pdescription;
    private String gemstoneType;
    private MultipartFile image;
    private String manufacturer;
    private Float manufactureCost;
    private String metalType;
    private String pname;
    private String productCode;
}
