package com.project.JewelryMS.model.Shift;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CreateShiftRequest {
    private String endTime;
    private int register;
    private String shiftType;
    private String startTime;
    private String status;
    private String workArea;
}
