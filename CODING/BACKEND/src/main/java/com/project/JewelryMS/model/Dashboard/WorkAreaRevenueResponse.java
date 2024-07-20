package com.project.JewelryMS.model.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkAreaRevenueResponse{
    Long workAreaID;
    String workAreaCode;
    String description;
    String status;
    Float TotalRevenueAmount;
    Integer numberOrder;
    List<StaffWorkAreaRevenue> staffWorkAreaRevenues;
}
