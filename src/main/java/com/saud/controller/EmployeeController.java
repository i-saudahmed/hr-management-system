package com.saud.controller;

import com.saud.dto.pagination.PaginatedResponse;
import com.saud.dto.request.CreateEmployeeRequest;
import com.saud.dto.request.UpdateEmployeeRequest;
import com.saud.dto.request.UpdateEmployeeStatusRequest;
import com.saud.dto.response.EmployeeResponse;
import com.saud.service.EmployeeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

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

    @GET
    public PaginatedResponse<EmployeeResponse> getAllEmployees(@DefaultValue("0") @QueryParam("page") Integer page, @DefaultValue("10") @QueryParam("size") Integer size) {
        return employeeService.getAllEmployees(page, size);
    }

    @GET
    @Path("/{id}")
    public Response getEmployee(@PathParam("id") Long id) {
        EmployeeResponse response = employeeService.getEmployee(id);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEmployee(@PathParam("id") Long id) {
        employeeService.deleteEmployee(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateEmployee(@PathParam("id") Long id, UpdateEmployeeRequest request) {
      EmployeeResponse response =   employeeService.updateEmployee(id, request) ;
        return Response.ok(response).build();
    }

    @PATCH
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") Long id, @Valid UpdateEmployeeStatusRequest request){
        EmployeeResponse response = employeeService.updateStatusEmployee(id, request);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}/team")
    public Response getDirectReports(@PathParam("id") Long id) {
        List<EmployeeResponse> team = employeeService.getDirectReports(id);
        return Response.ok(team).build();
    }

}

