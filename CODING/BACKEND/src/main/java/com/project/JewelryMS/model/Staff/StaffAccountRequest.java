package com.project.JewelryMS.model.Staff;

import com.project.JewelryMS.enumClass.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffAccountRequest {


    private String phoneNumber;

    private float salary;

    private LocalDateTime startDate;

    private RoleEnum role;

    private String email;

    private String username;

    private String password;

    private String accountName;


}
