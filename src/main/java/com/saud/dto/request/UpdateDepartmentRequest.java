package com.saud.dto.request;

import jakarta.validation.constraints.Size;

public class UpdateDepartmentRequest {

    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;   // optional — only update if provided

    private Boolean status;  // optional — activate or deactivate
}
