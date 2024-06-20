package com.project.JewelryMS.model.Staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStaffAccountRequest {
    private String phoneNumber;
    private float salary;
    private Date startDate;
    private String email;
    private String username;
    private String password;
    private String accountName;
}
