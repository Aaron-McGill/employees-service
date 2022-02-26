package com.example.employees.service;

import com.example.employees.model.EmployeeEntity;
import com.example.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public List<EmployeeEntity> getAllWithBirthdayInCurrentMonth() {
        return repository.findByBirthdayMonth();
    }

    public List<EmployeeEntity> getAll() {
        return repository.findAll();
    }

    public EmployeeEntity create(EmployeeEntity entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
