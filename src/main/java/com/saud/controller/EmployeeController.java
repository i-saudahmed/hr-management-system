package com.saud.controller;

import com.saud.dto.request.CreateEmployeeRequest;
import com.saud.dto.response.EmployeeResponse;
import com.saud.service.EmployeeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/api/employees")
@ApplicationScoped
public class EmployeeController {

    EmployeeService service ;

    @POST
    public EmployeeResponse createEmployee(@Valid CreateEmployeeRequest request){
        return  service.createEmployee(request);
    }
}
