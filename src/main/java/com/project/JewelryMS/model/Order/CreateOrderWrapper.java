package com.project.JewelryMS.model.Order;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class CreateOrderWrapper {
    private CreateOrderRequest orderRequest;
    private List<CreateOrderDetailRequest> detailList;
    private String email;
}
