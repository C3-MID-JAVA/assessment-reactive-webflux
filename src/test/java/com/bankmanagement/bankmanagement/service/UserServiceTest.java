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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
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
        UserResponseDTO expectedResponse = new UserResponseDTO("675e0e1259d6de4eda5b29b7", "John Doe", "123456789");

        when(userRepository.findByDocumentId(userRequest.getDocumentId())).thenReturn(Mono.empty());
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(userService.register(userRequest))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(expectedResponse.getId(), response.getId());
                    assertEquals(expectedResponse.getName(), response.getName());
                    assertEquals(expectedResponse.getDocumentId(), response.getDocumentId());
                })
                .verifyComplete();

        verify(userRepository, times(1)).findByDocumentId(userRequest.getDocumentId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Negative
    @Test
    void whenDocumentIdAlreadyExists_shouldThrowException() {
        UserRequestDTO userRequest = new UserRequestDTO("Jane Doe", "987654321");
        User existingUser = new User("123", "Jane Doe", "987654321");

        when(userRepository.findByDocumentId(userRequest.getDocumentId())).thenReturn(Mono.just(existingUser));

        StepVerifier.create(userService.register(userRequest))
                .expectErrorMatches(ex -> ex instanceof BadRequestException &&
                        ex.getMessage().equals("Document ID already exists."))
                .verify();

        verify(userRepository, times(1)).findByDocumentId(userRequest.getDocumentId());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldReturnUsersSuccesfully(){
        List<User> users = List.of(
                new User("675e0e1259d6de4eda5b29b6", "Jhon Doe", "12345678"),
                new User("675e0e1259d6de4eda5b29b", "No name", "12345679")
        );

        when(userRepository.findAll()).thenReturn(Flux.fromIterable(users));

        Flux<UserResponseDTO> response = userService.getAllUsers();

        StepVerifier.create(response)
                .expectNextMatches(userResponseDTO -> "12345678".equals(userResponseDTO.getDocumentId()))
                .expectNextMatches(userResponseDTO -> "12345679".equals(userResponseDTO.getDocumentId()))
                .verifyComplete();

        verify(userRepository, times(1)).findAll();
    }
}
