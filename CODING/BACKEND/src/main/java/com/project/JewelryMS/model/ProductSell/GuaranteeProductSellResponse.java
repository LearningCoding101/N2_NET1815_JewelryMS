package com.project.JewelryMS.model.ProductSell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuaranteeProductSellResponse {
    Long guaranteeID;
    String coverage;
    String policyType;
    Integer warrantyPeriodMonth;
    Boolean status;
    ProductSellResponse productSell;
}
