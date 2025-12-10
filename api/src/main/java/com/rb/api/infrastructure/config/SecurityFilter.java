package com.rb.api.infrastructure.config;

import com.rb.api.application.service.AuthService;
import com.rb.api.application.service.TokenService;
import com.rb.api.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final AuthService authService;

    public SecurityFilter(TokenService tokenService, UserDetailsService userDetailsService, UserRepository userRepository, AuthService authService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath() + (request.getPathInfo() != null ? request.getPathInfo() : "");
        if (request.getMethod().equalsIgnoreCase("POST") &&
                (path.endsWith("/auth/login") || path.endsWith("/auth/register")))
        {
            filterChain.doFilter(request, response);
            return;
        }

        String token = recoverToken(request);

        if (token != null) {
            String userIdString = tokenService.validateToken(token);

            if (!userIdString.isEmpty()) {
                try {
                    UUID userId = UUID.fromString(userIdString);
                    UserDetails user = authService.loadUserById(userId);

                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (IllegalArgumentException e) {
                    System.err.println("Erro ao converter Subject do token para UUID: " + userIdString);
                } catch (UsernameNotFoundException e) {
                    System.err.println("Usuário não encontrado para o ID no token: " + userIdString);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    public UserDetails loadUserById(UUID id) throws UsernameNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o ID: " + id));
    }
}