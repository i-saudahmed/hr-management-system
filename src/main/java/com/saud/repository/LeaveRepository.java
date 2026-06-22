package com.saud.repository;

import com.saud.entity.Leave;
import com.saud.enums.LeaveStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LeaveRepository implements PanacheRepository<Leave> {

    public List<Leave> findByEmployeeId(Long employeeId) {
        return find("employee.id", employeeId).list();
    }

    public List<Leave> findByManagerId(Long managerId) {
        return find("employee.manager.id", managerId).list();
    }

    public List<Leave> findByStatus(LeaveStatus status){
        return  find("status", status).list();
    }

    public List<Leave> findPendingHrApproval(){
        return find("status", LeaveStatus.PENDING_HR_APPROVAL).list();
    }

    public List<Leave> findPendingManagerApprovalForManager(Long managerId){
        return find("status = ?1 and employee.manager.id = ?2", LeaveStatus.PENDING_MANAGER_APPROVAL , managerId).list();
    }

}

