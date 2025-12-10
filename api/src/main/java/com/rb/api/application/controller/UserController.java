package com.rb.api.application.controller;

import com.rb.api.application.dto.auth.RegisterDTO;
import com.rb.api.application.dto.user.*;
import com.rb.api.application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createEmployee(@RequestBody @Valid CreateEmployeeRequestDTO dto){
        UserResponseDTO user = userService.createEmployee(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public ResponseEntity<UserResponseDTO> updateDetails(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserRequestDTO dto
    ){
        UserResponseDTO user = userService.updateDetails(id, dto);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public ResponseEntity<UserResponseDTO> changePassword(
            @PathVariable UUID id,
            @RequestBody @Valid UserChangePasswordRequestDTO dto
    ){
        UserResponseDTO user = userService.changePassword(id, dto);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/change-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> changeRole(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserRoleRequestDTO dto
    ){
        UserResponseDTO user = userService.changeRole(id, dto);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> findAll(){
        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id){
        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}