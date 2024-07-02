package com.project.JewelryMS.model.Customer;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerResponse {
    private long PK_CustomerID;
    private String email;
    private String phoneNumber;
    private String gender;
    private Date createDate;
    private int pointAmount;
    private boolean status;
}
