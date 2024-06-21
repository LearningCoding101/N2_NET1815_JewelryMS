package com.project.JewelryMS.model.Dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class RevenueProductResponse {
    List<TopSellProductResponse> list;
}
