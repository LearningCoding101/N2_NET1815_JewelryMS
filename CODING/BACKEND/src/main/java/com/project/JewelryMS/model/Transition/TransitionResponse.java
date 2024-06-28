package com.project.JewelryMS.model.Transition;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.project.JewelryMS.enumClass.OrderTypeEnum;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransitionResponse {
    private Long orderID;
    private String paymentType;
    private Integer orderStatus;
    private Float totalAmount;
    private Date purchaseDate;
    @Enumerated(value = EnumType.STRING)
    private OrderTypeEnum OrderType;
    private Integer staffID;
    private String staffName;
    private Long customerID;
    private String cusName;
}
