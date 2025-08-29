package com.rb.api.domain.model;

import com.rb.api.domain.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
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

}