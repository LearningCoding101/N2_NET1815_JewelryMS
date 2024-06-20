package com.project.JewelryMS.model.OrderDetail;

import lombok.Data;

import java.util.List;
@Data
public class CalculateGuaranteeDateRequest {
    private List<Long> OrderDetail_ID;
}
