package com.project.JewelryMS.model.Manager;

import com.project.JewelryMS.enumClass.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerAccountResponse {
    int PK_userID;

    String email;

    String username;

    String accountName;

    String image;

    RoleEnum role;

    int status;

}

