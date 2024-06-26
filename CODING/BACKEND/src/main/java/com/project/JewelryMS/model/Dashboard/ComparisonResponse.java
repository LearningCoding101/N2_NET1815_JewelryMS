package com.project.JewelryMS.model.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComparisonResponse {
    private long totalQuantityDifference;
    private float totalRevenueDifference;
    private long totalCustomerAccountsDifference;
}
