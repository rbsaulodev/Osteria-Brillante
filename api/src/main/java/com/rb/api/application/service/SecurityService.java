package com.rb.api.application.service;

import com.rb.api.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
public class SecurityService {
    public boolean isOwner(UUID targetId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            return false;
        }

        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId().equals(targetId);
    }
}