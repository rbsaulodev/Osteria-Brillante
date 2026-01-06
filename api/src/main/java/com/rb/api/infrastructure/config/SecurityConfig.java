package com.rb.api.infrastructure.config;

import com.rb.api.application.service.AuthService;
import com.rb.api.application.service.TokenService;
import com.rb.api.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilter securityFilter(TokenService tokenService, UserDetailsService userDetailsService, UserRepository userRepository, AuthService authService) {
        return new SecurityFilter(tokenService, userDetailsService, userRepository, authService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, SecurityFilter securityFilter) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/menu", "/tables/available").permitAll()

                        .requestMatchers(HttpMethod.PATCH, "/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/users/{id}/password").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/{id}").authenticated()

                        .requestMatchers(HttpMethod.GET, "/orders/kitchen").hasAnyRole("ADMIN", "COOK")
                        .requestMatchers(HttpMethod.PATCH, "/orders/{id}/status/cook").hasAnyRole("ADMIN", "COOK")

                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/menu/**", "/tables/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}