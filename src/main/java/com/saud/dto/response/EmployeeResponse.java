package com.saud.dto.response;

import com.saud.enums.EmployeeStatus;
import com.saud.enums.EmploymentType;
import com.saud.enums.Role;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private EmployeeStatus status;
    private EmploymentType employmentType;
    private LocalDate joinDate;

    private Long departmentId;
    private String departmentName;

    private Long managerId;
    private String managerName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
