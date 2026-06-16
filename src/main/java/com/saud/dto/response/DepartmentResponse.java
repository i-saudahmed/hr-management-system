package com.saud.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {

    private Long id;
    private String name;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

