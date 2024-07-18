package com.project.JewelryMS.model.Profile;

import com.project.JewelryMS.enumClass.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffListResponse {
    private long id;
    private RoleEnum role;
    private String email;
    private String username;
    private String accountName;
    private String image;
    private int status;
}
