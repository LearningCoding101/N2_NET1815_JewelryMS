package com.project.JewelryMS.model.Shift;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public class StaffAvailabilitySerializer extends JsonSerializer<Map<Integer, Map<DayOfWeek, List<String>>>> {
    @Override
    public void serialize(Map<Integer, Map<DayOfWeek, List<String>>> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        for (Map.Entry<Integer, Map<DayOfWeek, List<String>>> entry : value.entrySet()) {
            gen.writeObjectFieldStart(String.valueOf(entry.getKey()));
            for (Map.Entry<DayOfWeek, List<String>> innerEntry : entry.getValue().entrySet()) {
                gen.writeObjectField(innerEntry.getKey().name().toLowerCase(), innerEntry.getValue());
            }
            gen.writeEndObject();
        }
        gen.writeEndObject();
    }
}
