package com.bankmanagement.bankmanagement.routes;

import com.bankmanagement.bankmanagement.dto.UserRequestDTO;
import com.bankmanagement.bankmanagement.dto.UserResponseDTO;
import com.bankmanagement.bankmanagement.exception.BadRequestException;
import com.bankmanagement.bankmanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    private UserRequestDTO validUserRequest;
    private UserResponseDTO userResponse;

    @BeforeEach
    void setUp() {
        validUserRequest = new UserRequestDTO("John Doe", "12345678");
        userResponse = new UserResponseDTO("675e0e1259d6de4eda5b29b7", "John Doe", "12345678");
    }

    @Test
    void register_ValidUser_ReturnsCreatedResponse() {
        when(userService.register(any(UserRequestDTO.class))).thenReturn(Mono.just(userResponse));

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("675e0e1259d6de4eda5b29b7")
                .jsonPath("$.name").isEqualTo("John Doe")
                .jsonPath("$.documentId").isEqualTo("12345678");

        verify(userService, times(1)).register(any(UserRequestDTO.class));
    }

    @Test
    void register_DuplicateUser_ReturnsBadRequest() {
        when(userService.register(any(UserRequestDTO.class)))
                .thenReturn(Mono.error(new BadRequestException("Document ID already exists.")));

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Document ID already exists.");

        verify(userService, times(1)).register(any(UserRequestDTO.class));
    }

    @Test
    void register_EmptyDocumentId_ReturnsBadRequest() {
        UserRequestDTO invalidUserRequest = new UserRequestDTO();
        invalidUserRequest.setDocumentId("");
        invalidUserRequest.setName("John Doe");

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUserRequest)
                .exchange()
                .expectStatus().isBadRequest();

        verify(userService, never()).register(any(UserRequestDTO.class));
    }
}
