package com.saud.dto.request;

import com.saud.enums.EmployeeStatus;
import com.saud.enums.EmploymentType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateEmployeeRequest {

    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;      // optional — only update if provided

    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    private String status ;

    private String email;

    private Long departmentId;     // optional — reassign department

    private Long managerId;        // optional — reassign manager

    private EmploymentType employmentType;

    private EmployeeStatus employeeStatus ;

    private String designation;

}