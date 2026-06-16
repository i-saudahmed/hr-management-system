package com.saud.service;

import com.saud.dto.request.CreateEmployeeRequest;
import com.saud.dto.response.DepartmentResponse;
import com.saud.dto.response.EmployeeResponse;
import com.saud.entity.Department;
import com.saud.entity.Employee;
import com.saud.repository.DepartmentRepository;
import com.saud.repository.EmployeeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

@ApplicationScoped
public class EmployeeService {

    @Inject
    EmployeeRepository repository;
    @Inject
    DepartmentRepository departmentRepository;

    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {

        Employee employee = repository.findByEmail(request.getEmail());

        if (employee != null) {
            throw new IllegalArgumentException("Email Already Exists");
        }

        Department department = departmentRepository
                .findByIdOptional(request.getDepartmentId())
                .orElseThrow(() ->
                        new NotFoundException("Department not found"));

        Employee manager = null;

        if (request.getManagerId() != null) {
            manager = repository.findByIdOptional(request.getManagerId()).orElseThrow(() ->
                    new NotFoundException("Manager Not Found"));
        }

        Employee createEmployee = getEmployee(request, department, manager);

        repository.persist(createEmployee);

        return EmployeeResponse.builder()
                .id(createEmployee.getId())
                .firstName(createEmployee.getFirstName())
                .lastName(createEmployee.getLastName())
                .email(createEmployee.getEmail())
                .role(createEmployee.getRole())
                .status(createEmployee.getStatus())
                .employmentType(createEmployee.getEmploymentType())
                .joinDate(createEmployee.getJoinDate())
                .departmentName(createEmployee.getDepartment().getName())
                .departmentId(createEmployee.getDepartment().getId())
                .managerId(
                        createEmployee.getManager().getId() != null ?
                                createEmployee.getManager().getId() : null)
                .managerName(
                        createEmployee.getManager() != null
                                ? createEmployee.getManager().getFirstName()
                                : null)
                .createdAt(createEmployee.getCreatedAt())
                .updatedAt(createEmployee.getUpdatedAt()).build();
    }

    private static @NonNull Employee getEmployee(CreateEmployeeRequest request, Department department, Employee manager) {
        Employee createEmployee = new Employee();
        createEmployee.setFirstName(request.getFirstName());
        createEmployee.setLastName(request.getLastName());
        createEmployee.setEmail(request.getEmail());
        createEmployee.setRole(request.getRole());
        createEmployee.setDesignation(request.getDesignation());
        createEmployee.setJoinDate(request.getJoinDate());
        createEmployee.setEmploymentType(request.getEmploymentType());
        createEmployee.setDepartment(department);
        createEmployee.setManager(manager);
        return createEmployee;
    }
}
