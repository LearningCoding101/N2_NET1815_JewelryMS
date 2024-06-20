package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftResponse {
    private int shiftID;
    private String endTime;
    private int register;
    private String shiftType;
    private String startTime;
    private String status;
    private String workArea;
}