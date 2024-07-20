package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffWorkAreaResponse {
    private Integer staffID;
    private String accountName;
    private String workAreaCode;
}
