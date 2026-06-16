package com.saud.dto.request;

import com.saud.enums.EmploymentType;
import com.saud.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateEmployeeRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Role is required")
    private Role role;           // "HR_MANAGER", "MANAGER", "EMPLOYEE"

    @NotNull(message = "Department is required")
    private Long departmentId;     // just the ID, not the whole object

    private Long managerId;        // optional — not every employee has a manager

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotNull(message = "Join date is required")
    private LocalDate joinDate;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType; // "FULL_TIME", "PART_TIME", "CONTRACT"
}