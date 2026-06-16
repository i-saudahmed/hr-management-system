package com.saud.dto.request;

import com.saud.enums.EmploymentType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateEmployeeRequest {

    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;      // optional — only update if provided

    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    private String email;

    private Long departmentId;     // optional — reassign department

    private Long managerId;        // optional — reassign manager

    private EmploymentType employmentType;

    private String designation;

    private LocalDate joinDate;
}