package com.rb.api.application.service;

import com.rb.api.application.dto.user.*;
import com.rb.api.application.exception.EmailAlreadyExistsException;
import com.rb.api.application.exception.ResourceNotFoundException;
import com.rb.api.application.mapper.UserMapper;
import com.rb.api.domain.model.User;
import com.rb.api.domain.enums.UserRole;
import com.rb.api.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) { // 3. Adicionar no construtor
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public UserResponseDTO changePassword(UUID id, UserChangePasswordRequestDTO dto){
        User user = findEntityById(id);

        String hashedNewPassword = passwordEncoder.encode(dto.newPassword());
        user.changePassword(hashedNewPassword);

        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO create(CreateUserRequestDTO dto) {
        userRepository.findByEmail(dto.email()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("O email informado já está em uso: " + dto.email());
        });

        String hashedPassword = passwordEncoder.encode(dto.password());

        User newUser = (dto.role() == UserRole.CUSTOMER)
                ? User.createCustomer(dto.fullName(), dto.email(), hashedPassword)
                : User.createEmployee(dto.fullName(), dto.email(), hashedPassword, dto.role());

        User savedUser = userRepository.save(newUser);
        return userMapper.toResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO changeRole(UUID id, UpdateUserRoleRequestDTO dto) {
        User user = findEntityById(id);
        user.changeRole(dto.newRole());
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateDetails(UUID id, UpdateUserRequestDTO dto) {
        User user = findEntityById(id);
        user.updateDetails(dto.fullName(), dto.email());
        return userMapper.toResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    @Transactional
    public void deleteById(UUID id){
        User user = findEntityById(id);
        userRepository.deleteById(id);
    }

    private User findEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
    }
}