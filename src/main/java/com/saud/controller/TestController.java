package com.saud.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
@Path("/api")
public class TestController {

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/test")
    public String test() {
        return "User: " + jwt.getSubject();
    }

}
