package com.example.employees.util;

import com.example.employees.model.EmployeeEntity;
import com.example.employees.representation.EmployeeCollectionDTO;
import com.example.employees.representation.EmployeeDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvertUtil {

    @Autowired
    private ModelMapper modelMapper;

    public EmployeeCollectionDTO convertToDTO(List<EmployeeEntity> entities) {
        return new EmployeeCollectionDTO(entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }

    public EmployeeDTO convertToDTO(EmployeeEntity entity) {
        return modelMapper.map(entity, EmployeeDTO.class);
    }

    public EmployeeEntity convertToEntity(EmployeeDTO dto) {
        return modelMapper.map(dto, EmployeeEntity.class);
    }
}
