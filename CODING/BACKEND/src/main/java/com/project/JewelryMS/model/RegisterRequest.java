package com.project.JewelryMS.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    String email;
    String aUsername;
    String aPassword;
    String accountName;
}
