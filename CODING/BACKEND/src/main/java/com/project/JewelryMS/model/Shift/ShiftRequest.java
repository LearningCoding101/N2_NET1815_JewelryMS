package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftRequest {
    private int shiftID;
    private String endTime;
    private String shiftType;
    private String startTime;
    private String status;
    private WorkAreaRequest workArea;
}
