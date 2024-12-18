package com.bankmanagement.bankmanagement.service;

import com.bankmanagement.bankmanagement.dto.UserRequestDTO;
import com.bankmanagement.bankmanagement.dto.UserResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResponseDTO> register(UserRequestDTO userRequestDTO);
    Flux<UserResponseDTO> getAllUsers();
}
