package com.gflopes.bookstoremanager.users.mapper;

import com.gflopes.bookstoremanager.users.dto.UserDTO;
import com.gflopes.bookstoremanager.users.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userDTO.role", target = "role")
    User toModel(UserDTO userDTO);

    UserDTO toDTO(User user);
}
