package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateShiftRequest {
    private String endTime;
    private int register;
    private String shiftType;
    private String startTime;
    private String status;
    private String workArea;
}
