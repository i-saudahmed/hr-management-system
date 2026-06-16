package com.saud.repository;

import com.saud.entity.Department;
import com.saud.entity.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<Department> {

    public Optional<Department> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public List<Department> findAllActive() {
        return find("status", true).list();
    }

//    public Department findByDepartmentId(Long departmentId) {
//        return find("department.id", departmentId).firstResultOptional().orElseThrow(() ->
//                new IllegalArgumentException("Department Not found"));
//    }
}
