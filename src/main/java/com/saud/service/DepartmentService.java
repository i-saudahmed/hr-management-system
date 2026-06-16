package com.saud.service;

import com.saud.dto.pagination.PageResponse;
import com.saud.dto.request.CreateDepartmentRequest;
import com.saud.dto.response.DepartmentResponse;
import com.saud.entity.Department;
import com.saud.repository.DepartmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DepartmentService {

    @Inject
    DepartmentRepository repository;

    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        Optional<Department> department = repository.findByName(request.getName());
        if (department.isPresent()) {
            throw new IllegalArgumentException("Department already exists");
        }
        Department createDepartment = new Department();
        createDepartment.setName(request.getName());

        repository.persist(createDepartment);

        return toResponse(createDepartment);
    }

    public PageResponse<DepartmentResponse> getAllDepartments(Integer page, Integer size) {
        List<DepartmentResponse> pageDept = repository.findAll().
                page(page, size)
                .list().stream().
                map(dept ->
                        new DepartmentResponse(dept.getId(),
                                dept.getName(), dept.getStatus(),
                                dept.getCreatedAt(), dept.getUpdatedAt()))
                .toList();


        long totalElements = repository.count();

        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PageResponse<>(
                pageDept,
                totalPages,
                totalElements,
                page,
                size,
                page == 0,
                page >= totalPages - 1
        );
    }

    public static DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder().id(department.getId()).name(department.getName()).status(department.getStatus()).createdAt(department.getCreatedAt()).updatedAt(department.getUpdatedAt()).build();
    }
}
