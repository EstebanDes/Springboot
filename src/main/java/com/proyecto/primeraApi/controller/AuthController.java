
package com.proyecto.primeraApi.controller;

import com.proyecto.primeraApi.dto.LoginRequestDTO;
import com.proyecto.primeraApi.dto.LoginResponseDTO;
import com.proyecto.primeraApi.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO datos) {

        if (datos.correo() == null || datos.correo().isBlank()
                || datos.password() == null || datos.password().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Correo y contraseña obligatorios"
            );
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        datos.correo(),
                        datos.password()
                )
        );

        String token = jwtService.generarToken(authentication);

        return new LoginResponseDTO(token);
    }
}
