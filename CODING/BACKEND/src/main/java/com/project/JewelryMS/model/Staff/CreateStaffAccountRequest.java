package com.project.JewelryMS.model.Staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStaffAccountRequest {
    private String phoneNumber;
    private Float salary;
    private String email;
    private String username;
    private String password;
    private String accountName;
}
