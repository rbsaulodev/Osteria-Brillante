package com.rb.api.application.service;

import com.rb.api.application.dto.auth.RegisterDTO;
import com.rb.api.application.dto.auth.TokenResponseDTO;
import com.rb.api.application.exception.EmailAlreadyExistsException;
import com.rb.api.domain.model.User;
import com.rb.api.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findEntityByEmail(email);
    }

    @Transactional
    public TokenResponseDTO register(RegisterDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new EmailAlreadyExistsException("O email informado já está em uso: " + dto.email());
        }

        String hashedPassword = passwordEncoder.encode(dto.password());
        User newUser = User.createCustomer(dto.fullName(), dto.email(), hashedPassword);

        User savedUser = userRepository.save(newUser);
        String token = tokenService.generateToken(savedUser);

        return new TokenResponseDTO(token, savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
    }

    private User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
    }
}