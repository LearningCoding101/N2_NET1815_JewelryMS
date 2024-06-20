package com.project.JewelryMS.model.Profile;

import com.project.JewelryMS.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffProfileResponse {
    private RoleEnum role;
    private String email;
    private String username;
    private String accountName;
    private Date startDate;
    private String phone;
    private float salary;
    private int status;

}
