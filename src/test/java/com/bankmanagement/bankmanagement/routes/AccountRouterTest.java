package com.bankmanagement.bankmanagement.routes;

import com.bankmanagement.bankmanagement.dto.AccountRequestDTO;
import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;
import com.bankmanagement.bankmanagement.exception.NotFoundException;
import com.bankmanagement.bankmanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AccountRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AccountService accountService;

    private AccountRequestDTO validAccountRequest;
    private AccountResponseDTO accountResponse;

    @BeforeEach
    void setUp() {
        validAccountRequest = new AccountRequestDTO("675e0e1259d6de4eda5b29b7");
        accountResponse = new AccountResponseDTO("12345678", 0.0, "675e0e1259d6de4eda5b29b7");
    }

    @Test
    void create_validAccount_ReturnsCreatedResponse() {
        when(accountService.create(any(AccountRequestDTO.class))).thenReturn(Mono.just(accountResponse));

        webTestClient.post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validAccountRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("12345678")
                .jsonPath("$.balance").isEqualTo(0.0)
                .jsonPath("$.userId").isEqualTo("675e0e1259d6de4eda5b29b7");

        verify(accountService, times(1)).create(any(AccountRequestDTO.class));
    }

    @Test
    void create_DuplicateUser_ReturnsBadRequest() {
        when(accountService.create(any(AccountRequestDTO.class)))
                .thenReturn(Mono.error(new NotFoundException("User not found")));

        webTestClient.post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validAccountRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("User not found");

        verify(accountService, times(1)).create(any(AccountRequestDTO.class));
    }

    @Test
    void create_EmptyUserId_ReturnsBadRequest() {
        AccountRequestDTO invalidAccountRequest = new AccountRequestDTO();
        invalidAccountRequest.setUserId("");

        webTestClient.post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidAccountRequest)
                .exchange()
                .expectStatus().isBadRequest();

        verify(accountService, never()).create(any(AccountRequestDTO.class));
    }

    @Test
    void getAllByUserId_validUser_ReturnsAccountsList() {
        List<AccountResponseDTO> accountList = List.of(accountResponse);

        when(accountService.getAllByUserId(anyString())).thenReturn(Flux.fromIterable(accountList));

        webTestClient.get()
                .uri("/accounts/{userId}/user", "675e0e1259d6de4eda5b29b7")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].accountNumber").isEqualTo("12345678")
                .jsonPath("$[0].balance").isEqualTo(0.0)
                .jsonPath("$[0].userId").isEqualTo("675e0e1259d6de4eda5b29b7");

        verify(accountService, times(1)).getAllByUserId("675e0e1259d6de4eda5b29b7");
    }

    @Test
    void getByAccountNumber_validAccount_ReturnsAccount() {
        when(accountService.findByAccountNumber(anyString())).thenReturn(Mono.just(accountResponse));

        webTestClient.get()
                .uri("/accounts/{id}", "12345678")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("12345678")
                .jsonPath("$.balance").isEqualTo(0.0)
                .jsonPath("$.userId").isEqualTo("675e0e1259d6de4eda5b29b7");

        verify(accountService, times(1)).findByAccountNumber("12345678");
    }

    @Test
    void getByAccountNumber_accountNotFound_ReturnsNotFound() {
        when(accountService.findByAccountNumber(anyString()))
                .thenReturn(Mono.error(new NotFoundException("Account not found")));

        webTestClient.get()
                .uri("/accounts/{id}", "99999999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Account not found");

        verify(accountService, times(1)).findByAccountNumber("99999999");
    }
}
