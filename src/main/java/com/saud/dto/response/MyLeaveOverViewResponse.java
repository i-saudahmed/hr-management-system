package com.saud.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyLeaveOverViewResponse
{
    private List<LeaveBalanceResponse> leaveBalances;
    private List<LeaveResponse> leaveHistory ;
}
