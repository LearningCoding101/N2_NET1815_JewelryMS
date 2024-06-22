package com.project.JewelryMS.model.Dashboard.Customer;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomerDemographicsResponse {
    List<CustomerDemographics> list;
}