package com.project.JewelryMS.model.OrderDetail;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailGuarantee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int warrantyPeriodMonth;
    private double carat;
    private Long categoryId;
    private int chi;
    private double cost;
    private String pDescription;
    private String gemstoneType;
    private String image;
    private String metalType;
    private String pName;
    private String productCode;
    private int pStatus;
    private String manufacturer;
    private double manufactureCost;
}
