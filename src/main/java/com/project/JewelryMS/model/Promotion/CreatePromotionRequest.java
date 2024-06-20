package com.project.JewelryMS.model.Promotion;

import lombok.Data;

import java.util.Date;

@Data
public class CreatePromotionRequest {
    String code;
    String description;
    String startDate;
    String endDate;
    int discount;
}
