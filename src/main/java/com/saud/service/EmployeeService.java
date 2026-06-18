package com.saud.service;

import com.saud.dto.pagination.PaginatedResponse;
import com.saud.dto.request.CreateEmployeeRequest;
import com.saud.dto.request.UpdateEmployeeRequest;
import com.saud.dto.response.EmployeeResponse;
import com.saud.entity.Department;
import com.saud.entity.Employee;
import com.saud.enums.EmployeeStatus;
import com.saud.enums.EmploymentType;
import com.saud.enums.Role;
import com.saud.repository.DepartmentRepository;
import com.saud.repository.EmployeeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.jspecify.annotations.NonNull;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@ApplicationScoped
public class EmployeeService {

    @Inject
    EmployeeRepository repository;
    @Inject
    DepartmentRepository departmentRepository;
    @Inject
    EmployeeRepository employeeRepository;

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

            if (manager.getRole() != Role.MANAGER) {
                throw new IllegalArgumentException(
                        "Selected employee is not a manager");
            }

        }

        if (request.getRole() == Role.MANAGER) {
            if (request.getManagerId() != null) {
                throw new ForbiddenException("Manager cant be assigned to manager");
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


    public PaginatedResponse<EmployeeResponse> getAllEmployees(Integer page, Integer size) {

        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        if (page < 0) {
            throw new IllegalArgumentException("Page must be greater than or equal to 0");  // ✅
        }
        List<EmployeeResponse> employeeResponseList = employeeRepository.findAll().page(page, size).list().stream().map(current ->
                EmployeeResponse.builder()
                        .id(current.getId())
                        .firstName(current.getFirstName())
                        .lastName(current.getLastName())
                        .email(current.getEmail())
                        .role(current.getRole())
                        .status(current.getStatus())
                        .employmentType(current.getEmploymentType())
                        .joinDate(current.getJoinDate())
                        .departmentId(
                                current.getDepartment() != null
                                        ? current.getDepartment().getId()
                                        : null
                        )
                        .departmentName(
                                current.getDepartment() != null
                                        ? current.getDepartment().getName()
                                        : null
                        )
                        .designation(current.getDesignation())
                        .managerId(
                                current.getManager() != null
                                        ? current.getManager().getId()
                                        : null
                        )
                        .managerName(
                                current.getManager() != null
                                        ? current.getManager().getFirstName() + " " + current.getManager().getLastName()
                                        : null
                        )
                        .createdAt(current.getCreatedAt())
                        .updatedAt(current.getUpdatedAt())
                        .build()
        ).toList();
        long totalElements = employeeRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PaginatedResponse<EmployeeResponse>(employeeResponseList, totalPages, totalElements, page, size, page == 0, page >= totalPages - 1);
    }

    public EmployeeResponse getEmployee(long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            throw new NotFoundException("Employee not found");
        }
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .role(employee.getRole())
                .status(employee.getStatus())
                .employmentType(employee.getEmploymentType())
                .joinDate(employee.getJoinDate())
                .departmentId(
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null
                )
                .departmentName(
                        employee.getDepartment() != null
                                ? employee.getDepartment().getName()
                                : null
                )
                .designation(employee.getDesignation())
                .managerId(
                        employee.getManager() != null
                                ? employee.getManager().getId()
                                : null
                )
                .managerName(
                        employee.getManager() != null
                                ? employee.getManager().getFirstName() + " " + employee.getManager().getLastName()
                                : null
                )
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteEmployee(long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee != null) {
            employeeRepository.deleteById(id);
        }
    }

    @Transactional
    public EmployeeResponse updateEmployee(long id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            throw new NotFoundException("Employee not found");
        }
        if (request.getEmail() != null && !request.getEmail().equals(employee.getEmail())) {

            Employee existingEmployee = repository.findByEmail(request.getEmail());
            if (existingEmployee != null) {
                throw new IllegalArgumentException("Email already exists");
            }
            employee.setEmail(request.getEmail());

            if (request.getFirstName() != null) {
                employee.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                employee.setLastName(request.getLastName());
            }

            if(request.getStatus() != null){
                employee.setStatus(EmployeeStatus.valueOf(request.getStatus()));
            }


            if(request.getEmploymentType() != null){
                employee.setEmploymentType(EmploymentType.valueOf(request.getEmploymentType()));
            }

            if(request.getEmployeeStatus() != null){
                employee.setE(EmploymentType.valueOf(request.getEmploymentType()));
            }

            if (request.getDesignation() != null) {
                employee.setDesignation(request.getDesignation());
            }

        }
    }


//    public EmployeeResponse updateEmployee(long id, UpdateEmployeeRequest request) {
//        if (request.getRole() != null) {
//            if (request.getRole() == Role.MANAGER && request.getManagerId() != null) {
//                throw new IllegalArgumentException("Manager can't be assigned to manager");
//            }
//            employee.setRole(request.getRole());
//        }
//
//        if (request.getEmploymentType() != null) {
//            employee.setEmploymentType(request.getEmploymentType());
//        }
//
//        if (request.getDesignation() != null) {
//            employee.setDesignation(request.getDesignation());
//        }
//
//        if (request.getDepartmentId() != null) {
//            Department department = departmentRepository
//                    .findByIdOptional(request.getDepartmentId())
//                    .orElseThrow(() ->
//                            new NotFoundException("Department not found"));
//            employee.setDepartment(department);
//        }
//
//        if (request.getManagerId() != null) {
//            Employee manager = repository.findByIdOptional(request.getManagerId()).orElseThrow(() ->
//                    new NotFoundException("Manager Not Found"));
//
//            if (manager.getRole() != Role.MANAGER) {
//                throw new IllegalArgumentException(
//                        "Selected employee is not a manager");
//            }
//            employee.setManager(manager);
//        } else if (request.isRemoveManager()) {
//            employee.setManager(null);
//        }
//
//        repository.persist(employee);
//
//        return getEmployee(id);
//    }


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
