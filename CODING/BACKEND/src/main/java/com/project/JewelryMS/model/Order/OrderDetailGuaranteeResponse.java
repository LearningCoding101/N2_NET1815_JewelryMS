package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailGuaranteeResponse {
    Long orderDetailID;
    Integer quantity;
    Date guaranteeEndDate;
    Long productID;
    Float chi;
    Float carat;
    String pName;
    String pDescription;
    String image;
    String gemstoneType;
    String metalType;
    Float cost;
    String productCode;
    Boolean pStatus;
    String manufacturer;
    Float manufactureCost;
    Long guaranteeID;
    String coverage;
    String policyType;
    Boolean GuaranteeStatus;
    Integer warrantyPeriodMonth;
}
