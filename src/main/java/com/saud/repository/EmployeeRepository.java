package com.saud.repository;

import com.saud.entity.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepository<Employee> {

    public Employee findByEmail(String email) {
        return find("email", email).firstResultOptional().orElseThrow(() -> new NotFoundException("Employee not found with email: " + email));
    }

    public List<Employee> findByDepartmentId(Long departmentId) {
        return find("department.id", departmentId).list();
    }

    public List<Employee> findByManagerId(Long managerId) {
        return find("manager.id", managerId).list();
    }
}
