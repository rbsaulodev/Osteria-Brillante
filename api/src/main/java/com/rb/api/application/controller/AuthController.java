package com.rb.api.application.controller;

import com.rb.api.application.dto.auth.AuthDTO;
import com.rb.api.application.dto.auth.RegisterDTO;
import com.rb.api.application.dto.auth.TokenResponseDTO;
import com.rb.api.application.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid AuthDTO dto) {
        TokenResponseDTO token = authService.authenticate(dto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody @Valid RegisterDTO dto) {
        TokenResponseDTO token = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }
}