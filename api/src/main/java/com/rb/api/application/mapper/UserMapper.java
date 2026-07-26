package com.rb.api.application.mapper;

import com.rb.api.application.dto.user.UpdateUserRequestDTO;
import com.rb.api.application.dto.user.UserResponseDTO;
import com.rb.api.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd/MM/yyyy HH:mm:ss")
    UserResponseDTO toResponseDTO(User user);
    void updateEntityFromDto(UpdateUserRequestDTO dto, @MappingTarget User user);
}