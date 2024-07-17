package com.project.JewelryMS.model.Dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthComparisonRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private String month1;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private String month2;

//    public YearMonth getMonth1() {
//        return YearMonth.parse(month1);
//    }
//
//    public void setMonth1(String month1) {
//        this.month1 = month1;
//    }
//
//    public YearMonth getMonth2() {
//        return YearMonth.parse(month2);
//    }
//
//    public void setMonth2(String month2) {
//        this.month2 = month2;
//    }
}
