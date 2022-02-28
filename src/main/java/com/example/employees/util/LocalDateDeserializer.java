package com.example.employees.util;

import com.example.employees.exceptions.JsonDateParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(p.getText(), formatter);
        } catch(Throwable e) {
            throw new JsonDateParseException("An error occurred parsing the date: '" + p.getText() + ".' Dates are expected to be of the form: M/d/yyyy.");
        }
        return parsedDate;
    }
}
