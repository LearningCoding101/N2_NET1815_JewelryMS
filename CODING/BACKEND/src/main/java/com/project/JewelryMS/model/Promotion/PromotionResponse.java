package com.project.JewelryMS.model.Promotion;

import lombok.*;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResponse {
    long promotionID;
    String code;
    String description;
    String startDate;
    String endDate;
    boolean status;
    int discount;
    List<String> productSell_id;
}
