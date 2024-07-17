package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetWorkAreaRequest {
    private long id;
    private String workAreaID;
    private int register;
    private String description;
    private String status;
}
