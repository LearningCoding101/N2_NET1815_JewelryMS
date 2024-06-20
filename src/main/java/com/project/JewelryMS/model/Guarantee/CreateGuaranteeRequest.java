package com.project.JewelryMS.model.Guarantee;

import lombok.Data;

import java.sql.Date;
@Data
public class CreateGuaranteeRequest {//For read methods, we pull all datas
    long FK_productID;
    String policyType;
    String coverage;
    Integer warrantyPeriod;
}