package com.project.JewelryMS.model.Manager;

import com.project.JewelryMS.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerAccountResponse {
    int PK_userID;

    String email;

    String aUsername;

    String accountName;

    RoleEnum role;

    int status;

}

