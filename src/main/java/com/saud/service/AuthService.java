package com.saud.service;

import com.saud.dto.request.AuthRequest;
import com.saud.dto.response.AuthResponse;
import com.saud.entity.Employee;
import com.saud.repository.EmployeeRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Inject
    EmployeeRepository employeeRepository ;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer ;


    public AuthResponse login(AuthRequest request){

        Employee employee = employeeRepository.findByEmail(request.getEmail()) ;

        if (employee == null) {
            throw new NotFoundException("Invalid email or password");
        }

        if (!BcryptUtil.matches(request.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

       String token = Jwt.issuer(issuer)
               .subject(employee.getId().toString())
               .groups(Set.of(employee.getRole().name()))
               .claim("employeeId", employee.getId())
               .claim("email", employee.getEmail())
               .claim("role", employee.getRole().name())
               .expiresIn(Duration.ofHours(24))
               .sign();

        return AuthResponse.builder()
                .token(token)
                .role(employee.getRole().name())
                .employeeId(employee.getId())
                .name(employee.getFirstName() + " " + employee.getLastName())
                .build();
    }
}
