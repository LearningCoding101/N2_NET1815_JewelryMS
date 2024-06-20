package com.project.JewelryMS.model.Performance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceResponse {
    private long PK_performanceID;

    //Some Staff details
    private int staffID;

    //Some user account details
//    String aUsername;


    //other performance details
    private String date;
    private int salesMade;
    private double revenueGenerated;
    private int customerSignUp;
    private boolean status;
}
