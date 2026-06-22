package mapper;

import com.saud.dto.response.leave.LeaveBalanceResponse;
import com.saud.dto.response.leave.LeaveResponse;
import com.saud.entity.Leave;
import com.saud.entity.LeaveBalance;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LeaveMapper {

    public LeaveResponse toResponse(Leave leave){
        return LeaveResponse.builder()
                .id(leave.getId())
                .employeeId(leave.getEmployee().getId())
                .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                .leaveType(leave.getLeaveType())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .totalDays(leave.getTotalDays())
                .reason(leave.getReason())
                .status(leave.getStatus())
                .managerApprovedByName(
                        leave.getManagerApprovedBy() != null
                                ? leave.getManagerApprovedBy().getFirstName() + " " + leave.getManagerApprovedBy().getLastName()
                                : null
                )
                .managerApprovedAt(leave.getManagerApprovedAt())
                .hrApprovedByName(
                        leave.getHrApprovedBy() != null
                                ? leave.getHrApprovedBy().getFirstName() + " " + leave.getHrApprovedBy().getLastName()
                                : null
                )
                .hrApprovedAt(leave.getHrApprovedAt())
                .rejectionReason(leave.getRejectionReason())
                .createdAt(leave.getCreatedAt())
                .build();
    }

    public List<LeaveResponse> toResponseList(List<Leave> leaves){
        return leaves.stream().map(this::toResponse).toList();
    }


    public LeaveBalanceResponse toBalanceResponse(LeaveBalance balance) {
        return LeaveBalanceResponse.builder()
                .leaveType(balance.getLeaveType())
                .year(balance.getYear())
                .totalAllotted(balance.getTotalAllotted())
                .used(balance.getUsed())
                .remaining(balance.getRemaining())
                .build();
    }

    public List<LeaveBalanceResponse> toBalanceResponseList(List<LeaveBalance> balances) {
        return balances.stream().map(this::toBalanceResponse).toList();
    }
}
