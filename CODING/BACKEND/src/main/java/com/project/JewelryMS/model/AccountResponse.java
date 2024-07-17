package com.project.JewelryMS.model;


import com.project.JewelryMS.enumClass.RoleEnum;
import lombok.Data;

@Data
public class AccountResponse {
    String username;
    String token;
    RoleEnum role;
    Long id;
}
