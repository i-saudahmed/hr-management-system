package com.saud.service;

import com.saud.dto.request.CreateEmployeeRequest;
import com.saud.dto.response.EmployeeResponse;
import com.saud.entity.Department;
import com.saud.entity.Employee;
import com.saud.enums.Role;
import com.saud.repository.DepartmentRepository;
import com.saud.repository.EmployeeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.jspecify.annotations.NonNull;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class EmployeeService {

    @Inject
    EmployeeRepository repository;
    @Inject
    DepartmentRepository departmentRepository;

    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {

        Employee employee = repository.findByEmail(request.getEmail());
        System.out.println(employee);

        if (employee != null) {
            throw new IllegalArgumentException("Email Already Exists");
        }

        Department department = departmentRepository
                .findByIdOptional(request.getDepartmentId())
                .orElseThrow(() ->
                        new NotFoundException("Department not found"));
        System.out.println(department);


        Employee manager = null;

        if (request.getManagerId() != null) {
            manager = repository.findByIdOptional(request.getManagerId()).orElseThrow(() ->
                    new NotFoundException("Manager Not Found"));

            if (manager.getRole() != Role.MANAGER) {
                throw new IllegalArgumentException(
                        "Selected employee is not a manager");
            }

        }

        Employee createEmployee = getEmployee(request, department, manager);

//        String password = UUID.randomUUID().toString().substring(0, 8);

        String characters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String password = RandomStringUtils.secure().next(8, characters);
        System.out.println(password);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        createEmployee.setPassword(hashedPassword);

        repository.persist(createEmployee);

        return EmployeeResponse.builder()
                .id(createEmployee.getId())
                .firstName(createEmployee.getFirstName())
                .lastName(createEmployee.getLastName())
                .email(createEmployee.getEmail())
                .role(createEmployee.getRole())
                .status(createEmployee.getStatus())
                .employmentType(createEmployee.getEmploymentType())
                .designation(createEmployee.getDesignation())
                .joinDate(createEmployee.getJoinDate())
                .departmentName(createEmployee.getDepartment() != null ? createEmployee.getDepartment().getName() : null)
                .departmentId(createEmployee.getDepartment() != null ? createEmployee.getDepartment().getId() : null)
                .managerId(
                        createEmployee.getManager() != null ?
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
