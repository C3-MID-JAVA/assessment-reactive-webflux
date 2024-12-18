package com.bankmanagement.bankmanagement.service;

import com.bankmanagement.bankmanagement.dto.UserRequestDTO;
import com.bankmanagement.bankmanagement.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO register(UserRequestDTO userRequestDTO);
}
