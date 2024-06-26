package com.project.JewelryMS.model.Dashboard.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoyalty {
    String email;
    String phoneNumber;
    Integer pointAmount;
    String rank;
}
