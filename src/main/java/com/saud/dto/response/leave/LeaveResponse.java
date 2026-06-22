package com.saud.dto.response.leave;

import com.saud.enums.LeaveStatus;
import com.saud.enums.LeaveType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveResponse {

    private Long id;

    private Long employeeId;
    private String employeeName;

    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays;
    private String reason;

    private LeaveStatus status;

    private String managerApprovedByName;   // null until manager acts
    private LocalDateTime managerApprovedAt;

    private String hrApprovedByName;        // null until HR acts
    private LocalDateTime hrApprovedAt;

    private String rejectionReason;         // null unless rejected

    private LocalDateTime createdAt;
}