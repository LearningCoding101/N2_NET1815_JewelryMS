package com.project.JewelryMS.model;


import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import lombok.Data;

@Data
public class AccountResponse {
    String username;
    String token;
    RoleEnum role;
    Long id;
}
