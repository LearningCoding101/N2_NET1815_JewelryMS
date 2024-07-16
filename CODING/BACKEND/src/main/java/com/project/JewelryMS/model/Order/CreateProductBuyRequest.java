package com.project.JewelryMS.model.Order;

import com.project.JewelryMS.entity.ProductBuy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateProductBuyRequest {
    private String name;
    private Long category_id;
    private String metalType;
    private String gemstoneType;
    private String image;
    private Float metalWeight;
    private Float gemstoneWeight;
    private Float cost;
    public CreateProductBuyRequest(ProductBuy productBuy) {
        this.name = productBuy.getPbName();
        this.category_id = productBuy.getCategory().getId();
        this.metalType = productBuy.getMetalType();
        this.gemstoneType = productBuy.getGemstoneType();
        this.image = productBuy.getImage();
        this.metalWeight = productBuy.getChi();
        this.gemstoneWeight = productBuy.getCarat();
        this.cost = productBuy.getPbCost();
    }
}
