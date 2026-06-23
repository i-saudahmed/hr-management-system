package com.saud.service;

import com.saud.dto.pagination.PaginatedResponse;
import com.saud.dto.request.CreateEmployeeRequest;
import com.saud.dto.request.UpdateEmployeeRequest;
import com.saud.dto.request.UpdateEmployeeStatusRequest;
import com.saud.dto.response.EmployeeResponse;
import com.saud.entity.Department;
import com.saud.entity.Employee;
import com.saud.enums.Role;
import com.saud.repository.DepartmentRepository;
import com.saud.repository.EmployeeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import mapper.EmployeeMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.logging.Logger;
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

    @Inject
    EmployeeMapper mapper ;

    private static final Logger LOG = Logger.getLogger(EmployeeService.class);


//    @Transactional
//    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
//
//        Employee employee = repository.findByEmail(request.getEmail());
//
//        if (employee != null) {
//            throw new IllegalArgumentException("Email Already Exists");
//        }
//
//        Department department = departmentRepository
//                .findByIdOptional(request.getDepartmentId())
//                .orElseThrow(() ->
//                        new NotFoundException("Department not found"));
//
//        Employee manager = null;
//
//        if (request.getManagerId() != null) {
//            manager = repository.findByIdOptional(request.getManagerId()).orElseThrow(() ->
//                    new NotFoundException("Manager Not Found"));
//
//            if (manager.getRole() != Role.MANAGER) {
//                throw new IllegalArgumentException(
//                        "Selected employee is not a manager");
//            }
//
//        }
//
//        if (request.getRole() == Role.MANAGER) {
//            if (request.getManagerId() != null) {
//                throw new ForbiddenException("Manager cant be assigned to manager");
//            }
//        }
//
//        Employee createEmployee = mapper.toEntity(request, department, manager);
//
////        String password = UUID.randomUUID().toString().substring(0, 8);
//
//        String characters =
//                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        String password = RandomStringUtils.secure().next(8, characters);
//        System.out.println(password + "password");
//        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//        createEmployee.setPassword(hashedPassword);
//
//        repository.persist(createEmployee);
//
//        return mapper.toResponse(createEmployee);
//    }

@Transactional
public EmployeeResponse createEmployee(CreateEmployeeRequest request) {


    LOG.infof("Creating employee with email: %s", request.getEmail());

    Employee employee = repository.findByEmail(request.getEmail());

    if (employee != null) {
        LOG.warnf("Employee creation failed. Email already exists: %s",
                request.getEmail());
        throw new IllegalArgumentException("Email Already Exists");
    }

    LOG.infof("Fetching department with id: %d",
            request.getDepartmentId());

    Department department = departmentRepository
            .findByIdOptional(request.getDepartmentId())
            .orElseThrow(() -> {
                LOG.warnf("Department not found. Id: %d",
                        request.getDepartmentId());
                return new NotFoundException("Department not found");
            });

    Employee manager = null;

    if (request.getManagerId() != null) {

        LOG.infof("Fetching manager with id: %d",
                request.getManagerId());

        manager = repository.findByIdOptional(request.getManagerId())
                .orElseThrow(() -> {
                    LOG.warnf("Manager not found. Id: %d",
                            request.getManagerId());
                    return new NotFoundException("Manager Not Found");
                });

        if (manager.getRole() != Role.MANAGER) {
            LOG.warnf("Employee %d is not a manager",
                    manager.getId());

            throw new IllegalArgumentException(
                    "Selected employee is not a manager");
        }
    }

    if (request.getRole() == Role.MANAGER) {
        if (request.getManagerId() != null) {

            LOG.warn("Attempted to assign a manager to another manager");

            throw new ForbiddenException(
                    "Manager cant be assigned to manager");
        }
    }

    LOG.info("Mapping request to Employee entity");

    Employee createEmployee =
            mapper.toEntity(request, department, manager);

    String characters =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    String password =
            RandomStringUtils.secure().next(8, characters);

    LOG.infof("Generated temporary password for employee: %s",
            request.getEmail());

    LOG.info(password + "password of employee");

    String hashedPassword =
            BCrypt.hashpw(password, BCrypt.gensalt());

    createEmployee.setPassword(hashedPassword);

    LOG.info("Persisting employee");

    repository.persist(createEmployee);

    LOG.infof("Employee created successfully. Id: %d, Email: %s",
            createEmployee.getId(),
            createEmployee.getEmail());

    return mapper.toResponse(createEmployee);
}

    public PaginatedResponse<EmployeeResponse> getAllEmployees(Integer page, Integer size) {

        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        if (page < 0) {
            throw new IllegalArgumentException("Page must be greater than or equal to 0");  // ✅
        }
        List<EmployeeResponse> employeeResponseList = employeeRepository.findAll().page(page, size).list().stream().map(current ->
                mapper.toResponse(current)).toList();

        long totalElements = employeeRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PaginatedResponse<EmployeeResponse>(employeeResponseList, totalPages, totalElements, page, size, page == 0, page >= totalPages - 1);
    }

    public EmployeeResponse getEmployee(long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            throw new NotFoundException("Employee not found");
        }
        return mapper.toResponse(employee);
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

        LOG.infof("Updating employee with id: %d", id);

        Employee employee = employeeRepository.findById(id);

        if (employee == null) {
            LOG.warnf("Employee not found with id: %d", id);
            throw new NotFoundException("Employee not found");
        }

        if (request.getManagerId() != null &&
                Boolean.TRUE.equals(request.getIsRemoveManager())) {
            LOG.warnf("Invalid request for employee %d. Cannot assign and remove manager simultaneously", id);
            throw new IllegalArgumentException(
                    "Cannot assign and remove manager simultaneously");
        }

        if (request.getEmail() != null &&
                !request.getEmail().equals(employee.getEmail())) {

            LOG.infof("Checking email uniqueness for: %s", request.getEmail());

            Employee existingEmployee = repository.findByEmail(request.getEmail());

            if (existingEmployee != null) {
                LOG.warnf("Email already exists: %s", request.getEmail());
                throw new IllegalArgumentException("Email already exists");
            }

            employee.setEmail(request.getEmail());
            LOG.infof("Updated email for employee %d", id);
        }

        if (request.getFirstName() != null) {
            employee.setFirstName(request.getFirstName());
            LOG.infof("Updated first name for employee %d", id);
        }

        if (request.getLastName() != null) {
            employee.setLastName(request.getLastName());
            LOG.infof("Updated last name for employee %d", id);
        }

        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
            LOG.infof("Updated status for employee %d to %s",
                    id, request.getStatus());
        }

        if (request.getEmploymentType() != null) {
            employee.setEmploymentType(request.getEmploymentType());
            LOG.infof("Updated employment type for employee %d to %s",
                    id, request.getEmploymentType());
        }

        if (request.getDesignation() != null) {
            employee.setDesignation(request.getDesignation());
            LOG.infof("Updated designation for employee %d", id);
        }

        if (request.getRole() != null) {

            if (request.getRole() == Role.MANAGER &&
                    employee.getManager() != null) {

                LOG.warnf("Employee %d cannot be promoted to MANAGER while assigned to a manager", id);

                throw new IllegalArgumentException(
                        "Manager cannot have a manager");
            }

            employee.setRole(request.getRole());

            LOG.infof("Updated role for employee %d to %s",
                    id, request.getRole());
        }

        if (request.getDepartmentId() != null) {

            Department department = departmentRepository
                    .findByIdOptional(request.getDepartmentId())
                    .orElseThrow(() -> {
                        LOG.warnf("Department not found with id: %d",
                                request.getDepartmentId());
                        return new NotFoundException("No Dept Found");
                    });

            employee.setDepartment(department);

            LOG.infof("Updated department for employee %d to department %d",
                    id, department.getId());
        }

        if (request.getManagerId() != null) {

            Employee manager = repository
                    .findByIdOptional(request.getManagerId())
                    .orElseThrow(() -> {
                        LOG.warnf("Manager not found with id: %d",
                                request.getManagerId());
                        return new NotFoundException("Manager Not found");
                    });

            if (manager.getId().equals(employee.getId())) {
                LOG.warnf("Employee %d attempted to assign themselves as manager", id);

                throw new IllegalArgumentException(
                        "Employee cannot be their own manager");
            }

            if (manager.getRole() != Role.MANAGER) {

                LOG.warnf("Employee %d selected employee %d as manager, but selected employee is not a manager",
                        id, manager.getId());

                throw new IllegalArgumentException(
                        "Selected employee is not Manager");
            }

            if (employee.getRole() == Role.MANAGER) {

                LOG.warnf("Manager %d cannot be assigned another manager", id);

                throw new IllegalArgumentException(
                        "Manager cannot have a manager");
            }

            employee.setManager(manager);

            LOG.infof("Assigned manager %d to employee %d",
                    manager.getId(), id);
        }
        else if (Boolean.TRUE.equals(request.getIsRemoveManager())) {

            employee.setManager(null);

            LOG.infof("Removed manager from employee %d", id);
        }

        LOG.infof("Employee updated successfully. Employee id: %d", id);

        return getEmployee(id);
    }

    @Transactional
    public EmployeeResponse updateStatusEmployee(Long id , UpdateEmployeeStatusRequest request){
        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            throw new NotFoundException("user not found");
        }
        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
        }
                return mapper.toResponse(employee);

    }

    public List<EmployeeResponse> getDirectReports(Long managerId) {
        List<Employee> reports = employeeRepository.findByManagerId(managerId);
        return mapper.toResponseList(reports);
    }

}
