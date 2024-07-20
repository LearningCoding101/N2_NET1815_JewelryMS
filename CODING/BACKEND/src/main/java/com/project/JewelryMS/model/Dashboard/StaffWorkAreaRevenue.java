package com.project.JewelryMS.model.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffWorkAreaRevenue {
    Integer staffID;
    String staffName;
    Float revenueAmount;
}
