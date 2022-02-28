package com.example.employees.controller;

import com.example.employees.exceptions.ErrorResponse;
import com.example.employees.model.EmployeeEntity;
import com.example.employees.representation.EmployeeCollectionDTO;
import com.example.employees.representation.EmployeeDTO;
import com.example.employees.service.EmployeeService;
import com.example.employees.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Autowired
    private ConvertUtil convertUtil;

    @GetMapping(value = "/employees")
    public ResponseEntity<?> getEmployees() {
        EmployeeCollectionDTO employees = convertUtil.convertToDTO(service.getAll());
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping(value = "/employeesByBirthday")
    public ResponseEntity<?> getEmployeesByBirthday() {
        EmployeeCollectionDTO employees = convertUtil.convertToDTO(service.getAllWithBirthdayInCurrentMonth());
        return ResponseEntity.ok().body(employees);
    }

    @PostMapping(value = "/employees")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeDTO employee) {
        EmployeeEntity entity = convertUtil.convertToEntity(employee);
        entity = service.create(entity);
        return new ResponseEntity<>(convertUtil.convertToDTO(entity), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        try {
            Long parsedId = Long.valueOf(id);
            service.delete(parsedId);
        } catch(NumberFormatException ignore) {
            return new ResponseEntity<>(new ErrorResponse("Bad Request",
                    Collections.singletonList("Employee IDs must be a number.")),
                    HttpStatus.BAD_REQUEST);
        } catch(EmptyResultDataAccessException ignore) {
            return new ResponseEntity<>(new ErrorResponse("Not Found",
                    Collections.singletonList("An employee with ID " + id + " does not exist.")),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
