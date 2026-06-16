package com.saud.controller;

import com.saud.dto.pagination.PageResponse;
import com.saud.dto.request.CreateDepartmentRequest;
import com.saud.dto.response.DepartmentResponse;
import com.saud.service.DepartmentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Path("/api/departments")
public class DepartmentController {

    @Inject
    DepartmentService departmentService;

    @POST
    public DepartmentResponse createDepartment(@Valid CreateDepartmentRequest request) {
        return departmentService.createDepartment(request);
    }

    @GET
    public PageResponse<DepartmentResponse> getAllDepartments(@DefaultValue("0") @QueryParam("page") Integer page, @DefaultValue("2") @QueryParam("size") Integer size) {
        return departmentService.getAllDepartments(page, size);
    }
}
