package com.project.JewelryMS.model.Shift;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffAvailabilityDeserializer extends JsonDeserializer<Map<Integer, Map<DayOfWeek, List<String>>>> {
    @Override
    public Map<Integer, Map<DayOfWeek, List<String>>> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Map<String, Map<String, List<String>>> tempMap = p.readValueAs(Map.class);
        Map<Integer, Map<DayOfWeek, List<String>>> resultMap = new HashMap<>();
        for (Map.Entry<String, Map<String, List<String>>> entry : tempMap.entrySet()) {
            Integer staffId = Integer.valueOf(entry.getKey());
            Map<DayOfWeek, List<String>> dayOfWeekMap = new HashMap<>();
            for (Map.Entry<String, List<String>> innerEntry : entry.getValue().entrySet()) {
                DayOfWeek dayOfWeek = DayOfWeek.valueOf(innerEntry.getKey().toUpperCase());
                dayOfWeekMap.put(dayOfWeek, innerEntry.getValue());
            }
            resultMap.put(staffId, dayOfWeekMap);
        }
        return resultMap;
    }
}
