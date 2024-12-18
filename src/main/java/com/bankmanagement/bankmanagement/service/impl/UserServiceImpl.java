package com.bankmanagement.bankmanagement.service.impl;

import com.bankmanagement.bankmanagement.dto.UserRequestDTO;
import com.bankmanagement.bankmanagement.dto.UserResponseDTO;
import com.bankmanagement.bankmanagement.exception.BadRequestException;
import com.bankmanagement.bankmanagement.mapper.UserMapper;
import com.bankmanagement.bankmanagement.model.User;
import com.bankmanagement.bankmanagement.repository.UserRepository;
import com.bankmanagement.bankmanagement.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO register(UserRequestDTO userRequestDTO) {
        if (userRepository.findByDocumentId(userRequestDTO.getDocumentId()).isPresent()) {
            throw new BadRequestException("Document ID already exists.");
        }
        User savedUser = userRepository.save(UserMapper.toEntity(userRequestDTO));
        return UserMapper.fromEntity(savedUser);
    }
}
