package com.example.bankcards.mappers;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.user.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    User toEntity(UserDTO dto);

}
