package com.bankmanagement.bankmanagement.mapper;

import com.bankmanagement.bankmanagement.dto.UserRequestDTO;
import com.bankmanagement.bankmanagement.dto.UserResponseDTO;
import com.bankmanagement.bankmanagement.model.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setDocumentId(userRequestDTO.getDocumentId());
        return user;
    }

    public static UserResponseDTO fromEntity(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setDocumentId(user.getDocumentId());
        return userResponseDTO;
    }

}
