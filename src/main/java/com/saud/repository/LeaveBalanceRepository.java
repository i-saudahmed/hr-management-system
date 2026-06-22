package com.saud.repository;

import com.saud.entity.LeaveBalance;
import com.saud.enums.LeaveType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LeaveBalanceRepository implements PanacheRepository<LeaveBalance> {

    public Optional<LeaveBalance> findByEmployeeAndTypeAndYear(Long employeeId, LeaveType type , Integer year){
        return find("employee.id = ?1 and leaveType = ?2 and year = ?3", employeeId, type, year).firstResultOptional();
    }

    public List<LeaveBalance> findAllByEmployeeAndYear(Long employeeId, Integer year){
        return find("employee.id = ?1 and year = ?2", employeeId, year).list();
    }

}
