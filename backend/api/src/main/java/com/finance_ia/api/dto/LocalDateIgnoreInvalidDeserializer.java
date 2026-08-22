package com.finance_ia.api.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateIgnoreInvalidDeserializer
        extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("d/M/yyyy");

    @Override
    public LocalDate deserialize(
            JsonParser parser,
            DeserializationContext context
    ) throws IOException {

        String value = parser.getText();

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(
                    value.trim(),
                    FORMATTER
            );
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}