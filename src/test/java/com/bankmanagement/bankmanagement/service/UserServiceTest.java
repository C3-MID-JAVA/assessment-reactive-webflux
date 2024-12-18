package com.bankmanagement.bankmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bankmanagement.bankmanagement.dto.UserRequestDTO;
import com.bankmanagement.bankmanagement.dto.UserResponseDTO;
import com.bankmanagement.bankmanagement.exception.BadRequestException;
import com.bankmanagement.bankmanagement.model.User;
import com.bankmanagement.bankmanagement.repository.UserRepository;
import com.bankmanagement.bankmanagement.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // Positive
    @Test
    void shouldRegisterUserSuccessfully() {
        UserRequestDTO userRequest = new UserRequestDTO("John Doe", "123456789");
        User user = new User("675e0e1259d6de4eda5b29b7", userRequest.getName(), userRequest.getDocumentId());

        when(userRepository.findByDocumentId(userRequest.getDocumentId())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.register(userRequest);

        assertNotNull(response);
        assertEquals(userRequest.getName(), response.getName());
        assertEquals(userRequest.getDocumentId(), response.getDocumentId());
        verify(userRepository, times(1)).findByDocumentId(userRequest.getDocumentId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Negative
    @Test
    void whenDocumentIdAlreadyExists_shouldThrowException() {
        UserRequestDTO userRequest = new UserRequestDTO("Jane Doe", "987654321");
        when(userRepository.findByDocumentId(userRequest.getDocumentId())).thenReturn(Optional.of(new User()));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> userService.register(userRequest)
        );

        assertEquals("Document ID already exists.", exception.getMessage());
        verify(userRepository, times(1)).findByDocumentId(userRequest.getDocumentId());
        verify(userRepository, never()).save(any(User.class));
    }
}
