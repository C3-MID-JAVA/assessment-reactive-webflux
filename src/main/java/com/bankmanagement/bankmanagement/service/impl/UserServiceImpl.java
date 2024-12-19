package com.bankmanagement.bankmanagement.service.impl;

import com.bankmanagement.bankmanagement.dto.UserRequestDTO;
import com.bankmanagement.bankmanagement.dto.UserResponseDTO;
import com.bankmanagement.bankmanagement.exception.BadRequestException;
import com.bankmanagement.bankmanagement.mapper.UserMapper;
import com.bankmanagement.bankmanagement.repository.UserRepository;
import com.bankmanagement.bankmanagement.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserResponseDTO> register(UserRequestDTO userRequestDTO) {
        return userRepository.findByDocumentId(userRequestDTO.getDocumentId())
                .flatMap(existingUser -> Mono.error(new BadRequestException("Document ID already exists.")))
                .then(Mono.defer(() ->
                        userRepository.save(UserMapper.toEntity(userRequestDTO))
                                .map(UserMapper::fromEntity)
                ));
    }

    @Override
    public Flux<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().map(UserMapper::fromEntity);
    }
}
