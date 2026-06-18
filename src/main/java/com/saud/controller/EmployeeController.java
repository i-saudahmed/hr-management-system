package com.saud.controller;

import com.saud.dto.request.CreateEmployeeRequest;
import com.saud.dto.response.EmployeeResponse;
import com.saud.service.EmployeeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RequestScoped
@Path("/api/employees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeController {

    @Inject
    EmployeeService employeeService;

    @POST
    public EmployeeResponse createEmployee(@Valid CreateEmployeeRequest request) {
        return employeeService.createEmployee(request);
    }
}
