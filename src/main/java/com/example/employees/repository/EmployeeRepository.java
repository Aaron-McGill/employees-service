package com.example.employees.repository;

import com.example.employees.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    @Query("select e from EmployeeEntity e where month(e.birthday) = month(current_date)")
    List<EmployeeEntity> findByBirthdayMonth();
}
