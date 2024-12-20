package ec.com.example.bank_account.controller;

import ec.com.example.bank_account.dto.UserRequestDTO;
import ec.com.example.bank_account.dto.UserResponseDTO;
import ec.com.example.bank_account.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        webTestClient = WebTestClient.bindToController(userController).build();
    }

    @Test
    void createUser_ShouldReturnCreatedStatus_WhenUserIsCreatedSuccessfully() {
        UserRequestDTO userRequest = new UserRequestDTO("Diego", "Loor",
                "1310000000", "diego.loor@sofka.com.co","Diego123.", "ACTIVE");
        UserResponseDTO userResponse = new UserResponseDTO("Diego", "Loor",
                "1310000000", "diego.loor@sofka.com.co", "ACTIVE");

        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(Mono.just(userResponse));

        webTestClient.post()
                .uri("/api/users")
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.ci").isEqualTo("1310000000")
                .jsonPath("$.email").isEqualTo("diego.loor@sofka.com.co");

        verify(userService).createUser(any(UserRequestDTO.class));
    }

    @Test
    public void getAllUsers_shouldReturn200_WhenUsersExist() {
        UserResponseDTO userResponse = new UserResponseDTO("Diego", "Loor",
                "1310000000", "diego.loor@sofka.com.co", "ACTIVE");

        when(userService.getAllUsers()).thenReturn(Flux.just(userResponse));

        webTestClient.get()
                .uri("/api/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDTO.class)
                .hasSize(1);

        verify(userService).getAllUsers();
    }

}