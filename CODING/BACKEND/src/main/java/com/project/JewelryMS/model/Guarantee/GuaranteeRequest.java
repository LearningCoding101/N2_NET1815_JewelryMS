package com.project.JewelryMS.model.Guarantee;

import lombok.Data;

import java.sql.Date;

@Data
public class GuaranteeRequest {
    long PK_guaranteeID;
    long FK_productID;
    String policyType;
    String coverage;
    Integer warrantyPeriod;
}