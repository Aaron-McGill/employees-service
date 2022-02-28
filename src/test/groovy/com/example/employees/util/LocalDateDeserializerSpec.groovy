package com.example.employees.util

import com.example.employees.exceptions.JsonDateParseException
import com.example.employees.representation.EmployeeDTO
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonBuilder;
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.time.format.DateTimeFormatter;

// This spec tests LocalDateDeserializer indirectly by calling ObjectMapper
class LocalDateDeserializerSpec extends Specification {

    ObjectMapper mapper = new ObjectMapper()
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy")

    @Unroll
    def "Test valid date: #date" () {
        given: "We have a JSON string with an employee request"
        String json = new JsonBuilder([
                firstName: "first",
                lastName: "last",
                birthday: date
        ]).toPrettyString()

        when: "Test deserialization into EmployeeDTO"
        EmployeeDTO dto = mapper.readValue(json, EmployeeDTO.class)

        then: "Deserialization is successful"
        verifyAll {
            dto.firstName == "first"
            dto.lastName == "last"
            dto.birthday == LocalDate.parse(date, formatter)
        }

        where:
        date            |_
        "01/01/2001"    |_
        "1/1/2001"      |_
    }

    @Unroll
    def "Test invalid date: #date" () {
        given: "We have a JSON string with an employee request"
        String json = new JsonBuilder([
                firstName: "first",
                lastName: "last",
                birthday: date
        ]).toPrettyString()

        when: "Test deserialization into EmployeeDTO"
        mapper.readValue(json, EmployeeDTO.class)

        then: "Deserialization fails with an exception"
        def error = thrown JsonMappingException
        error.getCause() instanceof JsonDateParseException

        where:
        date            |_
        "2001"          |_
        "1-1-2001"      |_
    }
}
