package com.project.JewelryMS.model.Performance;

import lombok.Data;

import java.util.Date;

@Data
public class PerformanceRequest {
    long PK_performanceID;

    private String date;
    private int salesMade;
    private double revenueGenerated;
    private int customerSignUp;

    //no need to request staff id in the update
//    private int staffID;
}
