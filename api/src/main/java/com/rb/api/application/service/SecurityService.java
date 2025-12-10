package com.rb.api.application.service;

import com.rb.api.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Collection;

@Service("securityService")
public class SecurityService {

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }

    public boolean isOwner(UUID targetId) {
        User currentUser = getAuthenticatedUser();
        if (currentUser == null) {
            // Este caso já deveria ter sido pego no ponto 1, se não,
            // significa que o @PreAuthorize foi avaliado antes do filtro JWT.
            System.out.println("DEBUG: isOwner - Current user is null.");
            return false;
        }

        System.out.println("DEBUG: isOwner - Current User ID (from token): " + currentUser.getId());
        System.out.println("DEBUG: isOwner - Target ID (from URL): " + targetId);

        boolean result = currentUser.getId().equals(targetId);
        System.out.println("DEBUG: isOwner - Result: " + result);
        return result;
    }

    public boolean hasRole(String roleName) {
        User currentUser = getAuthenticatedUser();
        if (currentUser == null) {
            return false;
        }

        String fullRoleName = roleName.toUpperCase().startsWith("ROLE_") ? roleName.toUpperCase() : "ROLE_" + roleName.toUpperCase();

        Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();

        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(fullRoleName));
    }

    public UUID getCurrentUserId() {
        User currentUser = getAuthenticatedUser();
        if (currentUser == null) {
            throw new SecurityException("Usuário não autenticado no contexto de segurança.");
        }
        return currentUser.getId();
    }
}