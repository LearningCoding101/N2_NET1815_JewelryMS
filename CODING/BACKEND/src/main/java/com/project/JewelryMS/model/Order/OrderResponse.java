package com.project.JewelryMS.model.Order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.entity.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"orderDetails.productSell.category"})
public class OrderResponse {
    private String paymentType;
    private Float totalAmount;
    private Date purchaseDate;
    private Integer status;
    private Long customer_ID;
    private Integer staff_ID;
    private Set<ProductResponse> productDetail = new HashSet<>();
}
