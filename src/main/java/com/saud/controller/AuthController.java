package com.saud.controller;

import com.saud.dto.request.AuthRequest;
import com.saud.dto.response.AuthResponse;
import com.saud.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(@Valid AuthRequest request) {
        AuthResponse response = authService.login(request);
        return Response.ok(response).build();
    }
}