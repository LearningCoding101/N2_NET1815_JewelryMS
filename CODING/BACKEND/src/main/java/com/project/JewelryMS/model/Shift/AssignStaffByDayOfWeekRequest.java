package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssignStaffByDayOfWeekRequest {
    private Map<Integer, Map<DayOfWeek, List<String>>> staffAvailability;
    private LocalDate startDate;
    private LocalDate endDate;

}

