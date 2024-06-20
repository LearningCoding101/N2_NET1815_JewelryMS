package com.project.JewelryMS.model.Order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"orderBuyDetails.productBuy.category"})
public class OrderBuyResponse {
    private String paymentType;
    private Float totalAmount;
    private Date purchaseDate;
    private Integer status;
    private Set<ProductBuyResponse> productBuyDetail = new HashSet<>();
}
