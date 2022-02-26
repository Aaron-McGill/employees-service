package com.example.employees.representation;

import java.util.List;

public class EmployeeCollectionDTO {

    public EmployeeCollectionDTO(List<EmployeeDTO> items) {
        this.items = items;
    }

    private List<EmployeeDTO> items;

    public List<EmployeeDTO> getItems() {
        return items;
    }

    public void setItems(List<EmployeeDTO> items) {
        this.items = items;
    }
}
