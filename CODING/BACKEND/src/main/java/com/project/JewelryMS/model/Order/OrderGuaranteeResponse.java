package com.project.JewelryMS.model.Order;

import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.model.OrderDetail.OrderDetailGuarantee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderGuaranteeResponse {
    Long OrderID;
    String paymentType;
    Date purchaseDate;
    Integer status;
    Float totalAmount;
    Integer staffID;
    String staffName;
    List<OrderDetailGuaranteeResponse> orderDetails;

}
