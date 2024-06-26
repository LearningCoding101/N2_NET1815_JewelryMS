package com.project.JewelryMS.model.Shift;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AssignStaffToMultipleDaysRequest {
    private List<Integer> staffIds;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> shiftTypes;
}
