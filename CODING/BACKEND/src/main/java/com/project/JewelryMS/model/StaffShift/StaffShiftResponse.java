package com.project.JewelryMS.model.StaffShift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffShiftResponse {
    private int shiftID;
    private String startTime;
    private String endTime;
    private String shiftType;
    private String status;
    private String workArea;
    private int register;
    private List<StaffResponse> staff;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StaffResponse {
        private int staffID;
        private String accountName;
        private String email;
        private String username;
    }
}

