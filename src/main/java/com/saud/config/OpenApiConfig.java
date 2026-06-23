package com.saud.config;

@org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition(
        info = @org.eclipse.microprofile.openapi.annotations.info.Info(
                title = "HRMS API",
                version = "1.0",
                description = "Human Resource Management System API"
        ),
        security = @org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement(name = "JWT")
)
@org.eclipse.microprofile.openapi.annotations.security.SecurityScheme(
        securitySchemeName = "JWT",
        type = org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}