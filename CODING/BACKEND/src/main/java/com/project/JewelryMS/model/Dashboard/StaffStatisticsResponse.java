package com.project.JewelryMS.model.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffStatisticsResponse {
    private long staffId;
    private String email;
    private long customerSignUps;
    private double revenueGenerated;
    private long salesCount;
    private long shiftsCount;
}
