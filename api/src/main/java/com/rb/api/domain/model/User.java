package com.rb.api.domain.model;

import com.rb.api.application.dto.user.CreateUserRequestDTO;
import com.rb.api.domain.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected User(String fullName, String email, String passwordHash, UserRole role) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(CreateUserRequestDTO dto) {
        this.fullName = dto.fullName();
        this.email = dto.email();
        this.passwordHash = dto.email();
        this.role = dto.role();
    }

    public void changePassword(String newPasswordHash) {
        if (newPasswordHash == null || newPasswordHash.isBlank()) {
            throw new IllegalArgumentException("A nova senha não pode ser vazia.");
        }
        this.passwordHash = newPasswordHash;
    }

    public void updateDetails(String newFullName, String newEmail) {
        if (newFullName != null && !newFullName.isBlank()) {
            this.fullName = newFullName;
        }
        if (newEmail != null && !newEmail.isBlank()) {
            this.email = newEmail;
        }
    }

    public static User createCustomer(String fullName, String email, String passwordHash) {
        return new User(fullName, email, passwordHash, UserRole.CUSTOMER);
    }

    public static User createEmployee(String fullName, String email, String passwordHash, UserRole role) {
        if (role == UserRole.CUSTOMER) {
            throw new IllegalArgumentException("Para criar um cliente, use o método createCustomer.");
        }
        return new User(fullName, email, passwordHash, role);
    }

    public void changeRole(UserRole newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("A nova função não pode ser nula.");
        }

        if (this.role == UserRole.CUSTOMER) {
            throw new IllegalStateException("Não é possível alterar a função de um cliente por este método.");
        }

        this.role = newRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_CUSTOMER"),
                    new SimpleGrantedAuthority("ROLE_WAITER"),
                    new SimpleGrantedAuthority("ROLE_COOK")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}