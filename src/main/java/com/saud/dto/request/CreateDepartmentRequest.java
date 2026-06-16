package com.saud.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDepartmentRequest {

    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

}
