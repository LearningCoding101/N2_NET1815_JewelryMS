package com.project.JewelryMS.model.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateManager {
    private String email;
    private String username;
    private String accountName;
    String image;
}
