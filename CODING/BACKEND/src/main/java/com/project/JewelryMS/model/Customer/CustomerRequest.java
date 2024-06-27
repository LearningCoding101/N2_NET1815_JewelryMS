package com.project.JewelryMS.model.Customer;

import lombok.Data;

@Data
public class CustomerRequest {//For read methods, we pull all datas
    long PK_CustomerID;
    String cusName;
    String phoneNumber;
    String email;
    String gender;
}

