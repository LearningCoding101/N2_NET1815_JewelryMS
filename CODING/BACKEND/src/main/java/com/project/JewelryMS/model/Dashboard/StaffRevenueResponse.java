package com.project.JewelryMS.model.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffRevenueResponse {
    private String staffName;
    private double totalRevenue;
}
