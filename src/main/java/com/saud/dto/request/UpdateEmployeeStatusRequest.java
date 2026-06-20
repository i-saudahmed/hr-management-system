package com.saud.dto.request;

import com.saud.enums.EmployeeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmployeeStatusRequest {

    @NotNull(message = "status is required")
    private EmployeeStatus status ;

}
