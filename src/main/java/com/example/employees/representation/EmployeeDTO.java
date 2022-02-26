package com.example.employees.representation;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@JsonInclude(Include.NON_NULL)
public class EmployeeDTO {

    private Long id;
    @NotEmpty(message = "A first name must be specified when adding a new employee.")
    private String firstName;
    @NotEmpty(message = "A last name must be specified when adding a new employee.")
    private String lastName;
    @Email(message = "The provided email address is not in a valid format.")
    private String email;
    private String phoneNumber;
    private Date birthday;

    public EmployeeDTO() {}

    public EmployeeDTO(String firstName, String lastName, String email,
                       String phoneNumber, Date birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
