package com.example.employees.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonDateParseException extends JsonProcessingException {

    public JsonDateParseException(String msg) {
        super(msg);
    }
}
