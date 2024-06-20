package com.project.JewelryMS.model.OrderDetail;

import com.project.JewelryMS.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculatePointsRequest {
    Float Total;
}
