package com.project.JewelryMS.model.ProductSell;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.Promotion;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateProductSellRequest {
    private Float carat;
    private long category_id;
    private Integer chi;
    private String pdescription;
    private String gemstoneType;
    private MultipartFile image;
    private String manufacturer;
    private Float manufactureCost;
    private String metalType;
    private String pname;
    private String productCode;
}
