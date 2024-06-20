package com.project.JewelryMS.model.ProductBuy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculatePBRequest {
    private String metalType;
    private String gemstoneType;
    private Integer metalWeight;
    private Float gemstoneWeight;
}
