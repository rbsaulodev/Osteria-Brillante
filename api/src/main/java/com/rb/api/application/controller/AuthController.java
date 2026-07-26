package com.rb.api.application.controller;

import com.rb.api.application.dto.auth.AuthDTO;
import com.rb.api.application.dto.auth.RegisterDTO;
import com.rb.api.application.dto.auth.TokenResponseDTO;
import com.rb.api.application.service.AuthService;
import com.rb.api.application.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.rb.api.domain.model.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid AuthDTO dto) {
        var authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication auth = authenticationManager.authenticate(authToken);

        User user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new TokenResponseDTO(token, user.getId(), user.getEmail(), user.getRole()));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody @Valid RegisterDTO dto) {
        TokenResponseDTO token = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }
}