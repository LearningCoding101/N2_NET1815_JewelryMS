package com.project.JewelryMS.model.Shift;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssignStaffByDayOfWeekRequest {
    @JsonProperty("staffAvailabilityByDayOfWeek")

    private Map<Integer, Map<DayOfWeek, List<String>>> staffAvailability;

    private LocalDate startDate;
    private LocalDate endDate;
}


