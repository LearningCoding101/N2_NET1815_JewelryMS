package com.project.JewelryMS.model.Customer;

import lombok.Data;

@Data
public class CreateCustomerRequest {
    private String cusName;
    private String email;
    private String phoneNumber;
    private String gender;
}