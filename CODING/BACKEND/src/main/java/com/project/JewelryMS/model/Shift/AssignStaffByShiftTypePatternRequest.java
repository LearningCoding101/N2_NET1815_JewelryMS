package com.project.JewelryMS.model.Shift;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssignStaffByShiftTypePatternRequest {

    private Map<String, List<Integer>> staffShiftPatterns;

    public Map<String, List<Integer>> getStaffShiftPatterns() {
        return staffShiftPatterns;
    }

    public void setStaffShiftPatterns(Map<String, List<Integer>> staffShiftPatterns) {
        this.staffShiftPatterns = staffShiftPatterns;
    }
}


