package com.project.JewelryMS.model;

import com.project.JewelryMS.entity.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponseGG extends Account {
    private String token;
}
