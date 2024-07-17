package com.project.JewelryMS.model.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearComparisonResponse {
    private Map<String, Double> revenue;
    private Map<String, Long> quantity;
    private Map<String, Long> customerSignup;
}
