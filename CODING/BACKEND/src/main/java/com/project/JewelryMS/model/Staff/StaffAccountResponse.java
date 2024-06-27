package com.project.JewelryMS.model.Staff;

import com.project.JewelryMS.enumClass.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
//import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffAccountResponse {
    private int staffID;
    private String phoneNumber;
    private float salary;
    private String startDate; // Changed to String for formatted date
    private String accountName;
    private RoleEnum role;
    private int status;
    private String email;
    private String username;
    private List<ShiftResponse> shift;

    // Getters and Setters

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShiftResponse {
        private int shiftID;
        private String endTime; // Changed to String for formatted date
        private int register;
        private String shiftType;
        private String startTime; // Changed to String for formatted date
        private String status;
        private String workArea;
    }
}

