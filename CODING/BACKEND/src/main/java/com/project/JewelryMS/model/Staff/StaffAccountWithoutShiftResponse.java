package com.project.JewelryMS.model.Staff;
import com.project.JewelryMS.enumClass.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffAccountWithoutShiftResponse {
    private int staffID;
    private String phoneNumber;
    private float salary;
    private LocalDate startDate;
    private String accountName;
    private String image;
    private RoleEnum role;
    private int status;
    private String email;
    private String username;
}
