package com.saud.service;

import com.saud.dto.request.leave.ApplyLeaveRequest;
import com.saud.dto.request.leave.RejectLeaveRequest;
import com.saud.dto.response.leave.LeaveBalanceResponse;
import com.saud.dto.response.leave.LeaveResponse;
import com.saud.dto.response.leave.MyLeaveOverviewResponse;
import com.saud.entity.Employee;
import com.saud.entity.Leave;
import com.saud.entity.LeaveBalance;
import com.saud.enums.LeaveStatus;
import com.saud.enums.LeaveType;
import com.saud.repository.EmployeeRepository;
import com.saud.repository.LeaveBalanceRepository;
import com.saud.repository.LeaveRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ContextException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import mapper.LeaveMapper;
import org.jboss.logmanager.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationScoped
public class LeaveService {

   @Inject
   LeaveRepository leaveRepository ;
   @Inject
   EmployeeRepository employeeRepository ;
   @Inject
    LeaveMapper leaveMapper ;

    @Inject
    LeaveBalanceRepository leaveBalanceRepository;

    // APPLY FOR LEAVE

    @Transactional
    public LeaveResponse appyLeave(Long employeeId , ApplyLeaveRequest request){

        // fetch the employee
        Employee employee = employeeRepository.findByIdOptional(employeeId).orElseThrow(() -> new RuntimeException("Employee not found "));
        System.out.println(employee);

        // 2. Validate dates — end date cannot be before start date
        if (request.getEndDate().isBefore(request.getStartDate())) {
                throw new IllegalArgumentException("End date cannot be before start date");
            }

        // 3. Calculate total days
        int totalDays = (int) ChronoUnit.DAYS.between(
                request.getStartDate() , request.getEndDate()) + 1 ;

        // 4. Check balance — only for non-unpaid leave types
        if (request.getLeaveType() != LeaveType.UNPAID) {
            int currentYear = LocalDate.now().getYear();


            LeaveBalance balance = leaveBalanceRepository.findByEmployeeAndTypeAndYear(employeeId , request.getLeaveType() , currentYear)
                    .orElseThrow(()-> new RuntimeException("No leave balance for"+ request.getLeaveType()));

            if (balance.getRemaining() < totalDays) {
                throw new ContextException(
                        "Insufficient balance. Requested: " + totalDays +
                                " days, Remaining: " + balance.getRemaining() + " days");
            }
        }

        Leave leave = new Leave() ;

        leave.setEmployee(employee);
        leave.setLeaveType(request.getLeaveType());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setTotalDays(totalDays);
        leave.setReason(request.getReason());
        leave.setStatus(LeaveStatus.PENDING_MANAGER_APPROVAL);

        leaveRepository.persist(leave);

        return  leaveMapper.toResponse(leave);


    }

    @Transactional
    public LeaveResponse approveByManager(Long leaveId, Long managerId){

        Leave leave = leaveRepository.findByIdOptional(leaveId).orElseThrow(()-> new NotFoundException("Leave Not Found"));

        if (leave.getStatus() != LeaveStatus.PENDING_MANAGER_APPROVAL) {
            throw new IllegalArgumentException(
                    "Leave is not pending manager approval. Current status: " + leave.getStatus());
        }

        Employee leaveEmployee = leave.getEmployee() ;
        if (leaveEmployee.getManager() == null || !leaveEmployee.getManager().getId().equals(managerId)) {
            throw new RuntimeException("You are not the manager of this employee");
        }

        Employee manager = employeeRepository.findByIdOptional(managerId).orElseThrow(()-> new RuntimeException("Manager not found"));

        leave.setStatus(LeaveStatus.PENDING_HR_APPROVAL);
        leave.setManagerApprovedBy(manager);
        leave.setManagerApprovedAt(LocalDateTime.now());

        leaveRepository.persist(leave);

        // TODO: send email to HR notifying them a leave needs their approval

        return leaveMapper.toResponse(leave);
    }

    // HR APPROVES — final approval, deduct balance


    @Transactional
    public LeaveResponse approveByHr(Long leaveId, Long hrId) {

        Leave leave = leaveRepository.findByIdOptional(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (leave.getStatus() != LeaveStatus.PENDING_HR_APPROVAL) {
            throw new IllegalArgumentException(
                    "Leave is not pending HR approval. Current status: " + leave.getStatus());
        }

        Employee hr = employeeRepository.findByIdOptional(hrId)
                .orElseThrow(() -> new RuntimeException("HR not found"));


        leave.setStatus(LeaveStatus.APPROVED);
        leave.setHrApprovedBy(hr);
        leave.setHrApprovedAt(LocalDateTime.now());

        leaveRepository.persist(leave);

        // 3. Deduct balance — ONLY here, not at manager approval stage

        if (leave.getLeaveType() != LeaveType.UNPAID){
                int currentYear = leave.getStartDate().getYear();
                LeaveBalance balance = leaveBalanceRepository.findByEmployeeAndTypeAndYear(leave.getEmployee().getId(), leave.getLeaveType() , currentYear)
                        .orElseThrow(()-> new RuntimeException("Balance not found"));

                balance.setUsed(balance.getUsed() + leave.getTotalDays());
                leaveBalanceRepository.persist(balance);
        }
        // TODO: send email to employee notifying them leave is approved
        return  leaveMapper.toResponse(leave);
    }

    // REJECT — works at both stages
    @Transactional
    public LeaveResponse reject(Long leaveId, Long rejectorId, RejectLeaveRequest request) {

        Leave leave = leaveRepository.findByIdOptional(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        // Can only reject if still pending at some stage
        if (leave.getStatus() != LeaveStatus.PENDING_MANAGER_APPROVAL &&
                leave.getStatus() != LeaveStatus.PENDING_HR_APPROVAL) {
            throw new IllegalArgumentException(
                    "Leave cannot be rejected. Current status: " + leave.getStatus());
        }

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setRejectionReason(request.getRejectionReason());

        leaveRepository.persist(leave);

        // TODO: send email to employee notifying them of rejection with reason

        return leaveMapper.toResponse(leave);
    }

    // GET MY LEAVES + BALANCE
    public MyLeaveOverviewResponse getMyLeaves(Long employeeId) {
        int currentYear = LocalDate.now().getYear();

        return MyLeaveOverviewResponse.builder()
                .balances(leaveMapper.toBalanceResponseList(
                        leaveBalanceRepository.findAllByEmployeeAndYear(employeeId, currentYear)))
                .leaveHistory(leaveMapper.toResponseList(
                        leaveRepository.findByEmployeeId(employeeId)))
                .build();
    }

    // GET TEAM LEAVES — manager sees their team
    public List<LeaveResponse> getTeamLeaves(Long managerId) {
        return leaveMapper.toResponseList(
                leaveRepository.findByManagerId(managerId));
    }

    // GET PENDING — HR sees all pending
    public List<LeaveResponse> getPendingLeaves() {
        return leaveMapper.toResponseList(
                leaveRepository.findPendingHrApproval());
    }
}
