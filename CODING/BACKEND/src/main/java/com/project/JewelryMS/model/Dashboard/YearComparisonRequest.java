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

}
