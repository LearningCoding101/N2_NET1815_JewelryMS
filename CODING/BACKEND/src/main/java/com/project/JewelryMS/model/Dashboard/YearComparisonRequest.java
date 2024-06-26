package com.project.JewelryMS.model.Dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearComparisonRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private String year1;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private String year2;

//    public Year getYear1() {
//        return Year.parse(year1);
//    }
//
//    public void setYear1(String year1) {
//        this.year1 = year1;
//    }
//
//    public Year getYear2() {
//        return Year.parse(year2);
//    }
//
//    public void setYear2(String year2) {
//        this.year2 = year2;
//    }
}
