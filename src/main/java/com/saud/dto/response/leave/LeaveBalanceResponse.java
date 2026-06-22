package com.saud.dto.response.leave;

import com.saud.enums.LeaveType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceResponse {
    private LeaveType leaveType;
    private Integer year;
    private Integer totalAllotted;
    private Integer used;
    private Integer remaining;
}