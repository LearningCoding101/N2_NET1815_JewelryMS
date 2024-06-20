package com.project.JewelryMS.model.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAdminResponse {
    String email;
    String username;
    String accountname;
}
