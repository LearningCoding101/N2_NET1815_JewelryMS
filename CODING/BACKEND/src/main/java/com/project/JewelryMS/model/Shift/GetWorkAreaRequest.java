package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetWorkAreaRequest {
    private long PK_WorkAreaId;
    private String workAreaCode;
    private String description;
    private String status;
}
