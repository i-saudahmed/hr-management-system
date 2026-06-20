package mapper;

import com.saud.dto.request.CreateEmployeeRequest;
import com.saud.dto.response.EmployeeResponse;
import com.saud.entity.Department;
import com.saud.entity.Employee;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class EmployeeMapper {

    public EmployeeResponse toResponse(Employee employee){
            if (employee == null) {
                return  null ;
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
                .designation(employee.getDesignation())
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
                .isRemoveManager(employee.getIsRemoveManager() != null ? employee.getIsRemoveManager() : null )
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();

    }

    public List<EmployeeResponse> toResponseList(List<Employee> employees){
        return  employees.stream().map(current -> toResponse(current)).toList();
    }

    public Employee toEntity(CreateEmployeeRequest request  , Department department , Employee manager){
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
