package com.saud.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyLeaveOverviewResponse {
    private List<LeaveBalanceResponse> balances;
    private List<LeaveResponse> leaveHistory;
}