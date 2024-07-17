package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkAreaRequest {
    private String workAreaID;
    private int register;
    private String description;
}

