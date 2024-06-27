package com.project.JewelryMS.model.Manager;

import com.project.JewelryMS.enumClass.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerAccountRequest {
    String email;
    String aUsername;
    String aPassword;
    String accountName;
    RoleEnum role;
}
